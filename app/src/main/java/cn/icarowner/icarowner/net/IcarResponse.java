package cn.icarowner.icarowner.net;

import com.alibaba.fastjson.JSONObject;

public class IcarResponse {

    public final static String RETURN_STATUS = "status";
    public final static String RETURN_DATA = "data";
    public final static String RETURN_STATUSINFO = "statusInfo";

    private int status;

    private JSONObject data;

    private JSONObject statusInfo;

    public IcarResponse(int status, JSONObject data, JSONObject statusInfo) {
        this.status = status;
        this.data = data;
        this.statusInfo = statusInfo;
    }

    public boolean isSuccess() {
        return 0 == this.status;
    }

    public int getStatus() {
        return this.status;
    }

    public JSONObject getData() {
        return this.data;
    }

    public JSONObject getStatusInfo() {
        return this.statusInfo;
    }
}
