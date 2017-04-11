package cn.icarowner.icarowner.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.icarowner.icarowner.Constant;
import cn.icarowner.icarowner.UserSharedPreference;
import cn.icarowner.icarowner.net.IcarResponse;

public class ResponseChecker {
    private static IcarResponse explainResponse(String content) {
        JSONObject rst = JSON.parseObject(content.trim());

        int status = rst.getInteger(IcarResponse.RETURN_STATUS);

        JSONObject data = null;
        if (rst.containsKey(IcarResponse.RETURN_DATA)) {
            data = rst.getJSONObject(IcarResponse.RETURN_DATA);
        }

        JSONObject statusInfo = null;
        if (rst.containsKey(IcarResponse.RETURN_STATUSINFO)) {
            statusInfo = rst.getJSONObject(IcarResponse.RETURN_STATUSINFO);
        }

        return new IcarResponse(status, data, statusInfo);
    }

    public static IcarResponse explainResponse(Response response) throws IOException {
        if (null == response || !response.isSuccessful()) return null;

        refreshLocalJwtTokenFromResponseHeader(response);

        String content = response.body().string();
        if (Constant.isDebug()) {
        
        }
        return explainResponse(content);
    }

    private static void refreshLocalJwtTokenFromResponseHeader(Response response) {
        String authorization = response.header("Authorization", null);
        if (null != authorization && authorization.startsWith("Bearer ")) {
            String jwtToken = authorization.substring("Bearer ".length());
            UserSharedPreference.getInstance().setJwtToken(jwtToken);
        }
    }
}
