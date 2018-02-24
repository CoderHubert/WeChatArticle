package com.hubert.wechatarticle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.hubert.wechatarticle.R;
import com.hubert.wechatarticle.service.ArticleService;

public class MainActivity extends AppCompatActivity {

    private CheckBox accessibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        accessibility = findViewById(R.id.checkBox);
        accessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initStatus(){
        if(accessibility != null){
            if (ArticleService.isServiceRunning(this, "com.hubert.wechatarticle.ArticleService")) {
                accessibility.setChecked(true);
            } else {
                accessibility.setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        initStatus();
        super.onResume();
    }
}
