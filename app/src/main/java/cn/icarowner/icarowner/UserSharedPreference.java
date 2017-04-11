package cn.icarowner.icarowner;

import android.content.Context;
import android.content.SharedPreferences;

import cn.icarowner.icarowner.manager.ACache;


/**
 * Created by cj on 2016/9/8.
 * <p>
 * 存储登录信息以及版本信息
 */
public class UserSharedPreference {

    private static UserSharedPreference instance;

    private Context mContext;
    private ACache mACache;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static final String SHARED_PREFERENCE_NAME = "USER";

    private static final int CACHE_SECONDS = 60 * 60;
    private static final String CACHE_JWT_TOKEN_KEY = "jwt_token";
    private static final String CACHE_LATEST_VERSION_CODE_KEY = "latest_version_code";
    private static final String CACHE_USER_KEY = "user";
    private static final String CACHE_PHONE_PASSWORD_KEY = "phone_password";
    private static final String CACHE_FIND_MANAGER = "find_manager";

    public UserSharedPreference(Context context) {
        this.mContext = context;
        this.mACache = ACache.get(mContext);
        this.mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.mEditor = mSharedPreferences.edit();
    }

    public static synchronized UserSharedPreference getInstance() {
        if (instance == null) {
            instance = new UserSharedPreference(IcarApplication.getContext());
        }
        return instance;
    }

    /**
     * 是否已经登录
     *
     * @return boolean
     */
    public boolean hasLogined() {
        return null != this.getJwtToken();
    }

    /**
     * 退出登录
     */
    public void logout() {
        this.removeJwtToken();
    }

    /**
     * 设置用户的jwt token
     *
     * @param token
     */
    public void setJwtToken(String token) {
        this.putJwtTokenToCache(token);

        this.putJwtTokenToSharedPreference(token);
    }

    private void putJwtTokenToCache(String token) {
        mACache.put(CACHE_JWT_TOKEN_KEY, token, CACHE_SECONDS);
    }

    private void putJwtTokenToSharedPreference(String token) {
        mEditor.putString(CACHE_JWT_TOKEN_KEY, token);
        mEditor.commit();
    }

    /**
     * 获取用户的jwt token
     */
    public String getJwtToken() {
        String token = this.getJwtTokenFromCache();

        if (null == token) {
            token = this.getJwtTokenFromSharedPreference();

            if (null != token) {
                this.putJwtTokenToCache(token);
            }
        }
        return token;
    }

    private String getJwtTokenFromCache() {
        return mACache.getAsString(CACHE_JWT_TOKEN_KEY);
    }

    private String getJwtTokenFromSharedPreference() {
        if (!mSharedPreferences.contains(CACHE_JWT_TOKEN_KEY)) {
            return null;
        }
        return mSharedPreferences.getString(CACHE_JWT_TOKEN_KEY, null);
    }

    /**
     * 清除jwt token
     */
    public void removeJwtToken() {
        this.removeJwtTokenInSharedPreference();

        this.removeJwtTokenInCache();
    }

    private void removeJwtTokenInCache() {
        mACache.remove(CACHE_JWT_TOKEN_KEY);
        mACache.clear();
    }

    private void removeJwtTokenInSharedPreference() {
        mEditor.remove(CACHE_JWT_TOKEN_KEY);
        mEditor.commit();
    }


    /**
     * 清除用户资料
     */
    public void removeUser() {
        this.removeUserInSharedPreference();

        this.removeUserInCache();
    }

    private void removeUserInCache() {
        mACache.remove(CACHE_USER_KEY);
    }

    private void removeUserInSharedPreference() {
        mEditor.remove(CACHE_USER_KEY);
        mEditor.commit();
    }

    /**
     * 设置app最后的版本号
     *
     * @param versionCode
     */
    public void setLatestVersionCode(int versionCode) {
        this.putLatestVersionCodeToCache(versionCode);

        this.putLatestVersionCodeToSharedPreference(versionCode);
    }

    private void putLatestVersionCodeToCache(int versionCode) {
        mACache.put(CACHE_LATEST_VERSION_CODE_KEY, Integer.valueOf(versionCode), CACHE_SECONDS);
    }

    private void putLatestVersionCodeToSharedPreference(int versionCode) {
        mEditor.putInt(CACHE_LATEST_VERSION_CODE_KEY, versionCode);
        mEditor.commit();
    }

    /**
     * valid that whether given version is futher
     * than latest version code in cache
     *
     * @return boolean
     */
    public boolean isNewVersionCode(int versionCode) {
        return versionCode > this.getLatestVersionCode();
    }

