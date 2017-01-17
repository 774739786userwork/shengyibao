package com.bangware.shengyibao.customervisits.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Constants_Camera;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.customervisits.adapter.GridViewAdapter;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.CommonUtil;
import com.bangware.shengyibao.utils.OnScrollGridView.CasesImageActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCustomerRecordActivity extends BaseActivity {
    private ImageButton mEd_Visits_Goback;
    private TextView mEd_customer_name,mEd_visit_status,
            mEd_visits_commit,mEd_sound_content,mEd_total_time_textview;
    private TextView mVisitStatus_tv,mBillingStatus_tv;
    private EditText mEd_visit_content,mEd_customer_level_et;
    private Spinner mEd_visit_type_spinner,mEd_issue_spinner,mEd_rate_spinner;
    private LinearLayout mEd_sound_file;
    private ImageView mEd_Visits_image;
    private GridView mEd_caremaView;
    private ArrayList<String> visitTypelist;//拜访类型数据源
    private ArrayList<String> billingTypelist;//出单类型数据源
    private ArrayList<String> customerRatelist;//客户评级数据源
    private int visitTypeNumber,billingTypeNumber,customerLevelNumber;

    public static String[] Visit_TYPE_SELECT = new String[]{"初访", "回访"};
    public static String[] Billing_TYPE_SELECT = new String[]{"未出单", "出单"};
    public static String[] Level_TYPE_SELECT = new String[]{"A级客户", "B级客户", "C级客户"};

    /**业主信息编辑UI**/
    private EditText mEdit_ownerName_et,mEdit_ownerPhone_et,mEdit_acreage_et,mEdit_unit_et;
    private LinearLayout mEdit_ownerNameAndPhone_linearLayout,mEdit_acreageAndPrice_linearLayout;
    private boolean is_owner = false; //判断是否是业主

    private MediaPlayer player;//语音播放器
    private boolean isPlay = false;//播放、暂停状态
    private VisitRecordBean visitRecordBean;
    private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private boolean candelete = false; //是否可以删除照片
    private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
    private String photoFolderAddress = null; //存放照片的文件夹
    private String pztargetPath = null;
    private ArrayList<String> listPhotoNames = null;
    private CaremaAdapter cadapter = null;//拍照完显示的adapter
    private int screenWidth = 0; //屏幕宽度
    private GridViewAdapter edAdapter;

    private User user;
    private String soundContent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_record);

        SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME,MODE_PRIVATE);
        user = AppContext.getInstance().readFromSharedPreferences(sharedPreferences);

        findView();
        setLisenter();
    }

    private void findView() {
        mEd_Visits_Goback= (ImageButton) findViewById(R.id.Ed_Visits_Goback);
        mEd_customer_name = (TextView)findViewById(R.id.Ed_Visits_Customer_Names);
        mEd_visit_content = (EditText)findViewById(R.id.Ed_Visits_et);
        mEd_customer_level_et = (EditText)findViewById(R.id.Ed_Visits_rate);
        mEd_visits_commit= (TextView) findViewById(R.id.Ed_visits_commit);
        mEd_visit_type_spinner = (Spinner) findViewById(R.id.Ed_visit_type_spinner);
        mEd_issue_spinner = (Spinner) findViewById(R.id.Ed_issue_spinner);
        mEd_rate_spinner = (Spinner) findViewById(R.id.Ed_rate_spinner);
        mEd_sound_file = (LinearLayout) findViewById(R.id.Ed_sound_file);
        mEd_sound_content = (TextView) findViewById(R.id.Ed_sound_content);
        mEd_total_time_textview = (TextView) findViewById(R.id.Ed_total_time_textview);
        mEd_Visits_image = (ImageView) findViewById(R.id.Ed_Visits_image);
        mEd_caremaView = (GridView) findViewById(R.id.Ed_caremaView);

        mVisitStatus_tv = (TextView) findViewById(R.id.visitStatus_tv);
        mBillingStatus_tv = (TextView) findViewById(R.id.billingStatus_tv);

        mEdit_ownerNameAndPhone_linearLayout = (LinearLayout) findViewById(R.id.Edit_ownerNameAndPhone_linearLayout);
        mEdit_acreageAndPrice_linearLayout = (LinearLayout) findViewById(R.id.Edit_acreageAndPrice_linearLayout);
        mEdit_ownerName_et = (EditText) findViewById(R.id.edit_ownerName_et);
        mEdit_ownerPhone_et = (EditText) findViewById(R.id.edit_ownerPhone_et);
        mEdit_acreage_et = (EditText) findViewById(R.id.edit_acreage_et);
        mEdit_unit_et = (EditText) findViewById(R.id.edit_unit_et);

        //获取今日拜访记录每个item传过来的值
        visitRecordBean = (VisitRecordBean) getIntent().getSerializableExtra("visitRecordBean");
        mEd_customer_name.setText(visitRecordBean.getCustomer().getName());//店名

        String levelRemark = visitRecordBean.getCustomerLevelRemark();
         mEd_customer_level_et.setText(levelRemark);//客户等级备注

        mVisitStatus_tv.setText(visitRecordBean.getVisitType());
        mBillingStatus_tv.setText(visitRecordBean.getIssueType());

        /**业主信息**/
        if (visitRecordBean.getOwnerBean().is_owner() == true){
            is_owner = true;
            mEdit_ownerNameAndPhone_linearLayout.setVisibility(View.VISIBLE);
            mEdit_ownerName_et.setText(visitRecordBean.getOwnerBean().getOwnerName());
            mEdit_ownerPhone_et.setText(visitRecordBean.getOwnerBean().getOwnerMobile());

            if (visitRecordBean.getOwnerBean().getAcreage() != 0.0 && visitRecordBean.getOwnerBean().getUnitPrice() != 0.0){
                mEdit_acreageAndPrice_linearLayout.setVisibility(View.VISIBLE);
                mEdit_acreage_et.setText(Double.valueOf(visitRecordBean.getOwnerBean().getAcreage()).toString());
                mEdit_unit_et.setText(Double.valueOf(visitRecordBean.getOwnerBean().getUnitPrice()).toString());
            }else {
                mEdit_acreageAndPrice_linearLayout.setVisibility(View.GONE);
            }
        }else {
            is_owner = false;
            mEdit_ownerNameAndPhone_linearLayout.setVisibility(View.GONE);
            mEdit_acreageAndPrice_linearLayout.setVisibility(View.GONE);
        }

        /**语音与拜访总结内容**/
        String content_type = visitRecordBean.getContentType();
        if (content_type.equals("0")){
            mEd_visit_content.setText(visitRecordBean.getVisitContent());
            mEd_sound_file.setVisibility(View.GONE);
            mEd_visit_content.setVisibility(View.VISIBLE);
        }else if (content_type.equals("1")){
            mEd_visit_content.setVisibility(View.GONE);
            mEd_sound_file.setVisibility(View.VISIBLE);
            soundContent = visitRecordBean.getVisitContent();
            mEd_total_time_textview.setText(visitRecordBean.getVisitVoiceTime());
            MyOnTouchListener myTouchlistener = new MyOnTouchListener();
            mEd_sound_content.setOnTouchListener(myTouchlistener);
        }

        setSpinnerSelect();//初始化加载下拉框值

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

        /**从后台获取照片并显示在GridViewAdapter**/
        if(visitRecordBean.getCustomer().getImages().size() > 0){
            edAdapter = new GridViewAdapter(this,visitRecordBean.getCustomer().getImages());
            mEd_caremaView.setAdapter(edAdapter);
            mEd_caremaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(EditCustomerRecordActivity.this,CasesImageActivity.class);
                    intent.putExtra("showImage",(Serializable) visitRecordBean);
                    intent.putExtra(CasesImageActivity.EXTRA_IMAGE_INDEX,position);
                    startActivity(intent);
                }
            });
        }

    }

    //输入框监听改变  监听面积、单价数字小数点
    private void getEditTextData(){
        mEdit_acreage_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEdit_acreage_et.getText().toString().indexOf(".") >= 0) {
                    if (mEdit_acreage_et.getText().toString().indexOf(".", mEdit_acreage_et.getText().toString().indexOf(".") + 1) > 0) {
                        Log.e("TAG", "onTextChanged: 已经输入小数点不能重复输入");
                        mEdit_acreage_et.setText(mEdit_acreage_et.getText().toString().substring(0, mEdit_acreage_et.getText().toString().length() - 1));
                        mEdit_acreage_et.setSelection(mEdit_acreage_et.getText().toString().length());
                    }
                }
            }
        });

        mEdit_unit_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEdit_unit_et.getText().toString().indexOf(".") >= 0) {
                    if (mEdit_unit_et.getText().toString().indexOf(".", mEdit_unit_et.getText().toString().indexOf(".") + 1) > 0) {
                        Log.e("TAG", "onTextChanged: 已经输入小数点不能重复输入");
                        mEdit_unit_et.setText(mEdit_unit_et.getText().toString().substring(0, mEdit_unit_et.getText().toString().length() - 1));
                        mEdit_unit_et.setSelection(mEdit_unit_et.getText().toString().length());
                    }
                }
            }
        });
    }

    /**下拉框值获取**/
    private void setSpinnerSelect(){
        /**拜访类型
        visitTypelist = new ArrayList<String>();
        String visit_status = visitRecordBean.getVisitType();
        if (visit_status.equals(Visit_TYPE_SELECT[0])){
            visitTypelist.add(visit_status);
            visitTypelist.add("回访");
        }else if (visit_status.equals(Visit_TYPE_SELECT[1])){
            visitTypelist.add(visit_status);
            visitTypelist.add("初次拜访");
        }
        ArrayAdapter<String> adapter_visit = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, visitTypelist);
        adapter_visit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉框样式
        mEd_visit_type_spinner.setAdapter(adapter_visit);**/

        /**出单类型
        billingTypelist = new ArrayList<String>();
        String status = visitRecordBean.getIssueType();
        if (status.equals(Billing_TYPE_SELECT[0])){
            billingTypelist.add(status);
            billingTypelist.add("出单");
        }else if (status.equals(Billing_TYPE_SELECT[1])){
            billingTypelist.add(status);
            billingTypelist.add("未出单");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, billingTypelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEd_issue_spinner.setAdapter(adapter);**/

        /**客户评级类型**/
        customerRatelist = new ArrayList<String>();
        String levelStr = visitRecordBean.getCustomerLevel();
        if (levelStr.equals(Level_TYPE_SELECT[0])){
            customerRatelist.add(levelStr);
            customerRatelist.add("B级客户");
            customerRatelist.add("C级客户");
        } else if (levelStr.equals(Level_TYPE_SELECT[1])) {
            customerRatelist.add(levelStr);
            customerRatelist.add("A级客户");
            customerRatelist.add("C级客户");
        }else if (levelStr.equals(Level_TYPE_SELECT[2])) {
            customerRatelist.add(levelStr);
            customerRatelist.add("B级客户");
            customerRatelist.add("A级客户");
        }
        ArrayAdapter<String> rateadapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, customerRatelist);
        rateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEd_rate_spinner.setAdapter(rateadapter);
    }

    /**下拉框选择获取值**/
    private void onSpinnerItemSelect(){
        /**拜访类型下拉选择事件
        mEd_visit_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String visitType = visitTypelist.get(position).toString();
                if (position == 0){
                    if (visitType.equals(Visit_TYPE_SELECT[0])){
                        visitTypeNumber = 0;
                    }else {
                        visitTypeNumber = 1;
                    }
                }else if (position == 1){
                    if (visitType.equals(Visit_TYPE_SELECT[1])){
                        visitTypeNumber = 1;
                    }else {
                        visitTypeNumber = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });**/

        /**出单状态下拉选择事件
        mEd_issue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String issueType = billingTypelist.get(position).toString();
                if (position == 0){
                    if (issueType.equals(Billing_TYPE_SELECT[1])){
                        billingTypeNumber = 1;
                        mEdit_ownerNameAndPhone_linearLayout.setVisibility(View.VISIBLE);
                        mEdit_acreageAndPrice_linearLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        billingTypeNumber = 0;
                        mEdit_ownerNameAndPhone_linearLayout.setVisibility(View.GONE);
                        mEdit_acreageAndPrice_linearLayout.setVisibility(View.GONE);
                    }
                }else if (position == 1){
                    if (issueType.equals(Billing_TYPE_SELECT[0])){
                        billingTypeNumber = 0;
                        mEdit_ownerNameAndPhone_linearLayout.setVisibility(View.GONE);
                        mEdit_acreageAndPrice_linearLayout.setVisibility(View.GONE);
                    }else {
                        billingTypeNumber = 1;
                        mEdit_ownerNameAndPhone_linearLayout.setVisibility(View.VISIBLE);
                        mEdit_acreageAndPrice_linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });**/
        /**客户评级下拉选择事件**/
        mEd_rate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String levelType= customerRatelist.get(position).toString();
                if (position == 0){
                    if (levelType.equals(Level_TYPE_SELECT[0])){
                        customerLevelNumber = 0;
                    }else if (levelType.equals(Level_TYPE_SELECT[1])){
                        customerLevelNumber = 1;
                    }else {
                        customerLevelNumber = 2;
                    }
                }else if (position == 1){
                    if (levelType.equals(Level_TYPE_SELECT[1])){
                        customerLevelNumber = 1;
                    }else if (levelType.equals(Level_TYPE_SELECT[0])){
                        customerLevelNumber = 0;
                    }else {
                        customerLevelNumber = 2;
                    }
                }else if (position == 2){
                    if (levelType.equals(Level_TYPE_SELECT[2])){
                        customerLevelNumber = 2;
                    }else if (levelType.equals(Level_TYPE_SELECT[1])){
                        customerLevelNumber = 1;
                    }else {
                        customerLevelNumber = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**各UI控件的点击事件**/
    private void setLisenter() {
        MyOnClickListener clickListener = new MyOnClickListener();
        mEd_Visits_image.setOnClickListener(clickListener);
        mEd_visits_commit.setOnClickListener(clickListener);
        mEd_Visits_Goback.setOnClickListener(clickListener);

        onSpinnerItemSelect();//下拉框选择事件调用
        getEditTextData();
    }
    /**语音播放触摸事件**/
    private class MyOnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (isPlay == false){
                    mEd_sound_content.setBackgroundResource(R.drawable.chatto_bg_pressed);
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(Model.HTTPURL+visitRecordBean.getVisitContent());
                        player.prepare();
                        player.start();

                        isPlay = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (isPlay == true){
                    try {
                        mEd_sound_content.setBackgroundResource(R.drawable.chatto_bg_pressed);
                        isPlay = false;
                        if (player != null){
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }catch (IllegalStateException ex){
                        ex.printStackTrace();
                    }
                }
            }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                mEd_sound_content.setBackgroundResource(R.drawable.chatto_bg_normal);
            }
            return true;
        }
    }

    /**各UI控件的点击事件**/
    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int mid = view.getId();
            switch (mid){
                //提交响应
                case R.id.Ed_visits_commit:
                    commitEditDataToServer();
                    break;
                //返回键响应
                case R.id.Ed_Visits_Goback:
                    finish();
                    break;
                //拍照键响应
                case R.id.Ed_Visits_image:
                    //申请6.0权限
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkSMSPermission = ContextCompat.checkSelfPermission(EditCustomerRecordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditCustomerRecordActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                            return;
                        } else {
                            getTakePhoto();
                        }
                    } else {
                        getTakePhoto();
                    }
                    break;
            }
        }
    }

    //拍照调用
    private void getTakePhoto(){
        //验证sd卡是否可用
        if(CommonUtil.getSDPath() == null){
            showToast("请安装SD卡");
            return;
        }
        //验证是否超出限制照片数量
        if(listPhotoNames != null && listPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
            showToast("最多只允许拍摄" + Constants_Camera.MAX_PHOTO_SIZE + "张照片。");
            return;
        }
        //调用系统相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(defaultPhotoAddress)));
        startActivityForResult(intent, CRAEMA_REQUEST_CODE);
    }

    //相机拍照回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //拍照
                case CRAEMA_REQUEST_CODE:
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
                        cadapter = new CaremaAdapter(this, screenWidth, listPhotoNames, candelete);
                        mEd_caremaView.setAdapter(cadapter);
                    } else {
                        cadapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    //提交数据到后台
    private boolean commitEditDataToServer(){
        String contentStr = mEd_visit_content.getText().toString();
        String levelRemarkStr = mEd_customer_level_et.getText().toString();
        String visitStr = mVisitStatus_tv.getText().toString();
        String billingStr = mBillingStatus_tv.getText().toString();
        String ownerNameStr = mEdit_ownerName_et.getText().toString();
        String ownerMobileStr = mEdit_ownerPhone_et.getText().toString();
        String ownerAcreageStr = mEdit_acreage_et.getText().toString();
        String ownerPriceStr = mEdit_unit_et.getText().toString();
        //提交数据到后台的接口
        String editUrlToServer = Model.CUSTOMER_VISIT_URL+"/"+visitRecordBean.getVisitId()+".json?token="+ user.getLogin_token();
        try{
            HttpClient httpclient= new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(editUrlToServer);
            MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));
                mulentity.addPart("customer_id", new StringBody(visitRecordBean.getCustomer().getId()));
                int index = (listPhotoNames==null)? 0:listPhotoNames.size();
                for(int i = 0;i <index; i++){
                    File file = new File(listPhotoNames.get(i));
                    FileBody filebody = new FileBody(file);
                    mulentity.addPart("file[]",filebody);
                }

                /**语音及内容**/
                if (!"".equals(soundContent)){
                    mulentity.addPart("content_type", new StringBody("1"));
                }else {
                    mulentity.addPart("content", new StringBody(contentStr, Charset.forName("UTF-8")));
                    mulentity.addPart("content_type", new StringBody("0"));
                }

                /**客户级别备注**/
                if (levelRemarkStr == null){
                    mulentity.addPart("customer_level_remark",new StringBody(""));
                }else {
                    mulentity.addPart("customer_level_remark",new StringBody(levelRemarkStr,Charset.forName("UTF-8")));
                }

                /**出单状态**/
                if (billingStr.equals(Billing_TYPE_SELECT[1])){
                    if (is_owner == true){
                        if (!"".equals(ownerNameStr)){
                            mulentity.addPart("owner_name",new StringBody(ownerNameStr,Charset.forName("UTF-8")));
                        }else {
                            showToast("请填写业主姓名！");
                            return false;
                        }

                        if (!"".equals(ownerMobileStr) && isPhoneNumberValid(ownerMobileStr)){
                            mulentity.addPart("owner_phone",new StringBody(ownerMobileStr));
                        }else {
                            showToast("业主手机号码不能为空或手机号码格式不正确！");
                            return false;
                        }

                        mulentity.addPart("acreage",new StringBody(ownerAcreageStr,Charset.forName("UTF-8")));
                        mulentity.addPart("unit_price",new StringBody(ownerPriceStr,Charset.forName("UTF-8")));
                    }
                    mulentity.addPart("visit_type", new StringBody("1"));
                }else if (billingStr.equals(Billing_TYPE_SELECT[0])){
                    mulentity.addPart("visit_type", new StringBody("0"));
                }

            /**业主信息**/
            if (is_owner == true){
                if (!"".equals(ownerNameStr)){
                    mulentity.addPart("owner_name",new StringBody(ownerNameStr,Charset.forName("UTF-8")));
                }else {
                    showToast("请填写业主姓名！");
                    return false;
                }

                if (!"".equals(ownerMobileStr) && isPhoneNumberValid(ownerMobileStr)){
                    mulentity.addPart("owner_phone",new StringBody(ownerMobileStr));
                }else {
                    showToast("业主手机号码不能为空或手机号码格式不正确！");
                    return false;
                }

                mulentity.addPart("is_owner",new StringBody(Boolean.toString(is_owner)));
                mulentity.addPart("owner_id",new StringBody(visitRecordBean.getOwnerBean().getOwnerId()));
            }

            /**拜访状态**/
            if (visitStr.equals(Visit_TYPE_SELECT[0])){
                mulentity.addPart("visit_status", new StringBody("0"));
            }else {
                mulentity.addPart("visit_status", new StringBody("1"));
            }
                mulentity.addPart("customer_level",new StringBody(String.valueOf(customerLevelNumber)));
                mulentity.addPart("is_owner",new StringBody(Boolean.toString(is_owner)));
                mulentity.addPart("organization_id", new StringBody(user.getOrg_id()));

            httpPut.setEntity(mulentity);
            HttpResponse response = httpclient.execute(httpPut);
            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject objresult = new JSONObject(strResult);
                if (objresult != null) {
                    switch (objresult.getInt("result")) {
                        case 0:
                            showToast(objresult.getString("msg"));
                            String tempStr = "day";
                            Intent intent = new Intent(EditCustomerRecordActivity.this, CustomerVisitRecordActivity.class);
                            Bundle bundle =  new Bundle();
                            bundle.putString("time",tempStr);
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        }catch (Exception e){
            showToast("请求出错！");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlay = false;
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
