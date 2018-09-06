package com.moping.dragsample;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * handler进行线程切换测试例子
 */
public class TestThreadTransActivity extends AppCompatActivity {

    private Button thread1Btn;
    private TextView result;


    static class MainThreadHandler extends Handler {

        WeakReference<TestThreadTransActivity> activityWeakReference;

        public MainThreadHandler(TestThreadTransActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (activityWeakReference.get() != null) {
                TestThreadTransActivity activity = activityWeakReference.get();
                activity.result.setText("msg:" + msg.obj + " currentThread:" + Thread.currentThread().getName());
            }
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            Looper.prepare();

            Handler handler = new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i("XXX", "msg:" + msg.obj + " currentThread:" + Thread.currentThread().getName());
                }
            };

            Message message = Message.obtain();
            message.what = 1;
            message.obj = "我来自线程：" + Thread.currentThread().getName();
            handler.sendMessage(message);

            Handler handler2 = new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i("XXX", "msg:" + msg.obj + " currentThread:" + Thread.currentThread().getName());
                }
            };

            Message message2 = Message.obtain();
            message2.what = 2;
            message2.obj = "我来自线程：" + Thread.currentThread().getName() + "  2";
            handler2.sendMessage(message2);
            Looper.loop();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threadtrans);

        thread1Btn = (Button)findViewById(R.id.thread1Btn);
        result = (TextView)findViewById(R.id.result);
        thread1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Thread thread = new Thread(new MyRunnable());
//                thread.setName("子线程");
//                thread.start();

                HandlerThread thread = new HandlerThread("HandlerThread 子线程");
                thread.start();

                Handler handler = new Handler(thread.getLooper()){ // Looper指定了Handler所在的线程
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.i("XXX", "msg:" + msg.obj + " currentThread:" + Thread.currentThread().getName());
                        result.setText((String)msg.obj);  // 只有UI线程创建的Handler才能进行UI更新
                    }
                };

                Log.i("XXX","uiThread1------" + Thread.currentThread());//主线程

                Message message = Message.obtain();
                message.what = 1;
                message.obj = "我来自线程：" + thread.getName();
                handler.sendMessage(message);
            }
        });

    }
}
