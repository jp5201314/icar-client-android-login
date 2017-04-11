package cn.icarowner.icarowner.activity;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.icarowner.icarowner.R;
import cn.icarowner.icarowner.UserSharedPreference;
import cn.icarowner.icarowner.dialog.DialogCreater;
import cn.icarowner.icarowner.dialog.WaitDialog;
import cn.icarowner.icarowner.inter.IActivityFinish;
import cn.icarowner.icarowner.inter.IComponentContainer;
import cn.icarowner.icarowner.inter.ILifeCycleComponent;
import cn.icarowner.icarowner.manager.ACache;
import cn.icarowner.icarowner.manager.AppManage;
import cn.icarowner.icarowner.manager.LifeCycleComponentManager;
import cn.icarowner.icarowner.manager.PhoneManager;
import cn.icarowner.icarowner.manager.SystemBarTintManager;


/**
 * Created by ouarea on 16/2/29.
 */

public class BaseActivity extends AppCompatActivity implements IComponentContainer, IActivityFinish {
    public static final String BROADCAST_FLAG = "cn.icarowner.icar.broadcast.common";

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();
    private static LinearInterpolator interpolator = new LinearInterpolator();

    private CommonBroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManage.getInstance().addActivity(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void finish() {
        AppManage.getInstance().finishActivity(this);
    }

    @Override
    public void finishActivity() {
        super.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mComponentContainer.onBecomesVisibleFromPartiallyInvisible();

        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new CommonBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_FLAG);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mComponentContainer.onBecomesPartiallyInvisible();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onStop() {
        super.onStop();
        mComponentContainer.onBecomesTotallyInvisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComponentContainer.onDestroy();
    }


    /**
     * 开始执行contentView动画
     */
    protected void startContentViewAnimation(View contentView, AnimatorListenerAdapter onAnimationEnd) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(contentView, "alpha", 1),
                ObjectAnimator.ofFloat(contentView, "translationY", 0)
        );
        set.setDuration(400).start();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(onAnimationEnd);
    }

    /**
     * toast message
     *
     * @param text
     */
    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast message
     *
     * @param resource
     */
    protected void toast(int resource) {
        Toast.makeText(this, resource, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addComponent(ILifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }

    private class CommonBroadcastReceiver extends BroadcastReceiver {

        protected boolean allowJumpToLogin() {
            return null == ACache.get(BaseActivity.this).getAsString("loging") &&
                    !getRunningActivityName().equals(".activity.LoginActivity");
        }

        protected void jumpToLogin() {
            if (!allowJumpToLogin()) return;

            ACache.get(BaseActivity.this).put("loging", true, 5);
            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("TYPE")) {
                int type = intent.getIntExtra("TYPE", -1);
                String msg = intent.getStringExtra("msg");
                switch (type) {
                    case 10000://GENERAL
                        toast(msg);
                        break;
                    case 10001://GENERAL
                        toast(msg);
                        break;
                    case 40000://NOT_PROVIDED_JWT_TOKEN
                        toast("您还未登录，请登录");
                        UserSharedPreference.getInstance().removeUser();

                        jumpToLogin();
                        break;
                    case 40001://INVALID_JWT_TOKEN
                        toast("令牌已刷新，请重新登录");
                        UserSharedPreference.getInstance().removeUser();

                        jumpToLogin();
                        break;
                    case 40004://NOT_FOUND_JWT_USER
                        toast("用户信息已过期，请重新登录");
                        UserSharedPreference.getInstance().removeUser();

                        jumpToLogin();
                        break;
                    case 40005://EXPIRED_JWT_TOKEN
                        toast("登录已失效，请重新登录");
                        UserSharedPreference.getInstance().removeUser();

                        jumpToLogin();
                        break;
                    case 40006://EXPIRED_JWT_TOKEN
                        toast("你的账号已在其他设备登录");
                        UserSharedPreference.getInstance().removeUser();

                        jumpToLogin();
                        break;
                    default:
                        break;
                }
            }
        }
    }



    protected void showWaitingDialog(boolean show) {
        showWaitingDialog(show, getString(R.string.please_wait));
    }

    protected void showWaitingDialog(boolean show, String waitingNotice) {
        WaitDialog waitingDialog =  DialogCreater.createProgressDialog(BaseActivity.this, waitingNotice);;
        if (!show) {
            waitingDialog.dismiss();
            return;
        }
        waitingDialog.show();
    }

    /**
     * 从本地获取持久化的entity
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T getEntityFromLocalStorage(String storageKey, String key, Class<T> clazz) {
        SharedPreferences share = getSharedPreferences(storageKey, Context.MODE_PRIVATE);
        if (share.contains(key)) {
            String dealerJson = share.getString(key, null);
            return null == dealerJson ? null : JSON.parseObject(dealerJson, clazz);
        }
        return null;
    }

    /**
     * 将entity持久化存储到本地(异步,非及时)
     *
     * @param key
     * @param entity
     */
    protected void putEntityToLocalStorage(String storageKey, String key, Object entity) {
        putEntityToLocalStorage(storageKey, key, entity, false);
    }

    /**
     * 将entity持久化存储到本地(及时)
     *
     * @param key
     * @param entity
     */
    protected void putEntityToLocalStorageNow(String storageKey, String key, Object entity) {
        putEntityToLocalStorage(storageKey, key, entity, true);
    }

    private void putEntityToLocalStorage(String storageKey, String key, Object entity, boolean isNow) {
        SharedPreferences share = getSharedPreferences(storageKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        if (null == entity) {
            if (share.contains(key)) {
                editor.remove(key);
                return;
            }
            return;
        }
        editor.putString(key, JSON.toJSONString(entity));
        if (isNow) {
            editor.commit();
        } else {
            editor.apply();
        }
    }



    /**
     * 验证是否登录,如果未登录,则跳转登录页面
     *
     * @return
     */
    protected boolean validLogin() {
        if (!UserSharedPreference.getInstance().hasLogined()) {
            return false;
        }
        return true;
    }

    /**
     * 验证是否新版,如果新版,则跳转引导页
     *
     * @return
     */
    protected boolean validNewVersion() {
        int nowVersionCode = PhoneManager.getVersionInfo().versionCode;

        UserSharedPreference userSharedPreference = UserSharedPreference.getInstance();
        if (userSharedPreference.isNewVersionCode(nowVersionCode)) {
            //startActivity(new Intent(BaseActivity.this, GuideActivity.class));
            userSharedPreference.setLatestVersionCode(nowVersionCode);
            return true;
        }
        return false;
    }

    protected String getRunningActivityName() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        String className = info.topActivity.getClassName(); //完整类名
        String packageName = info.topActivity.getPackageName();//包名


        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return shortClassName;
    }
}