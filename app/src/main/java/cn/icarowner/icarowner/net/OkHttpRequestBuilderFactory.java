package cn.icarowner.icarowner.net;

public class OkHttpRequestBuilderFactory {

    public static OkHttpGetRequestBuilder createGetRequestBuilder(String url) {
        return new OkHttpGetRequestBuilder(url);
    }

    public static OkHttpPostRequestBuilder createPostRequestBuilder(String url) {
        return new OkHttpPostRequestBuilder(url);
    }

    public static OkHttpPostRequestBuilder createEncryptPostRequestBuilder(String url, String signKey) {
        return new OkHttpPostRequestBuilder(url, signKey);
    }
}
