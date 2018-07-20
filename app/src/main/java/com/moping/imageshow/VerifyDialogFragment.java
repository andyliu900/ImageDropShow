package com.moping.imageshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moping.imageshow.base.BaseDialogFragment;

public class VerifyDialogFragment extends BaseDialogFragment {

//    private EditText verify_et;
//    private Button verify_btn;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_verifydialog, null);

//        verify_et = (EditText)view.findViewById(R.id.verify_et);
//        verify_btn = (Button)view.findViewById(R.id.verify_btn);
//        verify_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String verifyCode = verify_et.getText().toString();
//                if (TextUtils.isEmpty(verifyCode)) {
//                    Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (checkVerifyCode(verifyCode)) {
//                    dismiss();
//                    SharedPreferencesUtils.setParam(mContext, Constant.USAGE_FLAG_KEY, true);
//                } else {
//                    Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
//                    SharedPreferencesUtils.setParam(mContext, Constant.USAGE_FLAG_KEY, false);
//                }
//            }
//        });

        return view;
    }

//    private boolean checkVerifyCode(String verifyCode) {
//        String macAddress = NetWorkUtil.getDeviceMac(mContext);
//        Log.i("XXX", "macAddress:" + macAddress);
//        if (macAddress != null && !TextUtils.isEmpty(macAddress)) {
//            int macLength = macAddress.length();
//            String lastMacStr = macAddress.substring(macLength - 5, macLength);
//            lastMacStr = lastMacStr.replace(":","");
//            if (verifyCode.equals(lastMacStr)) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }

}
