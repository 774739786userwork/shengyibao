package com.bangware.shengyibao.customervisits.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Constants_Camera;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.customer.adapter.MyContactAdapter;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customercontacts.CustomerContactAdapter;
import com.bangware.shengyibao.customercontacts.presenter.CustomerContactsPresenter;
import com.bangware.shengyibao.customercontacts.presenter.impl.CustomerContactsPresenterImpl;
import com.bangware.shengyibao.customercontacts.view.CustomerContactsView;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.ClearEditText;
import com.bangware.shengyibao.utils.CommonUtil;
import com.bangware.shengyibao.utils.customdialog.CommonDialog;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomerVisits extends BaseActivity implements CustomerContactsView{
    private ImageButton mVisits_Goback;//返回按钮
    private TextView mVisits_Customer_Names,mVisits_commit,issue_and_unIssue_type;//选择拜访的客户名称，完成按钮
    private ClearEditText mVisits_edits;//查询客户输入框
    private ImageView mVisitslist_textviews;//调取通讯录按钮
    private TextView mVisits_query_btns;//点击查询按钮
    private ListView mVisits_query_ListView;//查询客户的结果显示列表
    private EditText mVisits_et;//拜访总结输入框
    private ImageView mVisits_image;//拍照按钮并显示
    private TextView mVisits_speechvoice;//录音按钮
    private String username,usernumber;//获取通讯录名字以及电话号码
    private Spinner type_spinner,issue_spinner;//拜访类型与出单类型下拉框
    private String typeStr = "";//获取拜访选择类型
    private String billingStr = "";//获取出单选择类型
    private int intType;//拜访状态码
    private String visitContent;
    private int intBilling;//出单状态码

    //图片上传常量定义
    private Context context;
    private GridView caremaView; 					  //照片显示区域
    private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private boolean candelete = false; //是否可以删除照片
    private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
    private String photoFolderAddress = null; //存放照片的文件夹
    private String pztargetPath = null;
    private List<String> listPhotoNames = null;
    private CaremaAdapter cadapter = null;
    private int screenWidth = 0; //屏幕宽度

    private static final int PHONE_CONTACT_CODE = 1;//调取通讯录请求码
    private List<Contacts> customerlist = new ArrayList<Contacts>();//客户联系人列表
    private CustomerContactAdapter adapter;//客户联系人适配器
    private List<String> mycontactlist=new ArrayList<String>();
    private MyContactAdapter myContactAdapter;
    private CommonDialog customDialog;
    private Contacts contact;
    private CustomerContactsPresenter CCPresenter;//客户联系人Presenter
    private String phone="";
    private String contactName="";
    private int nPage=1;
    private int nSpage=10;
    private String text="";

    private LinearLayout sound_file;
    private TextView tv_chatcontent,total_time_textview;
    private ImageView delete_image;
    private boolean check = false;
    private MediaPlayer player;//多媒体音频文件
    private String voicePath = "";//音频文件保存路径

    private LocationClient mLocClient;
    private MyLocationData locData;
    public CustomerVisits.MyLocationListenner myListener = new CustomerVisits.MyLocationListenner();
    private double  longitude,latitude;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true; // 是否首次定位
    //提交数据到后台的接口
    private String actionUrl =Model.CUSTOMER_VISIT_URL+ "?token="+ AppContext.getInstance().getUser().getLogin_token();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_visits);
        context = this;
        findView();//初始化控件
        setOnClick();//点击事件初始化
        myContactAdapter=new MyContactAdapter(this,mycontactlist);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
        mLocClient.setLocOption(option);
        mLocClient.start();//	调用此方法开始定位

    }

    private void setOnClick() {
       MyOnclickListener OnclickListener=new MyOnclickListener();
        mVisits_Goback.setOnClickListener(OnclickListener);
        mVisitslist_textviews.setOnClickListener(OnclickListener);
        mVisits_commit.setOnClickListener(OnclickListener);
        mVisits_image.setOnClickListener(OnclickListener);
        mVisits_query_btns.setOnClickListener(OnclickListener);
        mVisits_speechvoice.setOnClickListener(OnclickListener);
        delete_image.setOnClickListener(OnclickListener);
        //拜访下拉框获取选中植
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long arg3) {
                typeStr = CustomerVisits.this.getResources().getStringArray(R.array.visits)[position];
                intType=position;
                Log.e("intType",intType+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //出单类型下拉框获取选中植pp
        issue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    billingStr = CustomerVisits.this.getResources().getStringArray(R.array.issue)[position];
                    issue_and_unIssue_type.setVisibility(View.GONE);
                    intBilling=position;
                }else {
                    billingStr = CustomerVisits.this.getResources().getStringArray(R.array.issue)[position];
                    issue_and_unIssue_type.setVisibility(View.VISIBLE);
                    intBilling=position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void findView() {
        mMapView= (MapView) findViewById(R.id.bmapView_visits);
        mVisits_Goback= (ImageButton) findViewById(R.id.Visits_Goback);
        mVisits_Customer_Names= (TextView) findViewById(R.id.Visits_Customer_Names);
        mVisits_commit= (TextView) findViewById(R.id.visits_commit);
        mVisits_edits= (ClearEditText) findViewById(R.id.Visits_edits);
        mVisitslist_textviews= (ImageView) findViewById(R.id.Visitslist_textviews);
        mVisits_query_btns= (TextView) findViewById(R.id.Visits_query_btns);
        mVisits_query_ListView= (ListView) findViewById(R.id.Visits_query_ListView);
        mVisits_et= (EditText) findViewById(R.id.Visits_et);
        mVisits_image= (ImageView) findViewById(R.id.Visits_image);
        mVisits_speechvoice= (TextView) findViewById(R.id.Visits_speechvoice);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        issue_spinner = (Spinner) findViewById(R.id.issue_spinner);
        caremaView = (GridView)findViewById(R.id.caremaView);
        sound_file = (LinearLayout) findViewById(R.id.sound_file);
        tv_chatcontent= (TextView) findViewById(R.id.tv_chatcontent);
        total_time_textview= (TextView) findViewById(R.id.total_time_textview);
        delete_image = (ImageView) findViewById(R.id.deleteimage);
        issue_and_unIssue_type= (TextView) findViewById(R.id.issue_and_unIssue_type);

        //图片默认地址
        defaultPhotoAddress = CommonUtil.getSDPath() + File.separator + "default.jpg";
        //获取屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;

        //是否可删除照片
        candelete = getIntent().getBooleanExtra("candelete", true);

        //获取文件夹名称
        if(getIntent().getStringExtra("folderName") == null){
            photoFolderAddress = CommonUtil.getSDPath() + File.separator + "TestPhotoFolder";
        }else{
            photoFolderAddress = getIntent().getStringExtra("folderName");
        }

        //加载客户联系人Presenter
        CCPresenter=new CustomerContactsPresenterImpl(this);
        adapter = new CustomerContactAdapter(this, customerlist);
        mVisits_query_ListView.setAdapter(adapter);
        mVisits_query_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                //用Bundle传递数据到联系人详情界面
                contact = (Contacts)adapterView.getItemAtPosition(position);
                mVisits_Customer_Names.setText(contact.getCustomer().getName());
            }
        });
    }

    private class MyOnclickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.Visits_Goback:
                    finish();
                    break;

                //调取通讯录
                case R.id.Visitslist_textviews:
                    startActivityForResult(new Intent(
                            Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PHONE_CONTACT_CODE);
                    break;
                //拍照上传
                case R.id.Visits_image:
                    //申请6.0权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkSMSPermission = ContextCompat.checkSelfPermission(CustomerVisits.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CustomerVisits.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                            return;
                        } else {
                            takePhoto();
                        }
                    } else {
                        takePhoto();
                    }
                    break;
                //模糊查询
                case R.id.Visits_query_btns:
                    mVisits_query_ListView.setVisibility(View.VISIBLE);
                    customerContactsQuickQuery();
                    break;
                //录制语音
                case R.id.Visits_speechvoice:
                    Intent intent = new Intent(CustomerVisits.this,SpeechActivity.class);
                    intent.putExtra("speechContent", tv_chatcontent.getText().toString());
                    intent.putExtra("speechTime", total_time_textview.getText().toString());
                    startActivityForResult(intent, 1000);
                    break;
                //删除语音文件
                case R.id.deleteimage:
                    if(player!=null){
                        player.stop();
                        player.release();
                        player=null;
                    }
                    CommonUtil.deleteFile(voicePath);
                    voicePath = "";
                    sound_file.setVisibility(View.GONE);
                    mVisits_et.setVisibility(View.VISIBLE);
                    break;
                //完成提交请求数据到后台
                case R.id.visits_commit:
                    visitContent = mVisits_et.getText().toString();
                    if (TextUtils.isEmpty(visitContent) && voicePath == "") {
                        showToast("请输入拜访总结或者录制拜访语音");
                    }else
                    {
                        commitDataToServer();
                    }
                    break;
            }
        }
    }

    //拍照调用
    private void takePhoto(){
        //验证sd卡是否可用
        if(CommonUtil.getSDPath() == null){
            showToast("请安装SD卡");
            return;
        }
        //验证是否超出限制照片数量
        if(listPhotoNames != null && listPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
            showToast("只允许拍摄三张照片！");
            return;
        }
        //调用系统相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(defaultPhotoAddress)));
        startActivityForResult(intent, CRAEMA_REQUEST_CODE);
    }

    //处理权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    takePhoto();
                } else {
                    // Permission Denied
                    showToast("用户取消了权限");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                //调取通讯录回传值
                case PHONE_CONTACT_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        ContentResolver reContentResolverol = getContentResolver();
                        Uri contactData = data.getData();
                        @SuppressWarnings("deprecation")
                        Cursor cursor = managedQuery(contactData, null, null, null, null);
                        cursor.moveToFirst();
                        //获得DATA表中的名字
                        username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        //条件为联系人ID
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                        Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                null,
                                null);
                        if (phone != null) {
                            String phoneNumber = "";
                            while (phone.moveToNext()) {
                                if (phone.moveToFirst()) {
                                    do {
                                        //遍历所有的联系人下面所有的电话号码
                                        phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        //使用Toast技术显示获得的
                                        Log.e("phone", phoneNumber);
                                        // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
                                        usernumber = phoneNumber;
                                        usernumber = usernumber.replaceAll("^(\\+86)", "");
                                        usernumber = usernumber.replaceAll("^(86)", "");
                                        usernumber = usernumber.replaceAll("-", "");
                                        usernumber = usernumber.replaceAll(" ", "");
                                        usernumber = usernumber.trim();
                                        mycontactlist.add(usernumber);

                                    } while (phone.moveToNext());
                                    if (mycontactlist.size() > 1) {
                                        int screenView = CustomerVisits.this.getWindowManager().getDefaultDisplay().getWidth();
                                        customDialog = new CommonDialog(CustomerVisits.this, screenView, R.layout.show_mycontact_dialog_layout, R.style.custom_dialog);
                                        customDialog.setCanceledOnTouchOutside(false);
                                        final ListView my_contact_list = (ListView) customDialog.findViewById(R.id.my_contact_listview);
                                        TextView my_contact_close = (TextView) customDialog.findViewById(R.id.my_contact_close);
                                        my_contact_list.setAdapter(myContactAdapter);
                                        customDialog.show();
                                        my_contact_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mycontactlist.clear();
                                                customDialog.dismiss();
                                            }
                                        });
                                        my_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                usernumber = mycontactlist.get(i);
                                                mVisits_edits.setText(usernumber);
                                                mycontactlist.clear();
                                                customDialog.dismiss();
                                            }
                                        });
                                    } else if (mycontactlist.size() == 1) {
                                        mycontactlist.clear();
                                        mVisits_edits.setText(usernumber);
                                    }
                                }
                            }
                        }
                    }
				/*if (!phone.isClosed()){
					phone.close();
				}*/
                break;
                //拍照获取图片回传值
                case CRAEMA_REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        //文件夹目录是否存在
                        File folderAddr = new File(photoFolderAddress);
                        if (!folderAddr.exists() || !folderAddr.isDirectory()) {
                            folderAddr.mkdirs();
                        }
                        //将原图片压缩拷贝到指定目录
                        pztargetPath = photoFolderAddress + File.separator + CommonUtil.getUUID32() + ".jpg";

                        CommonUtil.dealImage(defaultPhotoAddress, pztargetPath);
                        //删除原图
                        new File(defaultPhotoAddress).delete();
                        //保存照片的绝对路径
                        if (listPhotoNames == null) {
                            listPhotoNames = new ArrayList<String>();
                        }
                        listPhotoNames.add(pztargetPath);

                        if (cadapter == null) {
                            cadapter = new CaremaAdapter(context, screenWidth, listPhotoNames, candelete);
                            caremaView.setAdapter(cadapter);
                        } else {
                            cadapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
            //回传语音文件
            if(requestCode == 1000 && resultCode == 1001)
            {
                String speech_content = data.getStringExtra("speechContent");
                String speech_time = data.getStringExtra("speechTime");
                total_time_textview.setText(speech_time);
                soundUse(speech_content);
            }
    }

    private void soundUse(String fileName){
        //判断sd卡上是否有声音文件，有的话就显示名称并播放
        voicePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/shengyibao_voice/"+fileName;
        File file=new File(voicePath);
        if(file.exists()){
            sound_file.setVisibility(View.VISIBLE);
            mVisits_et.setVisibility(View.GONE);
            //点击声音文件播放声音
            tv_chatcontent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        if (check == false){//若check为false，则表示此时语音的状态为正在播放
                            tv_chatcontent.setBackgroundResource(R.drawable.chatto_bg_pressed);
                            player= new MediaPlayer();
                            try {
                                player.setDataSource(voicePath);
                                player.prepare();
                                player.start();
                                check = true;
                            } catch (IllegalArgumentException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }else if(check == true){//若check为true，则表示此时语音的状态为暂停播放
                            tv_chatcontent.setBackgroundResource(R.drawable.chatto_bg_pressed);
                            check = false;
                            if(player!=null){
                                player.stop();
                                player.release();
                                player=null;
                            }
                        }
                    }else if(event.getAction()==MotionEvent.ACTION_UP){
                        tv_chatcontent.setBackgroundResource(R.drawable.chatto_bg_normal);
                    }
                    return true;
                }
            });
        }else {
            mVisits_et.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 输入框快捷查询操作
     */
    public void customerContactsQuickQuery(){
        text = mVisits_edits.getText().toString();
        if(TextUtils.isEmpty(text)){
            showToast("查询条件不能为空！");
            mVisits_query_ListView.setVisibility(View.GONE);
            return;
        }
        if (mVisits_edits.length()==8||mVisits_edits.length()>=11){
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(text);
            if (m.matches()) {
                phone = text;
                customerlist.clear();
                nPage = 1;
                contactName = "";
                CCPresenter.loadCustomerContacts(AppContext.getInstance().getUser(), nPage, nSpage, phone, contactName,"");
            }

            p = Pattern.compile("[\u4e00-\u9fa5]*");
            m = p.matcher(text);
            if (m.matches()) {
                contactName = text;
                customerlist.clear();
                nPage = 1;
                phone = "";
                CCPresenter.loadCustomerContacts(AppContext.getInstance().getUser(), nPage, nSpage, "", contactName,"");
            }
        }else
        {
            mVisits_query_ListView.setVisibility(View.GONE);
            showToast("号码格式不正确，请重新输入！");
        }
    }

    //提交数据到后台
    private boolean commitDataToServer(){

        String speedtime=total_time_textview.getText().toString();
        try {
            HttpClient httpclient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(actionUrl);
            MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));

            if (contact != null){
                mulentity.addPart("customer_id", new StringBody(contact.getCustomer().getId()));

                int index = (listPhotoNames==null)? 0:listPhotoNames.size();
                if (billingStr.equals(CustomerVisits.this.getResources().getStringArray(R.array.issue)[1])){
                    if (index > 0){
                        for(int i = 0;i <index; i++){
                            File file = new File(listPhotoNames.get(i));
                            FileBody filebody = new FileBody(file);
                            mulentity.addPart("file[]",filebody);
                        }
                    }else {
                        showToast("请至少拍摄一张合同照！");
                        return false;
                    }
                }else {
                    for(int i = 0;i <index; i++){
                        File file = new File(listPhotoNames.get(i));
                        FileBody filebody = new FileBody(file);
                        mulentity.addPart("file[]",filebody);
                    }
                }

                File file_voice = new File(voicePath);
                if (file_voice.exists()) {
                    FileBody fileStr = new FileBody(file_voice);
                    mulentity.addPart("audio", fileStr);
                    mulentity.addPart("duration",new StringBody(speedtime));
                    mulentity.addPart("content_type",new StringBody(String.valueOf(1)));
                } else {
                    mulentity.addPart("content", new StringBody(visitContent, Charset.forName("UTF-8")));
                    mulentity.addPart("content_type",new StringBody(String.valueOf(0)));
                }

                if (!String.valueOf(longitude).isEmpty() && !String.valueOf(latitude).isEmpty()){
                    mulentity.addPart("lng", new StringBody(Double.toString(longitude)));
                    mulentity.addPart("lat", new StringBody(Double.toString(latitude)));
                }else {
                    showToast("网络状况不佳！请稍后重试");
                    return false;
                }

                mulentity.addPart("visit_status", new StringBody(String.valueOf(intType),Charset.forName("UTF-8")));
                mulentity.addPart("visit_type", new StringBody(String.valueOf(intBilling),Charset.forName("UTF-8")));

                mulentity.addPart("employee_id", new StringBody(AppContext.getInstance().getUser().getEmployee_id()));
                mulentity.addPart("organization_id", new StringBody(AppContext.getInstance().getUser().getOrg_id()));

                httpPost.setEntity(mulentity);
                HttpResponse response = httpclient.execute(httpPost);
                if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    String strResult = EntityUtils.toString(response.getEntity());
                    finish();
                    JSONObject objresult = new JSONObject(strResult);
                    if (objresult != null) {
                        switch (objresult.getInt("result")) {
                            case 0:
                                showToast(objresult.getString("msg"));
                                String tempStr = "day";
                                Intent intent = new Intent(CustomerVisits.this, CustomerVisitRecordActivity.class);
                                Bundle bundle =  new Bundle();
                                bundle.putString("time",tempStr);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                showToast(objresult.getString("msg"));
                                break;
                            case 2:
                                showToast(objresult.getString("msg"));
                                break;
                        }
                    }
                }else {
                    showToast("返回内容为空！");
                }
            }else {
                showToast("你还没有选择拜访客户！");
            }
        } catch (Exception e) {
            showToast("请求出错！");
            e.printStackTrace();
        }
        return true;
    }


    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            longitude=locData.longitude;
            latitude=locData.latitude;
            Log.e("经纬度",longitude+" "+latitude);
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
    /**
     * 加载客户查询数据
     * @param Contacts
     */
    @Override
    public void doCustomerContactsLoadSuccess(List<Contacts> Contacts) {
        if(Contacts.size() > 0){
            customerlist.addAll(Contacts);
            adapter.notifyDataSetChanged();
        }else{
            mVisits_query_ListView.setVisibility(View.GONE);
            showToast("暂无客户数据！");
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        if (CCPresenter!=null)
        {
            CCPresenter.onDestroy();
            loadingdialog.dismiss();
        }
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(player!=null){
            player.stop();
            player.release();
            player=null;
        }
    }
}
