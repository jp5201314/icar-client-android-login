package cn.icarowner.icarowner.net;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.icarowner.icarowner.IcarApplication;
import cn.icarowner.icarowner.UserSharedPreference;
import cn.icarowner.icarowner.activity.BaseActivity;
import cn.icarowner.icarowner.utils.ResponseChecker;

/**
 * Created by ouarea on 16/2/29.
 */

public abstract class UiHandlerCallBack extends Handler implements com.squareup.okhttp.Callback {
    public final static int SUCCESS = 10001;
    public final static int ERROR = 10002;
    public final static int PROGRESS = 10003;
    public final static int FAILED = 10004;

    public final static int FAILED_NETWORK = -11;
    public final static int ERROR_JSON = -12;
    public final static int ERROR_RESPONSE = -13;
    public final static int ERROR_RESPONSE_CODE = -14;

    public void onFailure(Request request, IOException e) {
        sendFailedMessage(FAILED_NETWORK, "无网络连接");
    }

    public void onResponse(Response response) throws IOException {
        if (response != null && response.isSuccessful()) try {
            IcarResponse icarResponse = ResponseChecker.explainResponse(response);
            switch (icarResponse.getStatus()) {
                case 0://请求成功
                    sendSuccessData(icarResponse.getData());
                    break;
                case 10000://普通错误
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent10000 = new Intent();
                    intent10000.putExtra("TYPE", 10000);
                    String message = icarResponse.getStatusInfo().getString("message");

                    intent10000.putExtra("msg", message);
                    intent10000.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent10000);
                    break;
                case 10001://普通错误
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent10001 = new Intent();
                    intent10001.putExtra("TYPE", 10001);
                    String message1001 = icarResponse.getStatusInfo().getString("message");

                    intent10001.putExtra("msg", message1001);
                    intent10001.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent10001);
                    break;
                case 40000://JWT_TOKEN不存在
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent40000 = new Intent();
                    intent40000.putExtra("TYPE", 40000);
                    intent40000.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent40000);
                    UserSharedPreference.getInstance().removeJwtToken();
                    UserSharedPreference.getInstance().removeUser();
                    break;
                case 40001://JWT_TOKEN不可用
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent40001 = new Intent();
                    intent40001.putExtra("TYPE", 40001);
                    intent40001.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent40001);
//                    UserSharedPreference.getInstance().removeJwtToken();
//                    UserSharedPreference.getInstance().removeUser();
                    break;
                case 40004://JWT_USER未找到
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent40004 = new Intent();
                    intent40004.putExtra("TYPE", 40004);
                    intent40004.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent40004);
                    UserSharedPreference.getInstance().removeJwtToken();
                    UserSharedPreference.getInstance().removeUser();
                    break;
                case 40005://JWT_TOKEN过期失效
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent40005 = new Intent();
                    intent40005.putExtra("TYPE", 40005);
                    intent40005.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent40005);
                    UserSharedPreference.getInstance().removeJwtToken();
                    UserSharedPreference.getInstance().removeUser();
                    break;
                case 40006://USER_KICKED
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    Intent intent40006 = new Intent();
                    intent40006.putExtra("TYPE", 40006);
                    intent40006.setAction(BaseActivity.BROADCAST_FLAG);
                    IcarApplication.getInstance().sendBroadcast(intent40006);
                    UserSharedPreference.getInstance().removeJwtToken();
                    UserSharedPreference.getInstance().removeUser();
                    break;
                default:
                    sendErrorMessage(icarResponse.getStatus(), icarResponse.getStatusInfo());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendFailedMessage(ERROR_JSON, "数据格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            sendFailedMessage(ERROR_RESPONSE, "请求响应异常");
        }
        else {
            sendFailedMessage(ERROR_RESPONSE_CODE, "请求失败，请重试");
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                success((JSONObject) msg.obj);
                break;
            case ERROR:
                JSONObject statusInfoJson = null;
                String message = "";

                if (null != msg.obj) {
                    statusInfoJson = (JSONObject) msg.obj;
                    if (statusInfoJson.containsKey("message")) {
                        message = statusInfoJson.getString("message");
                    }
                }
                error(msg.arg1, message, statusInfoJson);
                break;
            case PROGRESS:
                progress(msg.arg1);
                break;
            case FAILED:
                failed(msg.arg1, (String) msg.obj);
                break;
        }
    }

    public void sendSuccessData(JSONObject data) {
        Message message = new Message();
        message.what = SUCCESS;
        message.obj = data;
        sendMessage(message);
    }

    public void sendErrorMessage(int status, JSONObject statusInfo) {
        Message message = new Message();
        message.what = ERROR;
        message.arg1 = status;
        message.obj = statusInfo;
        sendMessage(message);
    }

    public void sendProgressMessage(int progress) {
        Message message = new Message();
        message.what = PROGRESS;
        message.arg1 = progress;
        sendMessage(message);
    }

    public void sendFailedMessage(int code, String msg) {
        Message message = new Message();
        message.what = FAILED;
        message.arg1 = code;
        message.obj = msg;
        sendMessage(message);
    }

    public abstract void success(JSONObject data);

    public abstract void error(int status, String message, JSONObject statusInfo);

    public abstract void progress(int progress);

    public abstract void failed(int code, String msg);
}
