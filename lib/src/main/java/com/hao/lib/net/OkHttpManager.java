package com.hao.lib.net;

import android.util.Log;
import okhttp3.*;
import okio.ByteString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpManager {
    private List<RequestFormBodyParam> requestFormBodyParams;
    private String content = "";
    private List<RequestMultipartBodyParam> requestMultipartBodyParamList;
    private NetBodyType netBodyType = NetBodyType.form;
    private NetType netType;//请求方式
    private String url = ""; //请求地址
    private Callback netCallBack;
    private Request request;

    //枚举 请求的类型 multipart分块提交 form 表单提交
    private enum NetBodyType {
        multipart, form, json
    }

    //枚举请求的方式
    public enum NetType {
        Post, Get
    }


    //构造方法初始化参数
    public OkHttpManager() {
        requestFormBodyParams = new ArrayList<>();
        requestMultipartBodyParamList = new ArrayList<>();
    }


    //添加表单提交的参数配置

    public OkHttpManager addFromParam(String name, String content) {
        netBodyType = NetBodyType.form;
        requestFormBodyParams.add(new RequestFormBodyParam(name, content));
        return this;
    }

    //添加表单提交的参数配置

    public OkHttpManager addFromParam(Map<String, Object> map) {
        netBodyType = NetBodyType.form;
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            requestFormBodyParams.add(new RequestFormBodyParam(entry.getKey(), entry.getValue().toString()));
        }
        return this;
    }

    public OkHttpManager addJsonParam(String string) {
        netBodyType = NetBodyType.json;
        content = string;
        return this;
    }

    //添加分块提交的参数配置
    public OkHttpManager addMultipartParam(RequestMultipartBodyParam requestMultipartBodyParam) {
        netBodyType = NetBodyType.multipart;
        requestMultipartBodyParamList.add(requestMultipartBodyParam);
        return this;
    }


    static class OkHttpHelper {
        public static OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor());
    }

    //获取单例的okhttpClient对象
    public static OkHttpClient getOkhttpClient() {
        return OkHttpHelper.builder.build();
    }

    public void setTimeOut(int second) {
        OkHttpHelper.builder.connectTimeout(second, TimeUnit.SECONDS);
        OkHttpHelper.builder.callTimeout(second, TimeUnit.SECONDS);
        OkHttpHelper.builder.readTimeout(second, TimeUnit.SECONDS);
        OkHttpHelper.builder.writeTimeout(second, TimeUnit.SECONDS);

    }


    OkHttpClient.Builder getOkhttpBuild() {
        return OkHttpHelper.builder;
    }

    //下载
    public OkHttpClient getOkhttpClient(final ProgressResponse.ProgressListener progressListener) {
        return getOkhttpBuild().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponse(response.body(), progressListener)).build();
            }
        }).build();
    }


    //构建Post格式的提交requet
    Request Post() {
        RequestBody body = RequestBody.create(null, "");
        if (url.equals("")) {
            new NullPointerException("请求地址为空，请调用SeUrl()");
        } else {
            Log.i("请求地址", "url=" + url);
        }

        if (netBodyType == NetBodyType.multipart) {
            MultipartBody.Builder builder = new MultipartBody.Builder("AaB03x").setType(MultipartBody.FORM);
            body = builder.build();
            //为数据添加复杂的请求的参数
            for (int i = 0; i < requestMultipartBodyParamList.size(); i++) {
                RequestMultipartBodyParam r = requestMultipartBodyParamList.get(i);
                if (r.mediaContent instanceof byte[]) {
                    body = RequestBody.create(r.mediaType, (byte[]) r.mediaContent);
                } else if (r.mediaContent instanceof File) {
                    body = RequestBody.create(r.mediaType, (File) r.mediaContent);
                } else if (r.mediaContent instanceof String) {
                    body = RequestBody.create(r.mediaType, (String) r.mediaContent);
                } else if (r.mediaContent instanceof ByteString) {
                    body = RequestBody.create(r.mediaType, (ByteString) r.mediaContent);
                }
                if (r.headerName != null) {
                    builder.addPart(Headers.of(r.headerName, r.headerContent), body);
                }
            }
            Log.i("请求参数", "param=" + requestMultipartBodyParamList.toString());
        } else if (netBodyType == NetBodyType.form) {
            //为数据添加复杂的请求的参数
            FormBody.Builder builder = new FormBody.Builder();
            if (requestFormBodyParams != null && requestFormBodyParams.size() != 0) {
                for (int i = 0; i < requestFormBodyParams.size(); i++) {
                    RequestFormBodyParam r = requestFormBodyParams.get(i);
                    builder.add(r.paramName, r.paramContent);
                }
            }
            body = builder.build();
            Log.i("请求参数", "param=" + requestFormBodyParams.toString());
        } else if (netBodyType == NetBodyType.json) {
            body = RequestBody.create(MediaType.get("application/json"), content);
            Log.i("请求参数", "param=" + content);
        }

        return new Request.Builder().url(url).post(body).build();
    }

    //构建GET格式的提交requet
    Request GET() {
        return new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .header("User-Agent", "OkHttp Example")
                .build();
    }


    //设置请求的地址
    public OkHttpManager setUrl(String postUrl) {
        this.url = postUrl;
        return this;
    }

    //设置请求返回监听
    public void setNetBack(Callback netCallBack) {
        this.netCallBack = netCallBack;
        buildNet();
    }

    public void setDownloadBack(File file, ProgressResponse.ProgressListener progressListener) {
        buildDownNet(file, progressListener);
    }


    private void buildDownNet(final File file, final ProgressResponse.ProgressListener progressListener) {
        if (netType == NetType.Post) {
            request = Post();
        } else if (netType == NetType.Post) {
            request = GET();
        } else {
            new NumberFormatException("未设置请求方式");
        }
        Call call = getOkhttpClient(progressListener).newCall(request);
        if (netCallBack != null)//do 设置请求回调
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("下载出错", "错误：" + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        InputStream is = response.body().byteStream();
                        RandomAccessFile savedFile = new RandomAccessFile(file, "rw");
                        savedFile.seek(file.length());//跳过已下载的字节
                        byte[] b = new byte[1024];
                        int total = 0;
                        int len;
                        while ((len = is.read(b)) != -1) {
                            total += len;
                            savedFile.write(b, 0, len);
                        }
                        progressListener.downSuc();
                    } catch (Exception e) {
                        progressListener.err(e);
                    }
                }
            });
    }

    //开始进行请求
    void buildNet() {
        if (netType == NetType.Post) {
            request = Post();
        } else if (netType == NetType.Post) {
            request = GET();
        } else {
            new NumberFormatException("未设置请求方式");
        }
        Call call = getOkhttpClient().newCall(request);
        if (netCallBack != null)//do 设置请求回调
            call.enqueue(netCallBack);
    }



    public OkHttpManager setNetType(NetType netType) {
        this.netType = netType;
        return this;
    }

}
