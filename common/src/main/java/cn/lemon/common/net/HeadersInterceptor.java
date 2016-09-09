package cn.lemon.common.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 给okhttp添加请求头
 * Created by linlongxin on 2016/8/8.
 */

public class HeadersInterceptor implements Interceptor {

    public static String UID = "";
    public static String TOKEN = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        try {
            newRequest = request.newBuilder()
                    .addHeader("UID", UID)
                    .addHeader("token", TOKEN)
                    .build();
        } catch (Exception e) {
            return chain.proceed(request);
        }
        return chain.proceed(newRequest);
    }
}
