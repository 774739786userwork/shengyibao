package com.bangware.shengyibao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class HtmlWebMainActivity extends BaseActivity {
	private ImageView wechat_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webmain);
		
		wechat_back = (ImageView) findViewById(R.id.wechat_back);
		wechat_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});


	}

}
