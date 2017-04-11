package cn.icarowner.icarowner;
import cn.icarowner.icarowner.manager.PhoneManager;


/**
 * Created by cj on 2016/9/5.
 */

public final class Constant {

    private static final String REQUEST_HOST = "https://api.woshichezhu.com/";
    private static final String REQUEST_HOST_FOR_TEST = "https://dev.api.woshichezhu.com/";

    private static final String PUSH_HOST = "api.woshichezhu.com";
    private static final String PUSH_HOST_FOR_TEST = "dev.api.woshichezhu.com";

    //private static final String REQUEST_HOST_FOR_TEST = "http://192.168.0.122:9000/";

    //public static final String SIGN_KEY = "PQYfcoazBZvc525XUAt4cob55NExEt3T";

    public static final String PAY_SIGN_NAME = "carsign";
    public static final String PAY_SIGN_KEY = "icarpaykey";

    //public static final String YUNBA_APP_KEY_TEST = "57cfdd7b6bc400044f63b3f9";
    //public static final String YUNBA_APP_KEY = "57e2968f794398d3630e7dd2";

    //public static final String TALKING_DATA_APP_ID = "11E2759CF85E6BD7B5DF99278B6E209F";
    //public static final String TALKING_DATA_CHANNEL_KEY_TEST = "Icarowner_Debug";
    // Official channel key: Icarowner_Release
    //public static final String TALKING_DATA_CHANNEL_KEY_RELEASE = "Icarowner_Release";


    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static String getHost() {
        if (isDebug()) {
            return REQUEST_HOST_FOR_TEST;
        } else {
            return REQUEST_HOST;
        }
    }

    public static String getPushHost() {
        if (isDebug()) {
            return PUSH_HOST_FOR_TEST;
        } else {
            return PUSH_HOST;
        }
    }

    //	public static String getYunbaAppKey() {
    //		if (isDebug()) {
    //			return YUNBA_APP_KEY_TEST;
    //		} else {
    //			return YUNBA_APP_KEY;
    //		}
    //	}

    //public static String getTalkingDataAppId() {
    //	return TALKING_DATA_APP_ID;
    //}

    //public static String getTalkingDataChannelKey() {
    //	if (isDebug()) {
    //		return TALKING_DATA_CHANNEL_KEY_TEST;
    //	} else {
    //		return TALKING_DATA_CHANNEL_KEY_RELEASE;
    //	}
    //}

    /**
     * 微信支付
     */
    public static final String WE_CHAT_APPID = "wx83c6b567ad37b372"; //微信ID
    //	public static final String WE_CHAT_APPID = "wx83c6b567ad37b372"; //微信ID
    //  API密钥，在商户平台设置
    //public static final  String WX_PAY_API_KEY="2041914800F74048c7976155edfecbb2";

    /**
     * 支付宝支付
     */
    public static final String ALIPAY_PARTNER = "2088021948679491";
    public static final String ALIPAY_SELLER = "icarowner@icarowner.cn";

    /**
     * 分享
     */
    //	public static final String WX_APPID = "wxdaf869f9bd24b02f"; //微信ID
    //	public static final String WX_APPSECRET = "09e00845d4575e4515a5c72d4a8781e5"; //微信ID
    //	public static final String QQ_APPID = "1103292660"; //QQ登录ID
    //	public static final String SINA_APPID = "2247106580";	//新浪登录ID

    /**
     *YUNBA
     */
    //	public static final String YUNBA_APPKEY = "55c99e8b9477ebf52469571a";
    //	public static final String YUNBA_DEBUG_APPKEY = "55ffb4f94a481fa955f396d0";

    /**
     * 微信支付
     */
    //  API密钥，在商户平台设置
    //    public static final  String WX_PAY_API_KEY="9bfe7c6249f79ab1012e726fd84d872b";


    public static final String APP_DIR = PhoneManager.getSdCardRootPath() + "/Icarowner/";//app文件目录
    public static final String IMAGE_CACHE_DIR_PATH = APP_DIR + "cache/";// 图片缓存地址
    public static final String UPLOAD_FILES_DIR_PATH = APP_DIR + "upload/";//上传文件、零时文件存放地址
    public static final String DOWNLOAD_DIR_PATH = APP_DIR + "downloads/";// 下载文件存放地址
    //public static final String IDENTITY_DIR_PATH = APP_DIR + "identity/";// 身份二维码存放地址

}