package com.moping.imageshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moping.imageshow.adapter.FolderAdapter;
import com.moping.imageshow.base.BaseFragment;
import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.SharedPreferencesUtils;
import com.moping.imageshow.view.CustomItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FolderIndexFragment extends BaseFragment implements View.OnClickListener {

    public static final String FUNCTION_NAME_GETPICLIST = "getFolderPicList";
    public static final String FUNCTION_NAME_SETTING = "settingApp";
    public static final String FUNCTION_NAME_EXIT = "exitApp";

    RecyclerView folder_list;

    View setting_img;
    View exit_img;

    List<String> folderNames = new ArrayList<>();
    FolderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folderindex, null);

        folder_list = (RecyclerView) view.findViewById(R.id.folder_list);
        folder_list.setLayoutManager(new LinearLayoutManager(mContext));
        folder_list.addItemDecoration(new CustomItemDecoration(20));
        adapter = new FolderAdapter();
        adapter.setOnFolderClickListener(new FolderAdapter.OnFolderClickListener() {
            @Override
            public void folderClick(int position) {
                mFunctionManager.invokeFunc(FUNCTION_NAME_GETPICLIST, position + 1);
                resetImageIndexBg();
                adapter.setCurrentSelectedFolderIndes(position);
            }
        });
        folder_list.setAdapter(adapter);

        setting_img = view.findViewById(R.id.setting_img);
        exit_img = view.findViewById(R.id.exit_img);
        setting_img.setOnClickListener(this);
        exit_img.setOnClickListener(this);

        updateFolderNames();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        updateFolderNames();
    }

    public void updateFolderNames() {
        String folderName1 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_ONE_KEY, "金币");
        String folderName2 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_TWO_KEY, "黄金");
        String folderName3 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_THREE_KEY, "钻石");
        String folderName4 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_FOUR_KEY, "汽车");
        String folderName5 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_FIVE_KEY, "包包");
        String folderName6 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_SIX_KEY, "理财产品");

        folderNames.clear();
        folderNames.add(folderName1);
        folderNames.add(folderName2);
        folderNames.add(folderName3);
        folderNames.add(folderName4);
        folderNames.add(folderName5);
        folderNames.add(folderName6);

        adapter.setFolderNames(folderNames);
        adapter.setCurrentSelectedFolderIndes(-1);
        adapter.notifyDataSetChanged();
    }
}
