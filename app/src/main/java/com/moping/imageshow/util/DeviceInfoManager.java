package com.moping.imageshow.util;


import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 设备信息管理器
 *
 * Created by randysu on 2018/4/9.
 */

public class DeviceInfoManager {

    private static final String TAG = DeviceInfoManager.class.getName();

    private static DeviceInfoManager INSTANCE;

    private Context context;
    private TelephonyManager telephonyManager;

    public static DeviceInfoManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DeviceInfoManager(context);
        }
        return INSTANCE;
    }

    public DeviceInfoManager(Context context) {
        this.context = context;

    }

    public String getDeviceId() {
        telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getDeviceId();
        }
        return null;
    }

    public String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public String getSystemModel() {
        return android.os.Build.MODEL;
    }

    public String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

}
