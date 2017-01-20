package com.bangware.shengyibao.ladingbilling.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.deliverynote.view.BluetoothPrinterActivity;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.main.view.MainActivity;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.printer.BluetoothService;
import com.bangware.shengyibao.printer.BtDevice;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.wch.wchusbdriver.CH34xAndroidDriver.TAG;

/**
 * 余卸货单打印
 */
public class StockBluetoothPrinterActivity extends BaseActivity {
    // Debugging
    private static final String TAG = "StockBluetoothPrinterActivity";
    private static final boolean D = true;
    // 获取设备请求码
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // 设备连接名字
    private String mConnectedDeviceName = null;
    // 蓝牙数据源
    private BluetoothAdapter mBluetoothAdapter = null;
    // 蓝牙服务
    private BluetoothService mService = null;

    //控件声明
    private ImageButton titleGoBack;
    private Button mPrintTicketBtn;
    private TextView mBluetooth_one,mBluetooth_two,mBluetooth_three;
    List<Product> productList;
    List<DisburdenGoods> goodsList;
    private String serial_numbers;

    private boolean isFirst=true;
    private boolean isScenned=true;
    private boolean isThried=true;
    private Drawable drawable = null;
    private int len=0;
    // Return Intent extra
    private BluetoothDeviceAdapter mPairedDevicesArrayAdapter;
    private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
    private List<BtDevice> mPairedDevices = new ArrayList<BtDevice>();
    private List<BtDevice> mNewDevices = new ArrayList<BtDevice>();

