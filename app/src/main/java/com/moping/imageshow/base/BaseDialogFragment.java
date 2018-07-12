package com.moping.imageshow.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.moping.imageshow.MainActivity;
import com.moping.imageshow.func.FunctionManager;

public class BaseDialogFragment extends DialogFragment {

    protected Context mContext;
    protected FunctionManager mFunctionManager;
    private MainActivity mBaseActivity;

    public void setFunctionManager(FunctionManager functionManager) {
        this.mFunctionManager = functionManager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof MainActivity) {
            mBaseActivity = (MainActivity)context;
            mBaseActivity.setFunctionsForFragment(getTag());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();
        if (context instanceof MainActivity) {
            mBaseActivity = (MainActivity)context;
            mBaseActivity.setFunctionsForFragment(getTag());
        }
    }
}