    /**
     * 获取最后记录的app版本号
     * <p>
     * return int   -1 if no latest version code
     */
    public int getLatestVersionCode() {
        int versionCode = this.getLatestVersionCodeFromCache();

        if (versionCode < 0) {
            versionCode = this.getLatestVersionCodeFromSharedPreference();

            if (versionCode > 0) {
                this.setLatestVersionCode(versionCode);
            }
        }

        return versionCode;
    }

    private int getLatestVersionCodeFromCache() {
        Object object = mACache.getAsObject(CACHE_LATEST_VERSION_CODE_KEY);
        if (null == object) {
            return -1;
        }
        return ((Integer) object).intValue();
    }

    private int getLatestVersionCodeFromSharedPreference() {
        if (!mSharedPreferences.contains(CACHE_LATEST_VERSION_CODE_KEY)) {
            return -1;
        }
        return mSharedPreferences.getInt(CACHE_LATEST_VERSION_CODE_KEY, -1);
    }

    /**
     * 清除latest version code(清除app数据时使用)
     */
    public void removeLatestVersionCode() {
        this.removeLatestVersionCodeInSharedPreference();

        this.removeLatestVersionCodeInCache();
    }

    private void removeLatestVersionCodeInCache() {
        mACache.remove(CACHE_LATEST_VERSION_CODE_KEY);
    }

    private void removeLatestVersionCodeInSharedPreference() {
        mEditor.remove(CACHE_LATEST_VERSION_CODE_KEY);
        mEditor.commit();
    }

    /**********************************************账号密码********************************************************************/
    /**
     * 存储用户账号密码
     * @param phoneNum
     * @param password
     */
    public void setPhoneAndPassword(String phoneNum, String password) {
        this.putPhoneAndPasswordToCache(phoneNum, password);
        this.putPhoneAndPasswordToSharedPreference(phoneNum, password);
    }

    public void putPhoneAndPasswordToCache(String phoneNum, String password) {
        mACache.put(CACHE_PHONE_PASSWORD_KEY, phoneNum + "/" + password, CACHE_SECONDS);
    }

    public void putPhoneAndPasswordToSharedPreference(String phoneNum, String password) {
        mEditor.putString(CACHE_PHONE_PASSWORD_KEY, phoneNum + "/" + password);
        mEditor.commit();
    }

    /**
     * 获取账号密码
     */
    public String getPhoneAndPassword() {
        String phoneAndPassword = this.getPhoneAndPasswordFromCache();

        if (null == phoneAndPassword) {
            phoneAndPassword = this.getPhoneAndPasswordFromSharedPreference();

            if (null != phoneAndPassword) {
                this.putPhoneAndPasswordToSharedPreference(phoneAndPassword.substring(0,11),phoneAndPassword.substring(12));
            }
        }
        return phoneAndPassword;
    }

    private String getPhoneAndPasswordFromCache() {
        return mACache.getAsString(CACHE_PHONE_PASSWORD_KEY);
    }

    private String getPhoneAndPasswordFromSharedPreference() {
        if (!mSharedPreferences.contains(CACHE_PHONE_PASSWORD_KEY)) {
            return null;
        }
        return mSharedPreferences.getString(CACHE_PHONE_PASSWORD_KEY, null);
    }

    /**
     * 清除账号密码
     */
    public void removePhoneAndPassword() {
        this.removePhoneAndPasswordInSharedPreference();

        this.removePhoneAndPasswordInCache();
    }

    private void removePhoneAndPasswordInCache() {
        mACache.remove(CACHE_PHONE_PASSWORD_KEY);
    }

    private void removePhoneAndPasswordInSharedPreference() {
        mEditor.remove(CACHE_PHONE_PASSWORD_KEY);
        mEditor.commit();
    }


    /*****************************************记录找经理弹出窗口是否显示过********************************************************************************/
    /**
     * 设置找经理显示次数
     *
     * @param versionCode
     */

    public void putFindManagerTimeToSharedPreference(int versionCode) {
        mEditor.putInt(CACHE_FIND_MANAGER, versionCode);
        mEditor.commit();
    }

    /**
     * 获取找经理显示次数
     * <p>
     * return int   -1 if no find time
     */
    public int getFindManagerTimFromSharedPreference() {
        if (!mSharedPreferences.contains(CACHE_FIND_MANAGER)) {
            return -1;
        }
        return mSharedPreferences.getInt(CACHE_FIND_MANAGER, -1);
    }
}
