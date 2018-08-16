package com.moping.imageshow;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.moping.imageshow.base.BaseDialogFragment;
import com.moping.imageshow.base.BaseFragment;
import com.moping.imageshow.func.FunctionManager;
import com.moping.imageshow.func.FunctionNoParamNoResult;
import com.moping.imageshow.func.FunctionWithParamOnly;
import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.DeviceInfoManager;
import com.moping.imageshow.util.SharedPreferencesUtils;
import com.moping.imageshow.util.TimeUtil;

public class MainActivity extends AppCompatActivity {

    private FolderIndexFragment folderIndexFragment;
    private ImageShowContentFragment imageShowContentFragment;

    private FrameLayout imageShowContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageShowContentLayout = (FrameLayout) findViewById(R.id.imageShowContentFragment);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        folderIndexFragment = new FolderIndexFragment();
        imageShowContentFragment = new ImageShowContentFragment();

        ft.add(R.id.folderIndexFragment, folderIndexFragment, "FolderIndexFragment");
        ft.add(R.id.imageShowContentFragment, imageShowContentFragment, "ImageShowContentFragment");

        ft.commit();

//        checkCanUseByUsageTime();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 添加接口并实现接口中的方法回调
     *
     * @param tag
     */
    public void setFunctionsForFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FunctionManager functionManager = FunctionManager.getInstance();
        Fragment baseFragment = fm.findFragmentByTag(tag);
        if (baseFragment instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) baseFragment;
            fragment.setFunctionManager(functionManager.addFunction(new FunctionWithParamOnly<Integer>(FolderIndexFragment.FUNCTION_NAME_GETPICLIST) {
                @Override
                public void function(Integer integer) {
                    if (imageShowContentFragment != null) {
                        //接口中的方法回调
                        imageShowContentFragment.doActionImagePickLayout(integer);
                    }
                }
            }).addFunction(new FunctionNoParamNoResult(FolderIndexFragment.FUNCTION_NAME_EXIT) {
                @Override
                public void function() {
                    showExitDialog();
                }
            }).addFunction(new FunctionNoParamNoResult(FolderIndexFragment.FUNCTION_NAME_SETTING) {
                @Override
                public void function() {
                    showSettingDialog();
                }
            }).addFunction(new FunctionNoParamNoResult(ImageShowContentFragment.RESET_IMAGEINDEX) {
                @Override
                public void function() {
                    if (folderIndexFragment != null) {
                        folderIndexFragment.resetImageIndexBg();
                    }
                }
            }));
        } else if (baseFragment instanceof BaseDialogFragment) {
            BaseDialogFragment fragment = (BaseDialogFragment) baseFragment;
            fragment.setFunctionManager(functionManager.addFunction(new FunctionNoParamNoResult(SettingDialogFragment.FUNCTION_SAVEFOLDERNAME_EXIT) {
                @Override
                public void function() {
                    Snackbar.make(imageShowContentLayout, "修改文件夹成功", Snackbar.LENGTH_LONG).show();
                    if (folderIndexFragment != null) {
                        folderIndexFragment.updateFolderNames();
                    }
                }
            }));
        }

    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showSettingDialog() {
        SettingDialogFragment settingDialogFragment = new SettingDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        settingDialogFragment.show(ft, "settingDialogFragment");
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("退出提示")
                .setMessage("确定退出程序？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void checkCanUseByUsageTime() {
        if (!Constant.MOPIN_BRAND.equals(DeviceInfoManager.getInstance(this).getDeviceBrand())) {
            long startTime = (Long) SharedPreferencesUtils.getParam(this, Constant.USAGE_TIME_START_KEY, 0L);
            long endTime = (Long) SharedPreferencesUtils.getParam(this, Constant.USAGE_TIME_CURRENT_KEY, System.currentTimeMillis());
//            Log.i("XXX", "startTime:" + startTime + "    endTime:" + endTime + "  minutes:" + TimeUtil.differentMinutesByMillisecond(startTime, endTime));
            if (TimeUtil.differentMinutesByMillisecond(startTime, endTime) >= 20) {
                VerifyDialogFragment verifyDialogFragment = new VerifyDialogFragment();
                verifyDialogFragment.setCancelable(false);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                verifyDialogFragment.show(ft, "verifyDialogFragment");
            }
        }
    }
}
