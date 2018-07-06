package com.moping.imageshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.moping.imageshow.base.BaseFragment;

public class FolderIndexFragment extends BaseFragment implements View.OnClickListener {

    public static final String FUNCTION_NAME_GETPICLIST = "getFolderPicList";
    public static final String FUNCTION_NAME_EXIT = "exitApp";

    ImageButton folder_one_btn;
    ImageButton folder_two_btn;
    ImageButton folder_three_btn;
    ImageButton folder_four_btn;
    ImageButton folder_five_btn;
    ImageButton folder_six_btn;

    View exit_layout;
    View exit_img;
    View exit_text;

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
        exit_layout = view.findViewById(R.id.exit_layout);
        exit_img = view.findViewById(R.id.exit_img);
        exit_text = view.findViewById(R.id.exit_text);
        folder_one_btn.setOnClickListener(this);
        folder_two_btn.setOnClickListener(this);
        folder_three_btn.setOnClickListener(this);
        folder_four_btn.setOnClickListener(this);
        folder_five_btn.setOnClickListener(this);
        folder_six_btn.setOnClickListener(this);
        exit_layout.setOnClickListener(this);
        exit_img.setOnClickListener(this);
        exit_text.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.folder_one:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 1);
                break;
            case R.id.folder_two:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 2);
                break;
            case R.id.folder_three:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 3);
                break;
            case R.id.folder_four:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 4);
                break;
            case R.id.folder_five:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 5);
                break;
            case R.id.folder_six:
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, 6);
                break;
            case R.id.exit_layout:
            case R.id.exit_img:
            case R.id.exit_text:
                mFunctionManager.invokeFunc(FUNCTION_NAME_EXIT);
                break;
                default:
        }
    }
}
