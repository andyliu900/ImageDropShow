package com.moping.imageshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moping.imageshow.base.BaseFragment;
import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.SharedPreferencesUtils;

public class FolderIndexFragment extends BaseFragment implements View.OnClickListener {

    public static final String FUNCTION_NAME_GETPICLIST = "getFolderPicList";
    public static final String FUNCTION_NAME_SETTING = "settingApp";
    public static final String FUNCTION_NAME_EXIT = "exitApp";

    ImageButton folder_one_btn;
    ImageButton folder_two_btn;
    ImageButton folder_three_btn;
    ImageButton folder_four_btn;
    ImageButton folder_five_btn;
    ImageButton folder_six_btn;

    private TextView folder_one_tv;
    private TextView folder_two_tv;
    private TextView folder_three_tv;
    private TextView folder_four_tv;
    private TextView folder_five_tv;
    private TextView folder_six_tv;

    View setting_img;
    View exit_img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folderindex, null);

        folder_one_btn = (ImageButton) view.findViewById(R.id.folder_one);
        folder_two_btn = (ImageButton) view.findViewById(R.id.folder_two);
        folder_three_btn = (ImageButton) view.findViewById(R.id.folder_three);
        folder_four_btn = (ImageButton) view.findViewById(R.id.folder_four);
        folder_five_btn = (ImageButton) view.findViewById(R.id.folder_five);
        folder_six_btn = (ImageButton) view.findViewById(R.id.folder_six);

        folder_one_tv = (TextView)view.findViewById(R.id.folder_one_tv);
        folder_two_tv = (TextView)view.findViewById(R.id.folder_two_tv);
        folder_three_tv = (TextView)view.findViewById(R.id.folder_three_tv);
        folder_four_tv = (TextView)view.findViewById(R.id.folder_four_tv);
        folder_five_tv = (TextView)view.findViewById(R.id.folder_five_tv);
        folder_six_tv = (TextView)view.findViewById(R.id.folder_six_tv);

        setting_img = view.findViewById(R.id.setting_img);
        exit_img = view.findViewById(R.id.exit_img);
        folder_one_btn.setOnClickListener(this);
        folder_two_btn.setOnClickListener(this);
        folder_three_btn.setOnClickListener(this);
        folder_four_btn.setOnClickListener(this);
        folder_five_btn.setOnClickListener(this);
        folder_six_btn.setOnClickListener(this);
        setting_img.setOnClickListener(this);
        exit_img.setOnClickListener(this);

        updateFolderNames();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.folder_one:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 1);
                resetImageIndexBg();
                folder_one_btn.setImageResource(R.mipmap.folder_one_press);
                break;
            case R.id.folder_two:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 2);
                resetImageIndexBg();
                folder_two_btn.setImageResource(R.mipmap.folder_two_press);
                break;
            case R.id.folder_three:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 3);
                resetImageIndexBg();
                folder_three_btn.setImageResource(R.mipmap.folder_three_press);
                break;
            case R.id.folder_four:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 4);
                resetImageIndexBg();
                folder_four_btn.setImageResource(R.mipmap.folder_four_press);
                break;
            case R.id.folder_five:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 5);
                resetImageIndexBg();
                folder_five_btn.setImageResource(R.mipmap.folder_five_press);
                break;
            case R.id.folder_six:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 6);
                resetImageIndexBg();
                folder_six_btn.setImageResource(R.mipmap.folder_six_press);
                break;
            case R.id.setting_img:
                mFunctionManager.invokeFunc(FUNCTION_NAME_SETTING);
                break;
            case R.id.exit_img:
                mFunctionManager.invokeFunc(FUNCTION_NAME_EXIT);
                break;
                default:
        }
    }

    public void resetImageIndexBg() {
        folder_one_btn.setImageResource(R.mipmap.folder_one);
        folder_two_btn.setImageResource(R.mipmap.folder_two);
        folder_three_btn.setImageResource(R.mipmap.folder_three);
        folder_four_btn.setImageResource(R.mipmap.folder_four);
        folder_five_btn.setImageResource(R.mipmap.folder_five);
        folder_six_btn.setImageResource(R.mipmap.folder_six);
    }

    public void updateFolderNames() {
        String folderName1 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_ONE_KEY, "文件夹1");
        folder_one_tv.setText(folderName1);

        String folderName2 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_TWO_KEY, "文件夹2");
        folder_two_tv.setText(folderName2);

        String folderName3 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_THREE_KEY, "文件夹3");
        folder_three_tv.setText(folderName3);

        String folderName4 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_FOUR_KEY, "文件夹4");
        folder_four_tv.setText(folderName4);

        String folderName5 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_FIVE_KEY, "文件夹5");
        folder_five_tv.setText(folderName5);

        String folderName6 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_SIX_KEY, "文件夹6");
        folder_six_tv.setText(folderName6);
    }
}
