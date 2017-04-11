package cn.icarowner.icarowner.activity;


import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.icarowner.icarowner.API;
import cn.icarowner.icarowner.Constant;
import cn.icarowner.icarowner.IcarApplication;
import cn.icarowner.icarowner.R;
import cn.icarowner.icarowner.UserSharedPreference;
import cn.icarowner.icarowner.net.OkHttpManager;
import cn.icarowner.icarowner.net.OkHttpPostRequestBuilder;
import cn.icarowner.icarowner.net.OkHttpRequestBuilderFactory;
import cn.icarowner.icarowner.net.UiHandlerCallBack;
import cn.icarowner.icarowner.utils.TimeCountUtil;
import cn.icarowner.icarowner.view.CleanEditText;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.ib_login_back)
    ImageButton ibLoginBack;
    @Bind(R.id.iv_app_name)
    ImageView ivAppName;
    @Bind(R.id.cet_phone_number)
    CleanEditText cetPhoneNumber;
    @Bind(R.id.iv_phone)
    ImageView ivPhone;
    @Bind(R.id.fl_phone_number)
    FrameLayout flPhoneNumber;
    @Bind(R.id.et_valid_code)
    EditText etValidCode;
    @Bind(R.id.iv_message)
    ImageView ivMessage;
    @Bind(R.id.bt_obtain_message)
    Button btObtainMessage;
    @Bind(R.id.fl_valid_code)
    FrameLayout flValidCode;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private String phoneNum,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(UserSharedPreference.getInstance().getPhoneAndPassword())) {
            String savePhone = UserSharedPreference.getInstance().getPhoneAndPassword().substring(0, 11);
            cetPhoneNumber.setText(savePhone);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        ButterKnife.unbind(this);
    }

    //  返回
    public void back(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        showWaitingDialog(true);
        OkHttpPostRequestBuilder builder = OkHttpRequestBuilderFactory.createPostRequestBuilder(
                Constant.getHost() + API.LOGIN_WITHOUT_REGISTRATION);
        phoneNum = cetPhoneNumber.getText().toString();
        builder.put("mobile", phoneNum);
        password = etValidCode.getText().toString();
        builder.put("code", password);
        OkHttpManager.post(builder, new UiHandlerCallBack() {
            @Override
            public void success(JSONObject data) {
                showWaitingDialog(false);
                UserSharedPreference.getInstance().setJwtToken(data.getString("token"));
                UserSharedPreference.getInstance().setPhoneAndPassword(phoneNum, password);
                String userId = data.getString("id");
                String mobile = data.getString("mobile");
                btnLogin.requestFocus();
                toast("登录成功"+"  "+data.getString("token"));
                Intent intent = new Intent(LoginActivity.this,DefineActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void error(int status, String message, JSONObject statusInfo) {
                showWaitingDialog(false);
            }

            @Override
            public void progress(int progress) {

            }

            @Override
            public void failed(int code, String msg) {
                toast(msg);
                showWaitingDialog(false);
            }
        });
    }

    @OnClick(R.id.bt_obtain_message)
    public void obtainShortMessage() {
        // 获取验证码
        getValidCode();
    }

    private void getValidCode() {
        OkHttpPostRequestBuilder builder = OkHttpRequestBuilderFactory.createPostRequestBuilder(
                Constant.getHost() + API.LOGIN_MSG_VALID_CODE
        );
        if (TextUtils.isEmpty(cetPhoneNumber.getText().toString())){
            toast("请输入你的手机号");
            return;
        }
        builder.put("mobile", cetPhoneNumber.getText().toString());
        OkHttpManager.post(builder, new UiHandlerCallBack() {
            @Override
            public void success(JSONObject data) {
                String send_interval = data.getString("send_interval");
//                toast(send_interval + "秒后重发");
                String verificationCode = data.getString("verification_code");
                if (null != verificationCode) {
//                    toast("测试验证码" + verificationCode);
                    etValidCode.setText(verificationCode);
                }
                long time = Long.parseLong(send_interval);
                TimeCountUtil timeCountUtil = new
                        TimeCountUtil(LoginActivity.this, time * 1000, 1000, btObtainMessage);
                timeCountUtil.start();
            }

            @Override
            public void error(int status, String message, JSONObject statusInfo) {
            }

            @Override
            public void progress(int progress) {

            }

            @Override
            public void failed(int code, String msg) {
                toast(msg);
            }
        });
    }
}
