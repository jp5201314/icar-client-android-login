package cn.icarowner.icarowner;
/**
 * Created by cj on 2016/9/7.
 */
public class API {
    public static final String LOGIN_WITHOUT_REGISTRATION = "api/user/login_without_registration";//不注册登录
    public static final String LOGIN_MSG_VALID_CODE = "api/user/login/code";//登录短信验证码

    public static final String MAIN_ACTIVITY_DIAPLAY = "api/home_page_data";//主页

    public static final String UPLOAD_IMAGE = "api/images/tmp/upload";//图片上传
    public static final String PERSONAL_INFORMATION = "api/user/profile";//个人资料
    public static final String BANNER = "api/banners";//banner

    public static final String FOUR_SHOP_DETAIL = "api/dealers/%s";//四S店详情
    public static final String FOUR_SHOP_LIST = "api/groups/%s/dealers";//四S店列表


    public static final String ADVISOR_INFORMATION = "api/dealer/users/%s";//顾问信息
    public static final String ADVISOR_LIST = "api/dealer/users";//顾问列表


    public static final String WAIT_EVALUATE_LIST = "api/service/orders/pending_evaluated";//待评价服务单列表
    public static final String EVALUATION_ORDER = "api/service/orders/%s/evaluation";//评价服务单
    public static final String EVALUATION_COUPON_MONEY = "api/service/orders/%s/amount_of_coupon_after_evaluation";//评价送优惠券的金额


    public static final String ALREADY_FINISH_ORDER_LIST = "api/service/orders/finished";//已完成的服务单列表


    public static final String WAIT_PAYMENT_ORDER_LIST = "api/service/orders/pending_pay";//待支付的服务单列表


    public static final String SERVICE_ORDER_DETAIL = "api/service/orders/%s";//服务单详情

    public static final String LATEST_ORDER_DETAIL = "api/service/orders/latest";//最近的服务单详情


    public static final String SCHEDULE_ORDER_LIST = "api/service/orders/unfinished";//进度列表

    public static final String COUPON_LIST = "api/user/coupons";//用户的优惠券列表
    public static final String ORDER_CAN_CHOICE_COUPON_LIST = "api/orders/%s/available_coupons";//订单可选择的优惠券列表
    public static final String CAN_RECEIVE_COUPON_LIST_DEALER = "api/dealers/%s/donation_coupons";//可领取的优惠券列表(店铺)
    public static final String CAN_RECEIVE_COUPON_LIST_GROUP = "api/groups/%s/donation_coupons";//可领取的优惠券列表(集团)
    public static final String RECEIVE_COUPON_DEALER = "api/dealers/%s/receive_coupon";//领取优惠券(店铺)
    public static final String RECEIVE_COUPON_GROUP = "api/groups/%s/receive_coupon";//领取优惠券(集团)
    public static final String CHOICE_COUPON = "api/orders/%s/bind_coupon";//使用优惠券
    public static final String CANCEL_CHOICE_COUPON = "api/orders/%s/unbind_coupon";//取消使用优惠券


    //    public static final String ALIPAY_ORDER = "api/payment/alipay/app-pay-prepare";//支付宝支付(旧的接口)
    public static final String ALIPAY_PREPARE_ORDER = "api/payment/alipay/app-pay-prepare-by-order-id";//支付宝下预支付单ById
    //    public static final String ALIPAY_ORDER_CALLBACK = "api/payment/alipay/app-pay-notify";//支付宝支付回调
    //    public static final String WXPAY_ORDER = "api/payment/wxpay/app-pay-prepare";//微信支付（旧的接口）
    public static final String WXPAY_PREPARE_ORDER = "api/payment/wxpay/app-pay-prepare-by-order-id";//微信下预支付单ById
    //    public static final String WXPAY_ORDER_CALLBACK = "api/payment/wxpay/app-pay-notify";//微信支付回调
    public static final String ZERO_PAY = "api/payment/balance/app-pay";//0元支付
    public static final String XYPAY_PREPARE_ORDER = "api/payment/industrial_bank/app-pay-prepare-by-order-id";//兴业下预支付单ById


    public static final String FEED_BACK = "api/user/feedback";//意见反馈
    public static final String UPDATE_VERSION = "versions/latest";//版本更新

    public static final String SERVICE_ORDER_FEED_BACK = "api/service/orders/%s/feedbacks";//服务单意见反馈


}
