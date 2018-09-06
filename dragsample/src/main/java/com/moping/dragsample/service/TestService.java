package com.moping.dragsample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class TestService extends Service {

    private Thread thread;
    private static int count;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(new MyRunnable());
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class MyRunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 99999999; i++) {
                Log.i("XXX", "count:" + count);
                count++;
            }
        }
    }
}
