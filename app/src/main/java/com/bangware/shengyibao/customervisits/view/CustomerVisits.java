package com.bangware.shengyibao.customervisits.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.bangware.shengyibao.activity.CustomProgressDialog;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Constants_Camera;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.BillingCaremaAdapter;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.customer.adapter.MyContactAdapter;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customer.view.AddCustomerActivity;
import com.bangware.shengyibao.customercontacts.CustomerContactAdapter;
import com.bangware.shengyibao.customercontacts.presenter.CustomerContactsPresenter;
import com.bangware.shengyibao.customercontacts.presenter.impl.CustomerContactsPresenterImpl;
import com.bangware.shengyibao.customercontacts.view.CustomerContactsView;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.presenter.CustomerVisitStatusPresenter;
import com.bangware.shengyibao.customervisits.presenter.impl.CustomerVisitStatusPresenterImpl;
import com.bangware.shengyibao.deliverynote.view.DeliveryNoteDetailActivity;
import com.bangware.shengyibao.main.view.MainActivity;
import com.bangware.shengyibao.net.pickpicture.BillingPickPictureActivity;
import com.bangware.shengyibao.net.pickpicture.BillingPickPictureAdapter;
import com.bangware.shengyibao.net.pickpicture.PickPictureActivity;
import com.bangware.shengyibao.net.pickpicture.PickPictureAdapter;
import com.bangware.shengyibao.net.pickpicture.VisitsPickPictureActivity;
import com.bangware.shengyibao.user.model.entity.User;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomerVisits extends BaseActivity implements CustomerContactsView,CustomerVisitStatusView{
    private ImageButton mVisits_Goback;//返回按钮
    private TextView mVisits_Customer_Names,mVisits_commit;//选择拜访的客户名称，完成按钮
    private ClearEditText mVisits_edits;//查询客户输入框
    private ImageView mVisitslist_textviews;//调取通讯录按钮
    private TextView mVisits_query_btns,mCustomer_level_img_tv;//点击查询按钮  填写客户级别备注点击控件
    private ListView mVisits_query_ListView;//查询客户的结果显示列表
    private EditText mVisits_et,mCustomer_level_et;//拜访总结输入框
    private ImageView mVisits_image;//拍照按钮并显示
    private TextView mVisits_speechvoice;//录音按钮
    private LinearLayout mWrite_remark_lLayout,mBilling_photo_lLayout;
    private RelativeLayout mRemark_rllayout,mRelative_visit_summary,mbilling_caremaRelLayout;//级别备注与拜访总结
    private boolean isShrink = false;
    private String username,usernumber;//获取通讯录名字以及电话号码
    private Spinner visit_status_spinner,issue_spinner;//拜访类型与出单类型下拉框
    private Spinner rate_spinner;//客户分级下拉框
    private String billingStr = "";//获取出单选择类型
//    private String rateStr="";//获取客户分级等级
    private int intType;//拜访状态码
    private int intBilling;//出单状态码
    private int rateType;//客户分级状态码
    private VisitRecordBean statusBean = new VisitRecordBean();
    public static String[] Visit_TYPE_SELECT = new String[]{"初访", "回访"};
    public static String[] Level_TYPE_SELECT = new String[]{"A级客户", "B级客户", "C级客户"};
    private CustomerVisitStatusPresenter statusPresenter;
    private User user;

    /**业主信息UI**/
    private LinearLayout mIsOwner_linearLayout,mOwnerNameAndPhone_linearLayout,mAcreageAndPrice_linearLayout;
    private CheckBox mIsOwnerCheckBox;
    private EditText mOwnerName_et,mOwnerPhone_et,mAcreage_et,mUnit_et;
    private boolean is_owner = false; //判断是否是业主

    //图片上传常量定义
    private Context context;
    private GridView caremaView,mbilling_caremaView; //照片显示区域
    private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int BILLING_CRAEMA_REQUEST_CODE = 2; //出单时拍照请求码
    private static final int MY_PERMISSIONS_REQUEST_BILLING_CAMERA = 3;//出单时拍照权限请求码
    private boolean candelete = false; //是否可以删除照片
    private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
    private String photoFolderAddress = null; //存放照片的文件夹
    private String pztargetPath = null;
    private ArrayList<String> listPhotoNames = null;
    private CaremaAdapter cadapter = null;
    private BillingCaremaAdapter billingCaremaAdapter = null;
    private ArrayList<String> billingPhotoNames = null;
    private int screenWidth = 0; //屏幕宽度
    private PickPictureAdapter pictureAdapter;
    private BillingPickPictureAdapter billingPickPictureAdapter;
    private FrameLayout mframeLayout;

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
    private RelativeLayout addCustomerRelLayout;
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
        addCustomerRelLayout.setOnClickListener(OnclickListener);
        mWrite_remark_lLayout.setOnClickListener(OnclickListener);
        mBilling_photo_lLayout.setOnClickListener(OnclickListener);

        //出单类型下拉框获取选中植
        issue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    //选择未出单 复选框取消选中并释放禁止事件
                    mIsOwnerCheckBox.setChecked(false);
                    mIsOwnerCheckBox.setClickable(true);
                    mAcreageAndPrice_linearLayout.setVisibility(View.GONE);//面积、单价控件的隐藏显示
                    mBilling_photo_lLayout.setVisibility(View.GONE);//出单拍摄合同照控件
                    mbilling_caremaRelLayout.setVisibility(View.GONE);
                    billingStr = CustomerVisits.this.getResources().getStringArray(R.array.issue)[position];
                    intBilling=position;
                }else {
                    //选择出单 复选框默认选中并禁止取消选中
                    getEditTextData();//初始化输入框监听方法
                    mIsOwnerCheckBox.setChecked(true);
                    mIsOwnerCheckBox.setClickable(false);
                    mAcreageAndPrice_linearLayout.setVisibility(View.GONE);//面积、单价控件的隐藏显示
                    mBilling_photo_lLayout.setVisibility(View.VISIBLE);//出单拍摄合同照控件
                    mbilling_caremaRelLayout.setVisibility(View.VISIBLE);//合同照显示区域
                    billingStr = CustomerVisits.this.getResources().getStringArray(R.array.issue)[position];
                    intBilling=position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //给复选框加选择事件
        mIsOwnerCheckBox.setOnCheckedChangeListener(changeListener);
    }

        //是否是业主 复选框选择
        private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.getId() == R.id.isChoiceCheckBox){
                    if (isChecked){
                        mOwnerNameAndPhone_linearLayout.setVisibility(View.VISIBLE);
                        is_owner = true;
                    }else {
                        mOwnerNameAndPhone_linearLayout.setVisibility(View.GONE);
                        is_owner = false;
                    }
                }
            }
        };

        //输入框监听改变  监听面积、单价数字小数点
        private void getEditTextData(){
            mAcreage_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mAcreage_et.getText().toString().indexOf(".") >= 0) {
                        if (mAcreage_et.getText().toString().indexOf(".", mAcreage_et.getText().toString().indexOf(".") + 1) > 0) {
                            Log.e("TAG", "onTextChanged: 已经输入小数点不能重复输入");
                            mAcreage_et.setText(mAcreage_et.getText().toString().substring(0, mAcreage_et.getText().toString().length() - 1));
                            mAcreage_et.setSelection(mAcreage_et.getText().toString().length());
                        }
                    }
                }
            });

            mUnit_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mUnit_et.getText().toString().indexOf(".") >= 0) {
                        if (mUnit_et.getText().toString().indexOf(".", mUnit_et.getText().toString().indexOf(".") + 1) > 0) {
                            Log.e("TAG", "onTextChanged: 已经输入小数点不能重复输入");
                            mUnit_et.setText(mUnit_et.getText().toString().substring(0, mUnit_et.getText().toString().length() - 1));
                            mUnit_et.setSelection(mUnit_et.getText().toString().length());
                        }
                    }
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
        mRelative_visit_summary= (RelativeLayout) findViewById(R.id.relative_visit_summary);
        mCustomer_level_et= (EditText) findViewById(R.id.Visits_rate_remark);
        mCustomer_level_img_tv= (TextView) findViewById(R.id.customer_level_img_tv);
        mVisits_image= (ImageView) findViewById(R.id.Visits_image);
        mVisits_speechvoice = (TextView) findViewById(R.id.Visits_speechvoice);
        visit_status_spinner = (Spinner) findViewById(R.id.visit_status_spinner);
        issue_spinner = (Spinner) findViewById(R.id.issue_spinner);
        rate_spinner= (Spinner) findViewById(R.id.rate_spinner);
        caremaView = (GridView)findViewById(R.id.caremaView);
        mbilling_caremaView = (GridView)findViewById(R.id.billing_caremaView);
        sound_file = (LinearLayout) findViewById(R.id.sound_file);
        tv_chatcontent= (TextView) findViewById(R.id.tv_chatcontent);
        total_time_textview= (TextView) findViewById(R.id.total_time_textview);
        delete_image = (ImageView) findViewById(R.id.deleteimage);
        addCustomerRelLayout= (RelativeLayout) findViewById(R.id.addCustomerRelLayout);
        mWrite_remark_lLayout = (LinearLayout) findViewById(R.id.write_remark_lLayout);
        mBilling_photo_lLayout = (LinearLayout) findViewById(R.id.billing_photo_lLayout);
        mRemark_rllayout = (RelativeLayout) findViewById(R.id.remark_rllayout);
        mbilling_caremaRelLayout = (RelativeLayout) findViewById(R.id.billing_caremaRelLayout);
        mframeLayout = (FrameLayout) findViewById(R.id.VisitframeLayout);

        /**业主信息**/
        mOwnerNameAndPhone_linearLayout = (LinearLayout) findViewById(R.id.ownerNameAndPhone_linearLayout);
        mAcreageAndPrice_linearLayout = (LinearLayout) findViewById(R.id.acreageAndPrice_linearLayout);
        mIsOwner_linearLayout = (LinearLayout) findViewById(R.id.isOwner_linearLayout);
        mIsOwnerCheckBox = (CheckBox) findViewById(R.id.isChoiceCheckBox);
        mOwnerName_et = (EditText) findViewById(R.id.ownerName_et);
        mOwnerPhone_et = (EditText) findViewById(R.id.ownerPhone_et);
        mAcreage_et = (EditText) findViewById(R.id.acreage_et);
        mUnit_et = (EditText) findViewById(R.id.unit_et);

        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user=AppContext.getInstance().readFromSharedPreferences(sharedPreferences);

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

        //加载拜访状态
        statusPresenter = new CustomerVisitStatusPresenterImpl(this);

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
                //获取客户ID与业务员绑定用来判断拜访状态
                statusPresenter.queryVisitStatus(user,contact.getCustomer().getId());
            }
        });
    }

    //从后台获取拜访状态
    @Override
    public void addCustomeVisitStatus(VisitRecordBean visitStatus) {
        if (visitStatus != null){
            this.statusBean = visitStatus;
            getSpinnerData();
            mIsOwner_linearLayout.setVisibility(View.VISIBLE);
        }else {
            showToast("获取拜访状态为异常！");
        }
    }

    @Override
    public void loadDataFailure(String failureMessage) {

    }

    /**后台获取拜访类型与客户等级状态数据填充到下拉框中**/
    private void getSpinnerData(){
        //拜访类型状态
        final ArrayList<String> visitlist = new ArrayList<String>();
        if (statusBean.getVisitType().equals("0")){
            intType = 0;
            visitlist.add(Visit_TYPE_SELECT[0]);
            visitlist.add(Visit_TYPE_SELECT[1]);
        }else if (statusBean.getVisitType().equals("1")){
            intType = 1;
            visitlist.add(Visit_TYPE_SELECT[1]);
            visitlist.add(Visit_TYPE_SELECT[0]);
        }
        ArrayAdapter<String> adapterVisit = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, visitlist);
        adapterVisit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉框样式
        visit_status_spinner.setAdapter(adapterVisit);

        //客户等级类型状态
        final ArrayList<String> levellist = new ArrayList<String>();
        if (statusBean.getCustomerLevel().equals("0")){
            rateType = 0;
            levellist.add(Level_TYPE_SELECT[0]);
            levellist.add(Level_TYPE_SELECT[1]);
            levellist.add(Level_TYPE_SELECT[2]);
        }else if (statusBean.getCustomerLevel().equals("1")){
            rateType = 1;
            levellist.add(Level_TYPE_SELECT[1]);
            levellist.add(Level_TYPE_SELECT[0]);
            levellist.add(Level_TYPE_SELECT[2]);
        }else  if (statusBean.getCustomerLevel().equals("2")){
            rateType = 2;
            levellist.add(Level_TYPE_SELECT[2]);
            levellist.add(Level_TYPE_SELECT[1]);
            levellist.add(Level_TYPE_SELECT[0]);
        }

        ArrayAdapter<String> adapterLevel = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, levellist);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉框样式
        rate_spinner.setAdapter(adapterLevel);

        //拜访类型下拉框选择值
        visit_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String visitStatus = visitlist.get(position).toString();
                if (position == 0){
                    if (visitStatus.equals(Visit_TYPE_SELECT[0])){
                        intType = 0;
                    }else {
                        intType = 1;
                    }
                }else if (position == 1){
                    if (visitStatus.equals(Visit_TYPE_SELECT[1])){
                        intType = 1;
                    }else {
                        intType = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //客户级别类型下拉框选择值
        rate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String rateStr = levellist.get(position).toString();
                if (position == 0){
                    if (rateStr.equals(Level_TYPE_SELECT[0])){
                        rateType = 0;
                    }else if (rateStr.equals(Level_TYPE_SELECT[1])){
                        rateType = 1;
                    }else {
                        rateType = 2;
                    }
                }else if (position == 1){
                    if (rateStr.equals(Level_TYPE_SELECT[1])){
                        rateType = 1;
                    }else if (rateStr.equals(Level_TYPE_SELECT[0])){
                        rateType = 0;
                    }else {
                        rateType = 2;
                    }
                }else if (position == 2){
                    if (rateStr.equals(Level_TYPE_SELECT[2])){
                        rateType = 2;
                    }else if (rateStr.equals(Level_TYPE_SELECT[1])){
                        rateType = 1;
                    }else {
                        rateType = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                //拍照或从相册选择上传
                case R.id.Visits_image:
                    showTakePhotoAndPictureDialog();
                    break;
                //模糊查询
                case R.id.Visits_query_btns:
                    mVisits_query_ListView.setVisibility(View.VISIBLE);
                    customerContactsQuickQuery();
                    break;
                //录制语音
                case R.id.Visits_speechvoice:
                    Intent intent = new Intent(CustomerVisits.this,SpeechActivity.class);
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
                    mRelative_visit_summary.setVisibility(View.VISIBLE);
                    break;
                //出单上传合同照
                case R.id.billing_photo_lLayout:
                    billingTakePhotoAndPictureDialog();
                    break;
                //客户等级备注输入框
                case R.id.write_remark_lLayout:
                    if (isShrink == false){
                        Drawable drawable = getResources().getDrawable(R.drawable.images_down);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        mCustomer_level_img_tv.setCompoundDrawables(null, null, drawable,
                                null);
                        mRemark_rllayout.setVisibility(View.VISIBLE);
                        isShrink = true;
                    }else if (isShrink == true){
                        Drawable drawable = getResources().getDrawable(R.drawable.arrow_right);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        mCustomer_level_img_tv.setCompoundDrawables(null, null, drawable,
                                null);
                        mRemark_rllayout.setVisibility(View.GONE);
                        isShrink = false;
                    }
                    break;
                //完成提交请求数据到后台
                case R.id.visits_commit:
                    loadingdialog.show();
                    commitDataToServer();
                    break;
                //新增客户
                case R.id.addCustomerRelLayout:
                    Intent intent_add = new Intent(CustomerVisits.this,AddCustomerActivity.class);
                    startActivity(intent_add);
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
        //调用系统相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(defaultPhotoAddress)));
        startActivityForResult(intent, CRAEMA_REQUEST_CODE);
    }

    //拍照调用
    private void getBillingtakePhoto(){
        //验证sd卡是否可用
        if(CommonUtil.getSDPath() == null){
            showToast("请安装SD卡");
            return;
        }
        if(billingPhotoNames != null && billingPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
            showToast("最多只允许拍摄" + Constants_Camera.MAX_PHOTO_SIZE + "张照片");
            return;
        }
        //调用系统相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(defaultPhotoAddress)));
        startActivityForResult(intent, BILLING_CRAEMA_REQUEST_CODE);
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
                    showTipsDialog();
                }
                break;
            case MY_PERMISSIONS_REQUEST_BILLING_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getBillingtakePhoto();
                } else {
                    showTipsDialog();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**拍照和从相册中选择图片**/
    private void showTakePhotoAndPictureDialog(){
        int srceenW =  ((BaseActivity)this).getWindowManager().getDefaultDisplay().getWidth();
        //联系人对话框
        customDialog = new CommonDialog(CustomerVisits.this,srceenW, R.layout.takephoto_andfromalum_dialog_layout,R.style.custom_dialog);
        TextView tv_dialog_login_context = (TextView)customDialog.findViewById(R.id.tv_dialog_common_context);
        TextView tv_dialog_take_photo = (TextView)customDialog.findViewById(R.id.tv_dialog_common_takephoto);
        TextView tv_dialog_from_alum = (TextView)customDialog.findViewById(R.id.tv_dialog_common_fromAlum);
        TextView tv_dialog_record_video = (TextView)customDialog.findViewById(R.id.tv_dialog_common_recordVideo);
        tv_dialog_record_video.setVisibility(View.GONE);
        TextView tv_dialog_login_close = (TextView)customDialog.findViewById(R.id.tv_dialog_common_close);
        tv_dialog_login_context.setText("请选择！");
        tv_dialog_take_photo.setText("拍照");
        tv_dialog_from_alum.setText("从相册中选取");
        customDialog.setCanceledOnTouchOutside(false);
        tv_dialog_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请6.0权限
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkSMSPermission;
                    try {
                        checkSMSPermission = ContextCompat.checkSelfPermission(CustomerVisits.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CustomerVisits.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                            return;
                        }else {
                            takePhoto();
                        }
                    } catch (RuntimeException e) {
                        showTipsDialog();
                        return;
                    }
                } else {
                    takePhoto();
                }
                customDialog.dismiss();
            }
        });
        tv_dialog_from_alum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_pick = new Intent(CustomerVisits.this, VisitsPickPictureActivity.class);
                startActivityForResult(intent_pick, 1100);
                customDialog.dismiss();
            }
        });
        tv_dialog_login_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    /**出单时拍照和从相册中选择图片**/
    private void billingTakePhotoAndPictureDialog(){
        int srceenW =  ((BaseActivity)this).getWindowManager().getDefaultDisplay().getWidth();
        //联系人对话框
        customDialog = new CommonDialog(CustomerVisits.this,srceenW, R.layout.takephoto_andfromalum_dialog_layout,R.style.custom_dialog);
        TextView tv_dialog_login_context = (TextView)customDialog.findViewById(R.id.tv_dialog_common_context);
        TextView tv_dialog_take_photo = (TextView)customDialog.findViewById(R.id.tv_dialog_common_takephoto);
        TextView tv_dialog_from_alum = (TextView)customDialog.findViewById(R.id.tv_dialog_common_fromAlum);
        TextView tv_dialog_login_close = (TextView)customDialog.findViewById(R.id.tv_dialog_common_close);
        tv_dialog_login_context.setText("请选择！");
        tv_dialog_take_photo.setText("拍照");
        tv_dialog_from_alum.setText("从相册中选取");
        customDialog.setCanceledOnTouchOutside(false);
        tv_dialog_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请6.0权限
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkSMSPermission;
                    try {
                        checkSMSPermission = ContextCompat.checkSelfPermission(CustomerVisits.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CustomerVisits.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_BILLING_CAMERA);
                            return;
                        }else {
                            getBillingtakePhoto();
                        }
                    } catch (RuntimeException e) {
                        showTipsDialog();
                        return;
                    }
                } else {
                    getBillingtakePhoto();
                }
                customDialog.dismiss();
            }
        });
        tv_dialog_from_alum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(billingPhotoNames != null && billingPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
                    showToast("最多只允许上传" + Constants_Camera.MAX_PHOTO_SIZE + "张照片");
                    return;
                }else {
                    Intent intent_pick = new Intent(CustomerVisits.this, BillingPickPictureActivity.class);
                    startActivityForResult(intent_pick, 100);
                }
                customDialog.dismiss();
            }
        });
        tv_dialog_login_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                //调取通讯录回传值
                case PHONE_CONTACT_CODE:
                    try {
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
                                                    mVisits_query_ListView.setVisibility(View.GONE);
                                                    mVisits_Customer_Names.setText("请选择拜访客户");
                                                    contact = null;
                                                    customDialog.dismiss();
                                                }
                                            });
                                            
                                        } else if (mycontactlist.size() == 1) {
                                            mycontactlist.clear();
                                            mVisits_query_ListView.setVisibility(View.GONE);
                                            mVisits_Customer_Names.setText("请选择拜访客户");
                                            contact = null;
                                            mVisits_edits.setText(usernumber);
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e)
                    {
                        showTipsDialog();
                    }
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

                        //隐藏拍照按钮
                        if(listPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
                            mframeLayout.setVisibility(View.GONE);
                        }else {
                            mframeLayout.setVisibility(View.VISIBLE);
                        }

                        if (cadapter == null) {
                            cadapter = new CaremaAdapter(context, screenWidth, listPhotoNames, candelete);
                            caremaView.setAdapter(cadapter);
                        } else {
                            cadapter.notifyDataSetChanged();
                        }
                    }
                    break;
                //出单时拍照获取图片回传值
                case BILLING_CRAEMA_REQUEST_CODE:
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

                        if (billingPhotoNames == null){
                            billingPhotoNames = new ArrayList<String>();
                        }
                        billingPhotoNames.add(pztargetPath);

                        if (billingCaremaAdapter == null){
                            billingCaremaAdapter = new BillingCaremaAdapter(context,screenWidth,billingPhotoNames,candelete);
                            mbilling_caremaView.setAdapter(billingCaremaAdapter);
                        }else {
                            billingCaremaAdapter.notifyDataSetChanged();
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
            // 回传图片文件
            if (requestCode == 1100 && resultCode == 1200){
                listPhotoNames= data.getStringArrayListExtra("imageList");
                //当图片数量为三张时隐藏拍照按钮
                if(listPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
                    mframeLayout.setVisibility(View.GONE);
                }else {
                    mframeLayout.setVisibility(View.VISIBLE);
                }
                pictureAdapter = new PickPictureAdapter(this, listPhotoNames, candelete);
                caremaView.setAdapter(pictureAdapter);
                pictureAdapter.notifyDataSetChanged();
                cadapter = null;
            }
            //出单时回传图片文件
            if (requestCode == 100 && resultCode == 110){
                billingPhotoNames= data.getStringArrayListExtra("imageList");
                billingPickPictureAdapter = new BillingPickPictureAdapter(this, billingPhotoNames, candelete);
                mbilling_caremaView.setAdapter(billingPickPictureAdapter);
                billingPickPictureAdapter.notifyDataSetChanged();
            }
    }

    private void soundUse(String fileName){
        //判断sd卡上是否有声音文件，有的话就显示名称并播放
        voicePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/shengyibao_voice/"+fileName;
        File file=new File(voicePath);
        if(file.exists()){
            sound_file.setVisibility(View.VISIBLE);
            mRelative_visit_summary.setVisibility(View.GONE);
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
            mRelative_visit_summary.setVisibility(View.VISIBLE);
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
        if (mVisits_edits.length()==8||mVisits_edits.length()>=11||text.length()==7){
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(text);
            if (m.matches()) {
                phone = text;
                customerlist.clear();
                nPage = 1;
                contactName = "";
                CCPresenter.loadCustomerContacts(user, nPage, nSpage, phone, contactName,"");
                mVisits_Customer_Names.setText("请选择拜访客户");
                contact = null;
            }

            p = Pattern.compile("[\u4e00-\u9fa5]*");
            m = p.matcher(text);
            if (m.matches()) {
                contactName = text;
                customerlist.clear();
                nPage = 1;
                phone = "";
                CCPresenter.loadCustomerContacts(user, nPage, nSpage, "", contactName,"");
            }
        }else
        {
            mVisits_query_ListView.setVisibility(View.GONE);
            showToast("号码格式不正确，请重新输入！");
        }
    }

    //提交数据到后台
    private boolean commitDataToServer(){
        String visitContent = mVisits_et.getText().toString();
        String customerLevelRemark = mCustomer_level_et.getText().toString();
        String speedtime=total_time_textview.getText().toString();
        String ownerName = mOwnerName_et.getText().toString();
        String ownerPhone = mOwnerPhone_et.getText().toString();
        String acreage = mAcreage_et.getText().toString();
        String unit = mUnit_et.getText().toString();
        //提交数据到后台的接口
        String actionUrl =Model.CUSTOMER_VISIT_URL+ "?token="+ user.getLogin_token();
        try {
            HttpClient httpclient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(actionUrl);
            MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));

            if (contact != null){
                mulentity.addPart("customer_id", new StringBody(contact.getCustomer().getId()));

                int index = (listPhotoNames==null)? 0:listPhotoNames.size();
                if (index > 0){
                    for(int i = 0;i <index; i++){
                        File file = new File(listPhotoNames.get(i));
                        FileBody filebody = new FileBody(file);
                        mulentity.addPart("file[]",filebody);
                    }
                }else {
                    loadingdialog.dismiss();
                    showToast("请上传签到图片！");
                    return false;
                }

                File file_voice = new File(voicePath);
                if (file_voice.exists()) {
                    FileBody fileStr = new FileBody(file_voice);
                    mulentity.addPart("audio", fileStr);
                    mulentity.addPart("duration",new StringBody(speedtime));
                    mulentity.addPart("content_type",new StringBody(String.valueOf(1)));
                } else {
                    if ("".equals(visitContent)) {
                        loadingdialog.dismiss();
                        showToast("请输入拜访总结或者录制拜访语音");
                        return false;
                    }else
                    {
                        mulentity.addPart("content", new StringBody(visitContent, Charset.forName("UTF-8")));
                        mulentity.addPart("content_type",new StringBody(String.valueOf(0)));
                    }
                }

                if (!String.valueOf(longitude).isEmpty() && !String.valueOf(latitude).isEmpty()){
                    mulentity.addPart("lng", new StringBody(Double.toString(longitude)));
                    mulentity.addPart("lat", new StringBody(Double.toString(latitude)));
                }else {
                    loadingdialog.dismiss();
                    showToast("网络状况不佳！请稍后重试");
                    return false;
                }

                if (customerLevelRemark == null){
                    mulentity.addPart("customer_level_remark",new StringBody(""));
                }else {
                    mulentity.addPart("customer_level_remark",new StringBody(customerLevelRemark,Charset.forName("UTF-8")));
                }
                mulentity.addPart("customer_level",new StringBody(String.valueOf(rateType)));
                mulentity.addPart("visit_status", new StringBody(String.valueOf(intType)));
                mulentity.addPart("visit_type", new StringBody(String.valueOf(intBilling)));

                if (billingStr.equals(CustomerVisits.this.getResources().getStringArray(R.array.issue)[1])){
                    if (is_owner == true){
                        if (!"".equals(ownerName)){
                            mulentity.addPart("owner_name",new StringBody(ownerName,Charset.forName("UTF-8")));
                        }else {
                            loadingdialog.dismiss();
                            showToast("请填写业主姓名！");
                            return false;
                        }

                        if (!"".equals(ownerPhone) && isPhoneNumberValid(ownerPhone)){
                            mulentity.addPart("owner_phone",new StringBody(ownerPhone));
                        }else {
                            loadingdialog.dismiss();
                            showToast("业主手机号码不能为空或手机号码格式不正确！");
                            return false;
                        }

                         mulentity.addPart("acreage",new StringBody(acreage,Charset.forName("UTF-8")));
                         mulentity.addPart("unit_price",new StringBody(unit,Charset.forName("UTF-8")));
                    }

                    int billingPhoto = (billingPhotoNames==null)? 0:billingPhotoNames.size();
                    if (billingPhoto > 0){
                        for (int i = 0; i < billingPhoto; i++){
                            File file = new File(billingPhotoNames.get(i));
                            FileBody filebody = new FileBody(file);
                            mulentity.addPart("file[]",filebody);
                        }
                    }else {
                        loadingdialog.dismiss();
                        showToast("请至少上传一张合同照！");
                        return false;
                    }
                }

                if (is_owner == true){
                    if (!"".equals(ownerName)){
                        mulentity.addPart("owner_name",new StringBody(ownerName,Charset.forName("UTF-8")));
                    }else {
                        loadingdialog.dismiss();
                        showToast("请填写业主姓名！");
                        return false;
                    }

                    if (!"".equals(ownerPhone) && isPhoneNumberValid(ownerPhone)){
                        mulentity.addPart("owner_phone",new StringBody(ownerPhone));
                    }else {
                        loadingdialog.dismiss();
                        showToast("业主手机号码不能为空或手机号码格式不正确！");
                        return false;
                    }

                    mulentity.addPart("is_owner",new StringBody(Boolean.toString(is_owner)));
                }

                mulentity.addPart("is_owner",new StringBody(Boolean.toString(is_owner)));
                mulentity.addPart("employee_id", new StringBody(user.getEmployee_id()));
                mulentity.addPart("organization_id", new StringBody(user.getOrg_id()));

                httpPost.setEntity(mulentity);
                HttpResponse response = httpclient.execute(httpPost);
                if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    String strResult = EntityUtils.toString(response.getEntity());
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
                    loadingdialog.dismiss();
                    showToast("返回内容为空！");
                }
            }else {
                loadingdialog.dismiss();
                showToast("你还没有选择拜访客户！");
            }
        } catch (Exception e) {
            loadingdialog.dismiss();
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
        loadingdialog.dismiss();
        // 退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        if (CCPresenter!=null)
        {
            CCPresenter.onDestroy();
            loadingdialog.dismiss();
        }
        if (statusPresenter != null){
            statusPresenter.destory();
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

    /*
     * 验证号码 手机号 固话
     *
     */
    public boolean isPhoneNumberValid(String phoneNumber) {
        String expression = "((^(13|15|18|17|14)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            return true;
        }else{
            return false;
        }
    }
}
