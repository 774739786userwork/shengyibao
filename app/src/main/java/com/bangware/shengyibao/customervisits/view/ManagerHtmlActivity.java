package com.bangware.shengyibao.customervisits.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.practicalprojects.presenter.impl.QueryPracticalProjectsPresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;

public class ManagerHtmlActivity extends BaseActivity {
    private ImageView back_image;
    private WebView webView_html;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_html);
        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        init();
    }

    private void init(){
        back_image = (ImageView) findViewById(R.id.visit_analysis_imageview);
        webView_html = (WebView) findViewById(R.id.webView_manager);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView_html.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webView_html.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webView_html.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webView_html.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webView_html.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webView_html.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webView_html.getSettings().setUseWideViewPort(true);
        webView_html.getSettings().setDomStorageEnabled(true);
        webView_html.loadUrl(Model.HTTPURL+"customer_visits/customer_visits_count.html?token="+user.getLogin_token());
//        webView_html.loadUrl("file:///android_asset/www/show_detail.html");
    }


}
