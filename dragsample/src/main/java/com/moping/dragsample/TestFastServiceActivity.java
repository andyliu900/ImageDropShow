package com.moping.dragsample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.moping.dragsample.service.TestService;

public class TestFastServiceActivity extends AppCompatActivity {

    private int count = 0;
    private TextView num;
    private Intent intent;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            count++;
            num.setText(count + "");

            handler.sendEmptyMessageDelayed(1, 200);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfastservice);

        num = (TextView)findViewById(R.id.num);

        handler.sendEmptyMessage(1);

        intent = new Intent(this, TestService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
