package com.bangware.shengyibao.practicalprojects.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.utils.ClearEditText;

public class FuzzyQueryPractical extends AppCompatActivity {
    private ImageView pra_back;
    private ClearEditText pra_edit;
    private TextView pra_query_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy_query_practical);
        findView();
        setListener();
    }

    private void setListener() {
        pra_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
         pra_query_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent();
                 intent.putExtra("content", pra_edit.getText().toString());
                 setResult(900,intent);
                 finish();
             }
         });
    }

    private void findView() {
        pra_query_btn= (TextView) findViewById(R.id.pra_query_btn);
        pra_back= (ImageView) findViewById(R.id.pra_back);
        pra_edit= (ClearEditText) findViewById(R.id.pra_edit);
    }
}
