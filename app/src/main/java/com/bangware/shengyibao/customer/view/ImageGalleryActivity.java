package com.bangware.shengyibao.customer.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.ImageGalleryAdapter;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bumptech.glide.Glide;

/**
 * 图片画廊展示
 */
public class ImageGalleryActivity extends BaseActivity {
    private Customer customer = null;
    private ImageGalleryAdapter galleryAdapter = null;
    private ImageView imageView;
    private Gallery gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        init();
        initData();
    }

    private void init(){
        customer = (Customer)getIntent().getExtras().getSerializable("customer");
        imageView = (ImageView) findViewById(R.id.imageview_gallery);
        gallery = (Gallery) findViewById(R.id.gallery);
    }

    private void initData(){
        imageView.setImageResource(R.drawable.no_pic_300);
        if(customer.getImages().size() > 0){
            String customer_imgae_url = customer.getImages().get(0).getImg_url();
            if(customer_imgae_url!=null && !"".equals(customer_imgae_url)){
                Glide.with(getApplicationContext()).load(Model.HTTPURL+ customer_imgae_url).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
                        .into(imageView);
            }
        }

        galleryAdapter = new ImageGalleryAdapter(customer.getImages(),this);
        gallery.setAdapter(galleryAdapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //显示点击的是第几张图片
                CustomerImage customerImage = customer.getImages().get(position);
                //设置背景部分的ImageView显示当前Item的图片
                Glide.with(ImageGalleryActivity.this).load(Model.HTTPURL+customerImage.getImg_url()).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
                        .into(imageView);
            }
        });
    }
}
