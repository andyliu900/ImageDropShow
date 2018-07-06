package com.moping.imageshow;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.moping.imageshow.base.BaseFragment;
import com.moping.imageshow.func.FunctionManager;
import com.moping.imageshow.func.FunctionNoParamNoResult;
import com.moping.imageshow.func.FunctionWithParamOnly;

public class MainActivity extends AppCompatActivity {

    private FolderIndexFragment folderIndexFragment;
    private ImageShowContentFragment imageShowContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        folderIndexFragment = new FolderIndexFragment();
        imageShowContentFragment = new ImageShowContentFragment();

        ft.add(R.id.folderIndexFragment, folderIndexFragment, "FolderIndexFragment");
        ft.add(R.id.imageShowContentFragment, imageShowContentFragment, "ImageShowContentFragment");

        ft.commit();
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
     * @param tag
     */
    public void setFunctionsForFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment)fm.findFragmentByTag(tag);
        FunctionManager functionManager = FunctionManager.getInstance();
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
        }));
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
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
}
