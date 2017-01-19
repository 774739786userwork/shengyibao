package com.bangware.shengyibao.ladingbilling.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.bangware.shengyibao.activity.R;

public class QueryDisburdenActivity extends AppCompatActivity {

    private ImageButton querydisburn_Goback;
    private ListView querydisburn_ListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_disburden);
        findView();
        setLisenter();
    }

    private void findView() {
        querydisburn_Goback= (ImageButton) findViewById(R.id.querydisburn_Goback);
        querydisburn_ListView= (ListView) findViewById(R.id.querydisburn_ListView);
    }
    private void setLisenter() {
        querydisburn_Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryDisburdenActivity.this.finish();
            }
        });
    }

}
