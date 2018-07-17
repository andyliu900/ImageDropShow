package com.moping.imageshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moping.imageshow.base.BaseDialogFragment;
import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.SharedPreferencesUtils;

public class SettingDialogFragment extends BaseDialogFragment implements View.OnClickListener,
        View.OnFocusChangeListener{

    public static final String FUNCTION_SAVEFOLDERNAME_EXIT = "saveFolderName";

    private TextView folder_one_tv;
    private TextView folder_two_tv;
    private TextView folder_three_tv;
    private TextView folder_four_tv;
    private TextView folder_five_tv;
    private TextView folder_six_tv;
    private EditText folder_one_et;
    private EditText folder_two_et;
    private EditText folder_three_et;
    private EditText folder_four_et;
    private EditText folder_five_et;
    private EditText folder_six_et;

    private Button savebtn;
    private Button canclebtn;

    private View view;

    private boolean folderOneDirtyFlag = false;
    private boolean folderTwoDirtyFlag = false;
    private boolean folderThreeDirtyFlag = false;
    private boolean folderFourDirtyFlag = false;
    private boolean folderFiveDirtyFlag = false;
    private boolean folderSixDirtyFlag = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settingdialog, null);

        folder_one_tv = (TextView)view.findViewById(R.id.folder_one_tv);
        folder_two_tv = (TextView)view.findViewById(R.id.folder_two_tv);
        folder_three_tv = (TextView)view.findViewById(R.id.folder_three_tv);
        folder_four_tv = (TextView)view.findViewById(R.id.folder_four_tv);
        folder_five_tv = (TextView)view.findViewById(R.id.folder_five_tv);
        folder_six_tv = (TextView)view.findViewById(R.id.folder_six_tv);

        folder_one_et = (EditText)view.findViewById(R.id.folder_one_et);
        folder_two_et = (EditText)view.findViewById(R.id.folder_two_et);
        folder_three_et = (EditText)view.findViewById(R.id.folder_three_et);
        folder_four_et = (EditText)view.findViewById(R.id.folder_four_et);
        folder_five_et = (EditText)view.findViewById(R.id.folder_five_et);
        folder_six_et = (EditText)view.findViewById(R.id.folder_six_et);
        folder_one_et.setOnFocusChangeListener(this);
        folder_two_et.setOnFocusChangeListener(this);
        folder_three_et.setOnFocusChangeListener(this);
        folder_four_et.setOnFocusChangeListener(this);
        folder_five_et.setOnFocusChangeListener(this);
        folder_six_et.setOnFocusChangeListener(this);

        savebtn = (Button)view.findViewById(R.id.savebtn);
        canclebtn = (Button)view.findViewById(R.id.canclebtn);
        savebtn.setOnClickListener(this);
        canclebtn.setOnClickListener(this);

        init();

        return view;
    }

    private void init() {
        String folderName1 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_ONE_KEY, "金币");
        folder_one_tv.setText(folderName1);

        String folderName2 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_TWO_KEY, "黄金");
        folder_two_tv.setText(folderName2);

        String folderName3 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_THREE_KEY, "钻石");
        folder_three_tv.setText(folderName3);

        String folderName4 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_FOUR_KEY, "汽车");
        folder_four_tv.setText(folderName4);

        String folderName5 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_FIVE_KEY, "包包");
        folder_five_tv.setText(folderName5);

        String folderName6 = (String)SharedPreferencesUtils.getParam(getContext(), Constant.FOLDER_SIX_KEY, "理财产品");
        folder_six_tv.setText(folderName6);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.savebtn:
                String folderName1 = folder_one_et.getText().toString();
                if (!TextUtils.isEmpty(folderName1)) {
                    SharedPreferencesUtils.setParam(getContext(), Constant.FOLDER_ONE_KEY, folderName1);
                    folderOneDirtyFlag = true;
                } else {
                    folderOneDirtyFlag = false;
                }

                String folderName2 = folder_two_et.getText().toString();
                if (!TextUtils.isEmpty(folderName2)) {
                    SharedPreferencesUtils.setParam(getContext(), Constant.FOLDER_TWO_KEY, folderName2);
                    folderTwoDirtyFlag = true;
                } else {
                    folderTwoDirtyFlag = false;
                }

                String folderName3 = folder_three_et.getText().toString();
                if (!TextUtils.isEmpty(folderName3)) {
                    SharedPreferencesUtils.setParam(getContext(), Constant.FOLDER_THREE_KEY, folderName3);
                    folderThreeDirtyFlag = true;
                } else {
                    folderThreeDirtyFlag = false;
                }

                String folderName4 = folder_four_et.getText().toString();
                if (!TextUtils.isEmpty(folderName4)) {
                    SharedPreferencesUtils.setParam(getContext(), Constant.FOLDER_FOUR_KEY, folderName4);
                    folderFourDirtyFlag = true;
                } else {
                    folderFourDirtyFlag = false;
                }

                String folderName5 = folder_five_et.getText().toString();
                if (!TextUtils.isEmpty(folderName5)) {
                    SharedPreferencesUtils.setParam(getContext(), Constant.FOLDER_FIVE_KEY, folderName5);
                    folderFiveDirtyFlag = true;
                } else {
                    folderFiveDirtyFlag = false;
                }

                String folderName6 = folder_six_et.getText().toString();
                if (!TextUtils.isEmpty(folderName6)) {
                    SharedPreferencesUtils.setParam(getContext(), Constant.FOLDER_SIX_KEY, folderName6);
                    folderSixDirtyFlag = true;
                } else {
                    folderSixDirtyFlag = false;
                }

                if (folderOneDirtyFlag
                        || folderTwoDirtyFlag
                        || folderThreeDirtyFlag
                        || folderFourDirtyFlag
                        || folderFiveDirtyFlag
                        || folderSixDirtyFlag) {
                    mFunctionManager.invokeFunc(FUNCTION_SAVEFOLDERNAME_EXIT);
                }
                dismiss();

                break;
            case R.id.canclebtn:
                dismiss();
                break;
                default:
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            folder_one_et.setBackgroundResource(R.drawable.table_right_bg);
            folder_two_et.setBackgroundResource(R.drawable.table_right_bg);
            folder_three_et.setBackgroundResource(R.drawable.table_right_bg);
            folder_four_et.setBackgroundResource(R.drawable.table_right_bg);
            folder_five_et.setBackgroundResource(R.drawable.table_right_bg);
            folder_six_et.setBackgroundResource(R.drawable.table_right_bg);

            view.setBackgroundResource(R.drawable.table_highline_bg);
        }

    }
}
