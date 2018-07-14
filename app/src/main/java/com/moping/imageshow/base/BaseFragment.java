package com.moping.imageshow.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.moping.imageshow.MainActivity;
import com.moping.imageshow.entity.MessageEvent;
import com.moping.imageshow.func.FunctionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseFragment extends Fragment {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageEvent(MessageEvent messageEvent) {

    }
}
