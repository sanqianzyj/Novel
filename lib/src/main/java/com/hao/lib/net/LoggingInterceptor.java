package com.hao.lib.net;

import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();

        long t1 = System.nanoTime();//请求发起的时间
//        Log.i("请求", String.format("发送请求 %s on %s%s%s",
//                request.url(), "\n连接:" + chain.connection(), "\n请求头:" + request.headers(), "\n请求参数：" + request.toString()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();//收到响应的时间

        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
//
//        Log.i("请求", String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
//                response.request().url(),
//                responseBody.string(),
//                (t2 - t1) / 1e6d,
//                response.headers()));
        Log.i("请求", "返回数据=" + responseBody.string());
        return response;
    }
}

