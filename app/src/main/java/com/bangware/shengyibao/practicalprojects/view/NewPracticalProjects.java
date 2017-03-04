package com.bangware.shengyibao.practicalprojects.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Constants_Camera;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.net.pickpicture.PickPictureActivity;
import com.bangware.shengyibao.net.pickpicture.PickPictureAdapter;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.CommonUtil;
import com.bangware.shengyibao.utils.customdialog.CommonDialog;
import com.bangware.shengyibao.utils.videoRecord.FFmpegRecorderActivity;
import com.bangware.shengyibao.utils.videoRecord.VideoUtils;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NewPracticalProjects extends BaseActivity implements MediaPlayer.OnCompletionListener {
    private ImageButton new_practicalprojects_Goback;
    private TextView new_practicalprojects_commit;
    private EditText new_practicalprojects_edit;
    private FrameLayout practicalprojects_frameLayout;
    private GridView practicalprojects_caremaView;
    private User user;

    private Context context;
    private ArrayList<String> listPhotoNames = null;
    private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
    private String photoFolderAddress = null; //存放照片的文件夹
    private String pztargetPath = null;
    private CaremaAdapter cadapter = null;
    private int screenWidth = 0; //屏幕宽度
    private boolean candelete = false; //是否可以删除照片
    private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private CommonDialog customDialog;
    private PickPictureAdapter pictureAdapter;

    /**
     * 视频参数
     */
    private String path = "";
    private TextureView surfaceView;
    private MediaPlayer mediaPlayer;
    private ImageView imagePlay;
    private RelativeLayout preview_video_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_new_practical_projects);

        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);

        findView();
        setLisenter();
    }
    private void findView() {
        new_practicalprojects_edit= (EditText) findViewById(R.id.new_practicalprojects_edit);
        practicalprojects_frameLayout= (FrameLayout) findViewById(R.id.practicalprojects_frameLayout);
        practicalprojects_caremaView= (GridView) findViewById(R.id.practicalprojects_caremaView);
        new_practicalprojects_Goback= (ImageButton) findViewById(R.id.new_practicalprojects_Goback);
        new_practicalprojects_commit= (TextView) findViewById(R.id.new_practicalprojects_commit);
        surfaceView = (TextureView) findViewById(R.id.preview_video);
        imagePlay = (ImageView) findViewById(R.id.previre_play);
        preview_video_parent = (RelativeLayout)findViewById(R.id.preview_video_parent);
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

        /**
         *
         */
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) preview_video_parent.getLayoutParams();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.widthPixels;
        preview_video_parent.setLayoutParams(layoutParams);

        mediaPlayer = new MediaPlayer();
    }

    private void setLisenter() {
        new_practicalprojects_Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new_practicalprojects_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingdialog.show();
              commitDataToServer();
            }
        });

        //拍照等功能操作点击事件
        practicalprojects_frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTakePhotoAndPictureDialog();
            }
        });

        //暂停视频播放点击事件
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imagePlay.setVisibility(View.VISIBLE);
                }
            }
        });

        //点击播放按钮图片实现视频播放点击事件
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                imagePlay.setVisibility(View.GONE);
            }
        });

        MySurfaceTextureListener textureListener = new MySurfaceTextureListener();
        surfaceView.setSurfaceTextureListener(textureListener);
        mediaPlayer.setOnCompletionListener(this);
        surfaceView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builer = new AlertDialog.Builder(NewPracticalProjects.this);
                builer.setTitle("确定要删除本视频吗？");
                builer.setCancelable(false);
                builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                        VideoUtils.deleteTempVideo(NewPracticalProjects.this);
                        path = "";
                        practicalprojects_frameLayout.setVisibility(View.VISIBLE);
                        preview_video_parent.setVisibility(View.GONE);
                    }
                });
                builer.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                builer.show();
                return true;
            }
        });
    }

    private class MySurfaceTextureListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
                                              int arg2) {
            prepare(new Surface(arg0));
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    }

    String video_time = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            //拍照回传图片
            case  CRAEMA_REQUEST_CODE:
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

                    if(listPhotoNames.size() >= Constants_Camera.MAX_PRACTICAL){
                        practicalprojects_frameLayout.setVisibility(View.GONE);
                    }else {
                        practicalprojects_frameLayout.setVisibility(View.VISIBLE);
                    }

                    if (cadapter == null) {
                        preview_video_parent.setVisibility(View.GONE);
                        practicalprojects_caremaView.setVisibility(View.VISIBLE);
                        cadapter = new CaremaAdapter(context, screenWidth, listPhotoNames, candelete);
                        practicalprojects_caremaView.setAdapter(cadapter);
                    } else {
                        cadapter.notifyDataSetChanged();
                    }
                }
                break;

        }
        // 回传图片文件
        if (requestCode == 1100 && resultCode == 1200){
            preview_video_parent.setVisibility(View.GONE);
            practicalprojects_caremaView.setVisibility(View.VISIBLE);
            listPhotoNames= data.getStringArrayListExtra("imageList");
            if(listPhotoNames.size() >= Constants_Camera.MAX_PRACTICAL){
                practicalprojects_frameLayout.setVisibility(View.GONE);
            }else {
                practicalprojects_frameLayout.setVisibility(View.VISIBLE);
            }
            pictureAdapter = new PickPictureAdapter(this, listPhotoNames, candelete);
            practicalprojects_caremaView.setAdapter(pictureAdapter);
            pictureAdapter.notifyDataSetChanged();
            cadapter = null;
        }
        //回传视频文件
        if (requestCode == 1300 && resultCode == 1400){
            preview_video_parent.setVisibility(View.VISIBLE);
            practicalprojects_caremaView.setVisibility(View.GONE);
            path = data.getStringExtra("path");
            video_time = data.getStringExtra("videoTime");
            File videoFile = new File(path);
            if (videoFile.exists()){
                practicalprojects_frameLayout.setVisibility(View.GONE);
            }else {
                practicalprojects_frameLayout.setVisibility(View.VISIBLE);
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
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**拍照、从相册中选择图片,拍摄小视频**/
    private void showTakePhotoAndPictureDialog(){
        int srceenW =  ((BaseActivity)this).getWindowManager().getDefaultDisplay().getWidth();
        //联系人对话框
        customDialog = new CommonDialog(NewPracticalProjects.this,srceenW, R.layout.takephoto_andfromalum_dialog_layout,R.style.custom_dialog);
        TextView tv_dialog_login_context = (TextView)customDialog.findViewById(R.id.tv_dialog_common_context);
        TextView tv_dialog_take_photo = (TextView)customDialog.findViewById(R.id.tv_dialog_common_takephoto);
        TextView tv_dialog_from_alum = (TextView)customDialog.findViewById(R.id.tv_dialog_common_fromAlum);
        TextView tv_dialog_record_video = (TextView) customDialog.findViewById(R.id.tv_dialog_common_recordVideo);
        TextView tv_dialog_login_close = (TextView)customDialog.findViewById(R.id.tv_dialog_common_close);
        tv_dialog_login_context.setText("请选择！");
        tv_dialog_take_photo.setText("拍照");
        tv_dialog_from_alum.setText("从相册中选取");
        tv_dialog_record_video.setText("小视频");
        customDialog.setCanceledOnTouchOutside(false);
        tv_dialog_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请6.0权限
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkSMSPermission;
                    try {
                        checkSMSPermission = ContextCompat.checkSelfPermission(NewPracticalProjects.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NewPracticalProjects.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
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
                Intent intent_pick = new Intent(NewPracticalProjects.this, PickPictureActivity.class);
                startActivityForResult(intent_pick, 1100);
                customDialog.dismiss();
            }
        });
        tv_dialog_record_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(NewPracticalProjects.this, FFmpegRecorderActivity.class);
                startActivityForResult(videoIntent,1300);
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
    private boolean commitDataToServer()
    {
        String rank=new_practicalprojects_edit.getText().toString();
        //提交数据到后台的接口
        String actionUrl = Model.NEW_PRACTICAL_PROJECT_URL+ "?token="+ user.getLogin_token();
        HttpClient httpclient= new DefaultHttpClient();
        HttpPost httpPost= new HttpPost(actionUrl);
        MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));
        try {
            if (!"".equals(rank))
            {
                mulentity.addPart("content",new StringBody(rank,Charset.forName("UTF-8")));
            }else
            {
                showToast("请输入案例描述");
                loadingdialog.dismiss();
                return false;
            }
            int index = (listPhotoNames==null)? 0:listPhotoNames.size();
            if (index > 0){
                for(int i = 0;i <index; i++){
                    File file = new File(listPhotoNames.get(i));
                    FileBody filebody = new FileBody(file);
                    mulentity.addPart("file[]",filebody);
                }
            }else {
                if (!"".equals(path)){
                    File getVideoFile = new File(path);
                    if (getVideoFile.exists()){
                        FileBody fileVideo = new FileBody(getVideoFile);
                        mulentity.addPart("video", fileVideo);
                        mulentity.addPart("duration",new StringBody(video_time));
                    }
                }else {
                    showToast("请上传图片或拍摄小视频！");
                    loadingdialog.dismiss();
                    return false;
                }
            }
            mulentity.addPart("employee_id",new StringBody(user.getEmployee_id()));
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
                            Intent intent = new Intent(NewPracticalProjects.this, QueryPracticalProjects.class);
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
                loadingdialog.dismiss();
            }
        } catch (Exception e) {
            showToast("请求出错");

            loadingdialog.dismiss();
            e.printStackTrace();
        }
        return true;
    }

    public void prepare(Surface surface) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            Log.e("tag", "prepare:播放 "+path);
            mediaPlayer.setDataSource(path);
            // 把视频画面输出到Surface
            mediaPlayer.setSurface(surface);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingdialog.dismiss();
    }

    private void stop(){
        mediaPlayer.stop();
        finish();
    }

    @Override
    public void onBackPressed() {
        stop();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        imagePlay.setVisibility(View.VISIBLE);
    }

}