    private User user;
    private LadingbillingQuery ladingbillingQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_bluetooth_printer);

        SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME,MODE_PRIVATE);
        user = AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        // 获取本地蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //接收List<Object>集合方法
        productList = (List<Product>) getIntent().getSerializableExtra("product");
        ladingbillingQuery = (LadingbillingQuery) getIntent().getSerializableExtra("carNumber");
        goodsList= (List<DisburdenGoods>) getIntent().getSerializableExtra("DisburdenGoods");
        serial_numbers=getIntent().getStringExtra("serial_num");
        Log.e("serial_numbers",serial_numbers);
        // 如果adapter为null则表示不支持蓝牙
        if (mBluetoothAdapter == null) {
            showMessage("蓝牙设备不可用");
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 如果蓝牙没有开启，则请求开启
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mService == null) {
                this.initDeviceList();
                init();
            }
        }
    }

    /**
     * 监听申请蓝牙打开窗口返回事件
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    init();
                    initDeviceList();
//				doDiscovery();
                } else {
                    // User did not enable Bluetooth or an error occured
                    showToast("蓝牙没有启动，无法打印单据！");
                    Intent intent=new Intent(this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity
        // returns.
        if (mService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    private void initDeviceList() {
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new BluetoothDeviceAdapter(this, R.layout.bluetooth_device_item, R.id.device_name, mPairedDevices);
        mNewDevicesArrayAdapter = new BluetoothDeviceAdapter(this, R.layout.bluetooth_device_item, R.id.device_name, mNewDevices);
        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.stock_paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.stock_new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            // findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevices.add(new BtDevice(device.getName(), device.getAddress(), false));
            }
        }
        mPairedDevicesArrayAdapter.notifyDataSetChanged();
    }

    private void init() {
        Button scanButton = (Button) findViewById(R.id.StockBluetoothPrinter_searchDevicesBtn);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDiscovery();
                // v.setVisibility(View.GONE);
            }
        });
        titleGoBack = (ImageButton) findViewById(R.id.StockBluetoothPrinter_GobackBtn);
        titleGoBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockBluetoothPrinterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        // Initialize the send button with a listener that for click events
        mPrintTicketBtn = (Button) findViewById(R.id.StockBluetoothPrinter_printTickedBtn);//打印小票按钮
        mBluetooth_one= (TextView) findViewById(R.id.StockBluetoothPrinter_one);//选择打印一次
        mBluetooth_two= (TextView) findViewById(R.id.StockBluetoothPrinter_two);//选择打印两次
        mBluetooth_three= (TextView) findViewById(R.id.StockBluetoothPrinter_three);//选择打印三次

        MyOnClickListener clickListener = new MyOnClickListener();
        mPrintTicketBtn.setOnClickListener(clickListener);
        mBluetooth_one.setOnClickListener(clickListener);
        mBluetooth_two.setOnClickListener(clickListener);
        mBluetooth_three.setOnClickListener(clickListener);
        // Initialize the BluetoothService to perform bluetooth connections
        mService = new BluetoothService(this, mHandler);

        if (isFirst)
        {
            len=1;
            drawable = getResources().getDrawable(R.drawable.setting_check_on);
            isFirst=false;
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
        mBluetooth_one.setCompoundDrawables(null, null, drawable, null);//设置图片在文字右边
    }

    private class MyOnClickListener implements View.OnClickListener {
        CountDownTimer timer;
        @Override
        public void onClick(View v) {
            int MID = v.getId();
            //动态设置textview drawableRight属性
            //定时跳转
            if (MID==R.id.StockBluetoothPrinter_one)
            {
                if (isFirst)
                {
                    len=1;
                    drawable = getResources().getDrawable(R.drawable.setting_check_off);
                    isFirst=false;
                }else
                {
                    Drawable drawable1=null;
                    len=1;
                    drawable=getResources().getDrawable(R.drawable.setting_check_on);
                    drawable1=getResources().getDrawable(R.drawable.setting_check_off);
                    drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                    mBluetooth_two.setCompoundDrawables(null, null, drawable1, null);//设置图片在文字右边
                    mBluetooth_three.setCompoundDrawables(null, null, drawable1, null);//设置图片在文字右边
                    isFirst=true;
                }

                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                mBluetooth_one.setCompoundDrawables(null, null, drawable, null);//设置图片在文字右边
            }

            if (MID==R.id.StockBluetoothPrinter_two)
            {
                if (isScenned)
                {
                    isFirst=false;
                    len=1;
                    drawable = getResources().getDrawable(R.drawable.setting_check_off);
					/*drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
					mBluetooth_one.setCompoundDrawables(null, null, drawable, null);//设置图片在文字右边*/
                    isScenned=false;

                }else
                {
                    Drawable drawable2=null;
                    len=2;
                    drawable=getResources().getDrawable(R.drawable.setting_check_on);
                    drawable2=getResources().getDrawable(R.drawable.setting_check_off);
                    drawable2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                    mBluetooth_one.setCompoundDrawables(null, null, drawable2, null);//设置图片在文字右边
                    mBluetooth_three.setCompoundDrawables(null, null, drawable2, null);//设置图片在文字右边
                    isScenned=true;
                }

                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                mBluetooth_two.setCompoundDrawables(null, null, drawable, null);//设置图片在文字右边
            }
            if (MID==R.id.StockBluetoothPrinter_three)
            {
                if (isThried)
                {
                    isFirst=false;
                    isScenned=false;
                    len=1;
                    drawable = getResources().getDrawable(R.drawable.setting_check_off);
                    isThried=false;

                }else
                {
                    Drawable drawable3=null;
                    len=3;
                    drawable=getResources().getDrawable(R.drawable.setting_check_on);
                    drawable3=getResources().getDrawable(R.drawable.setting_check_off);
                    drawable3.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                    mBluetooth_one.setCompoundDrawables(null, null, drawable3, null);//设置图片在文字右边
                    mBluetooth_two.setCompoundDrawables(null, null, drawable3, null);//设置图片在文字右边
                    isThried=true;
                }

                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                mBluetooth_three.setCompoundDrawables(null, null, drawable, null);//设置图片在文字右边
            }
            if(MID == R.id.StockBluetoothPrinter_printTickedBtn){

                for (int i = 0; i < len; i++) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    if (goodsList.size()!=0) {
                    mService.write(hexStringToBytes("1B 61 01"));
                    mService.write(hexStringToBytes("1D 21 10"));
                        sendMessage("卸货详情如下\n\n");
                        mService.write(hexStringToBytes("1D 21 00"));
                        mService.write(hexStringToBytes("1B 61 00"));
                        sendMessage("卸货单号:"+serial_numbers+"\n");
                        sendMessage("卸货时间：" + sdf.format(new Date()) + "\n");
                        sendMessage("操作人：" + user.getUser_realname() + "\n");
                        sendMessage("车牌号：" + ladingbillingQuery.getCarnumber() + "\n");
                        for (DisburdenGoods d : goodsList) {
                            sendMessage("--------------------------------\n");
                            sendMessage("产品名称：" + d.getProduct().getName() + "\n");
                            sendMessage("卸货数量：" +d.getDisburden() + "\n");
                        }
                        sendMessage("--------------------------------\n\n");
                    }else {
                        mService.write(hexStringToBytes("1B 61 01"));
                        mService.write(hexStringToBytes("1D 21 10"));
                        sendMessage("余货详情如下\n\n");
                        mService.write(hexStringToBytes("1D 21 00"));
                        mService.write(hexStringToBytes("1B 61 00"));
                        sendMessage("余货时间：" + sdf.format(new Date()) + "\n");
                        sendMessage("送货人：" + user.getUser_realname() + "\n");
                        sendMessage("车牌号：" + ladingbillingQuery.getCarnumber() + "\n");
                        mService.write(hexStringToBytes("1D 21 00"));
                        mService.write(hexStringToBytes("1B 61 00"));
                        for (Product p : productList) {
                            sendMessage("--------------------------------\n");
                            sendMessage("产品名称：" + p.getName() + "\n");
                            sendMessage("余货数量：" + p.getStock() + "\n");
                        }
                    }

                    sendMessage("\n");
                        sendMessage("仓管签名：___________________\n");
                        sendMessage("\n\n\n\n");
                }
                timer = new CountDownTimer(3000, 1000) {
                    //开始计时
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mPrintTicketBtn.setText((millisUntilFinished / 1000) + "秒后跳转到余货查询页");
                        mPrintTicketBtn.setClickable(false);
                        mPrintTicketBtn.setBackgroundColor(Color.GRAY);
                    }

                    //时间加载完调用
                    @Override
                    public void onFinish() {
                        final Intent intent = new Intent(StockBluetoothPrinterActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//设置跳转时A页面的activity处于栈顶
                        startActivity(intent);
                    }
                }.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        try{
            this.unregisterReceiver(mReceiver);
        }catch(Exception e){}
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
    }

    /**
     * Sends a message.
     *
     * @param message
     *            A string of text to send.
     *
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            showToast(R.string.str_unconnected);
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send;
            try {
                send = message.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }
            mService.write(send);
        }
    }

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            for(BtDevice device : mNewDevices){
                                device.setConnected(false);
                            }
                            for(BtDevice device : mPairedDevices){
                                device.setConnected(false);
                            }
                            mPairedDevicesArrayAdapter.notifyDataSetChanged();
                            mNewDevicesArrayAdapter.notifyDataSetChanged();
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    // byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    // String writeMessage = new String(writeBuf);
                    break;
                case BluetoothService.MESSAGE_READ:
                    // byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    // String readMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothService.DEVICE_NAME);
                    showToast("已连接到" + mConnectedDeviceName+" 打印机，请点击打印按钮");
                    mPrintTicketBtn.setEnabled(true);
                    for(BtDevice device : mNewDevices){
                        if(device.getAddress().equals(msg.getData().getString(BluetoothService.DEVICE_ADDRESS))){
                            device.setConnected(true);
                        }else{
                            device.setConnected(false);
                        }
                    }
                    for(BtDevice device : mPairedDevices){
                        if(device.getAddress().equals(msg.getData().getString(BluetoothService.DEVICE_ADDRESS))){
                            device.setConnected(true);
                        }else{
                            device.setConnected(false);
                        }
                    }
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                    break;
                case BluetoothService.MESSAGE_TOAST:
                    showToast(msg.getData().getString(BluetoothService.TOAST));
//				Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothService.TOAST),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        super.showLoading("正在查找蓝牙打印机...");
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            try{
                mBluetoothAdapter.cancelDiscovery();
                // Get the device MAC address, which is the last 17 chars in the
                // View
                TextView deviceAddressText = (TextView) v.findViewById(R.id.device_address);
                String address = (String)deviceAddressText.getText();
                Log.e("连接蓝牙设备", address);
                // Create the result Intent and include the MAC address
                if (BluetoothAdapter.checkBluetoothAddress(address)) {
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mService.stop();
                    mService.connect(device);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevices.add(new BtDevice(device.getName(), device.getAddress(), false));

                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                mNewDevicesArrayAdapter.notifyDataSetChanged();
                hideLoading();
//				if (mNewDevicesArrayAdapter.getCount() == 0) {
//					String noDevices = getResources().getText(R.string.str_btdevice_notfound).toString();
//					mNewDevicesArrayAdapter.add(noDevices);
//				}
            }
        }
    };

    class BluetoothDeviceAdapter extends ArrayAdapter<BtDevice> {
        private int mResourceId;
        public BluetoothDeviceAdapter(Context context, int resourceId,
                                      int textViewResourceId, List<BtDevice> btDevices) {
            super(context, resourceId, textViewResourceId, btDevices);
            this.mResourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BtDevice device = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);

            TextView deviceNameText = (TextView) view.findViewById(R.id.device_name);
            deviceNameText.setText(device.getName());
            TextView deviceAddressText = (TextView) view.findViewById(R.id.device_address);
            deviceAddressText.setText(device.getAddress());
            ImageView bondedImage = (ImageView)view.findViewById(R.id.device_bonded);
            ImageView unbondImage = (ImageView)view.findViewById(R.id.device_unbond);

            if(device.isConnected()){
                unbondImage.setVisibility(View.GONE);
                bondedImage.setVisibility(View.VISIBLE);
            }else{
                unbondImage.setVisibility(View.VISIBLE);
                bondedImage.setVisibility(View.GONE);
            }
            view.setBackgroundResource(R.drawable.my_tab_background);
            return view;
        }
    }

    /**
     * hex String to byte array
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        String[] hexStrings = hexString.split(" ");
        byte[] bytes = new byte[hexStrings.length];
        for (int i = 0; i < hexStrings.length; i++) {
            char[] hexChars = hexStrings[i].toCharArray();
            bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Intent intent = new Intent(StockBluetoothPrinterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//设置跳转时A页面的activity处于栈顶
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
