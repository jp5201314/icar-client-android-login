package cn.icarowner.icarowner;

import android.app.Application;
import android.content.Context;

/**
 * Created by cj on 2016/9/5.
 */
public class IcarApplication extends Application {
    private static IcarApplication INSTANCE;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mContext = getApplicationContext();

    }


    public static synchronized IcarApplication getInstance() {
        return INSTANCE;
    }

    public static synchronized Context getContext() {
        return mContext;
    }

}
