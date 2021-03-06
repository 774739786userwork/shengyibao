package com.bangware.shengyibao.net.pickpicture;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Constants_Camera;
import com.bangware.shengyibao.customer.view.ShowImageActivity;
import com.bangware.shengyibao.utils.CommonUtil;
import com.jauker.widget.BadgeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * 客户拜访选择图片
 * Created by bangware on 2016/12/30.
 */

public class VisitsPickPictureActivity extends BaseActivity{
    ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    private BadgeView numberView;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;//6.0权限申请码
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_grid);

        //申请6.0权限
        if (Build.VERSION.SDK_INT >= 23) {
            int checkSMSPermission;
            try {
                checkSMSPermission = ContextCompat.checkSelfPermission(VisitsPickPictureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(VisitsPickPictureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                    return;
                }else {
                    initData();
                }
            } catch (RuntimeException e) {
                showTipsDialog();
                return;
            }
        } else {
            initData();
        }
        initGallery();
    }

    //处理权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initData();
                } else {
                    // Permission Denied
                    showTipsDialog();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //调用系统相册
    private void initData() {
        this.imageUrls = new ArrayList<String>();
        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = this
                .getApplicationContext()
                .getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                        null, null, orderBy + " DESC");
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            Log.e("imageUrl", imageUrls.get(i));
        }
    }

    private void initGallery() {
        numberView = new BadgeView(this);
        numberView.setTargetView(findViewById(R.id.number_text));
        numberView.setTextSize(16);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.no_pic_300)
                .showImageForEmptyUri(R.drawable.no_pic_300)
                .cacheInMemory().cacheOnDisc().build();

        imageAdapter = new ImageAdapter(this, imageUrls);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);
        // gridView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // startImageGalleryActivity(position);
        // }
        // });
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    //选择完成
    public void btnChoosePhotosClick(View v) {
        loadingdialog.show();
        String pickPath = "";
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        if (selectedItems.size() > Constants_Camera.MAX_PHOTO_SIZE){
            showConfirmDialog();
            loadingdialog.dismiss();
        }else {
            for (int i = 0;i<selectedItems.size();i++){
                pickPath = selectedItems.get(i);
                CommonUtil.getImage(pickPath);
            }
            loadingdialog.dismiss();
            Intent intent = new Intent();
            intent.putStringArrayListExtra("imageList", selectedItems);
            setResult(1200,intent);
            finish();
        }
    }

    public void btnCancelClick(View v){
        finish();
    }

    private void showConfirmDialog()
    {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("最多只能选择三张照片！");
        builer.setCancelable(false);
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builer.show();
    }

    /**
     * Description GridView Adapter
     */
    public class ImageAdapter extends BaseAdapter {
        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;
        public ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<String>();
            this.mList = imageList;
        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }

            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_multiphoto_item,
                        null);
            }

            CheckBox mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);

            imageLoader.displayImage("file://" + imageUrls.get(position),
                    imageView, options, new SimpleImageLoadingListener() {
                        public void onLoadingComplete(Bitmap loadedImage) {
                            Animation anim = AnimationUtils.loadAnimation(
                                    VisitsPickPictureActivity.this, R.anim.fade_in);
                            imageView.setAnimation(anim);
                            anim.start();
                        }
                    });

            //点击查看详细
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent(VisitsPickPictureActivity.this,ShowImageActivity.class);
                    intent.putStringArrayListExtra("ImageUrl",mList);
                    intent.putExtra(ShowImageActivity.EXTRA_IMAGE_INDEX,position);
                    VisitsPickPictureActivity.this.startActivity(intent);
                }
            });
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            return convertView;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub

                    mSparseBooleanArray.put((Integer) buttonView.getTag(),
                            isChecked);
                /*int k = 0;
                for (int p = 0; p < mSparseBooleanArray.size();p++){
                    if (isChecked){
                        k ++;
                        numberView.setBadgeCount(k);
                    }else {
                        int m = mSparseBooleanArray.size();
                        m-= 1;
                        numberView.setBadgeCount(m);
                        Log.e("TAG", "递减------------>"+m);
                    }
                }*/
            }
        };
    }
}
