package cn.icarowner.icarowner.net;

import android.os.Build;



import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.icarowner.icarowner.manager.PhoneManager;
import cn.icarowner.icarowner.utils.SignUtil;


public class OkHttpPostRequestBuilder extends OkHttpRequestBuilder {

    protected boolean mHasFile = false;
    protected String mSignKey;
    protected List<Pair> params;

    public OkHttpPostRequestBuilder(String url) {
        super(url);
        this.getHeaders().put("X-Requested-With", "XMLHttpRequest");
        //this.getHeaders().put("User-Agent", "Icar_Android" + PhoneManager.getVersionInfo().versionName + "/" + Build.VERSION.SDK_INT);
    }

    public OkHttpPostRequestBuilder(String url, String signKey) {
        super(url);
        this.mSignKey = signKey;
        this.getHeaders().put("X-Requested-With", "XMLHttpRequest");
        this.getHeaders().put("User-Agent", "Icar_Android" + PhoneManager.getVersionInfo().versionName + "/" + Build.VERSION.SDK_INT);
    }

    public boolean isEncrypted() {
        return null != this.mSignKey;
    }

    public void put(String key, Pair value) {
        if (((File) value.getSecond()).exists()) {
            this.getParams().put(key, value);
            this.mHasFile = true;
        }
    }

    public boolean hasFile() {
        return mHasFile;
    }

    private RequestBody buildRequestBody() {
        if (null == getParams() || 0 == getParams().size()) {
            return null;
        }

        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        Iterator<Map.Entry<String, Object>> iterator = getParams().entrySet().iterator();
        StringBuffer sbContent = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getValue() instanceof Pair) {
                // upload file
                Pair<String, File> filePair = (Pair<String, File>) entry.getValue();
                builder.addFormDataPart(entry.getKey(), filePair.getSecond().getName(),
                        RequestBody.create(MediaType.parse(filePair.getFirst()), filePair.getSecond()));
            } else {
                // String
                builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
                if (this.isEncrypted() && !entry.getKey().matches("^\\w+\\[\\d+\\]$")) {
                    if (null == params) {
                        params = new LinkedList<>();
                    }
                    //非数组
                    //sbContent.append('&');
                    //sbContent.append(urlEncodeUTF8(entry.getKey()))
                    //        .append('=')
                    //        .append(urlEncodeUTF8((String) entry.getValue()));
                    params.add(new Pair(entry.getKey(), entry.getValue()));
                }
            }
        }
        if (this.isEncrypted()) {
            builder.addFormDataPart("sign", SignUtil.signWithMd5(mSignKey, params).toUpperCase());
        }
        return builder.build();
    }

    @Override
    public Request build() {
        return this.buildHeaders().url(getUrl()).post(buildRequestBody()).build();
    }
}
