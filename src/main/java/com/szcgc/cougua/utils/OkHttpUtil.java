package com.szcgc.cougua.utils;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 走okhttp的四种方式总结
 *
 */
public class OkHttpUtil {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    //参数json转换
    private static MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

    //初始化 6秒超时
    private  static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .build();

    //okhttp异步调用 阻塞获取主线程返回结果
    static class CallBackFutrue extends CompletableFuture<Response> implements Callback{

        @Override
        public void onFailure(Call call, IOException e) {
            super.completeExceptionally(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            super.complete(response);
        }
    }

    /**
     * 同步post
     * @param url
     * @param param
     * @return
     */
    public static int sendPostJsonMsg(String url,String param){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            //初始化参数 如果有的话
            RequestBody body = RequestBody.create(mediaType,param);
            Request.Builder builder = new Request.Builder().url(url).post(body);
            //初始化请求头
            builder.addHeader("Content-Type","application/json");
            //获取请求信息
            Request request = builder.build();
            //同步post方式
            Response response = okHttpClient.newCall(request).execute();
            stopwatch.stop();
            String result = response.body().string();
            System.out.println("同步post返回result"+result+"-耗时"+stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            stopwatch.stop();
            System.out.println("报错e"+e+stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return -1;
        }
        return 1;
    }

    //异步post
    public static int sendAsyncPostJsonMsg(String url,String param){

        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            //初始化参数 如果有的话
            RequestBody body = RequestBody.create(mediaType,param);
            Request.Builder builder = new Request.Builder().url(url).post(body);
            //初始化请求头
            builder.addHeader("Content-Type","application/json");
            //获取请求信息
            Request request = builder.build();
            //异步post方式
            CallBackFutrue callBackFutrue = new CallBackFutrue();
            okHttpClient.newCall(request).enqueue(callBackFutrue);
            Response response = callBackFutrue.get();
            String result = response.body().string();
            stopwatch.stop();
            System.out.println("异步post返回result"+result+"-耗时"+stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            stopwatch.stop();
            System.out.println("报错e"+e+stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return -1;
        }
        return 1;
    }

    /**
     * get获取新的url
     * @param url
     * @param param
     * @return
     */
    private static String getNewUrl(String url,String param){
        Map<String,String> paramMap = new HashMap<>();
        //先考虑json格式的str  考虑有key=value的格式
        if(StringUtils.isNotEmpty(param)){
            //兼容key=val格式的入参
            if(param.contains("{")){
                paramMap = gson.fromJson(param,new TypeToken<Map<String,String>>(){}.getType());
            }else{
                String[] split = param.split("&");
                for (int i = 0; i < param.split("&").length; i++) {
                    paramMap.put(split[i].split("=")[0],split[i].split("=")[1]);
                }
            }
        }
        System.out.println("paramMap"+paramMap);
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if(null != paramMap){
            for (String key:paramMap.keySet()) {
                builder.addQueryParameter(key,paramMap.get(key));
            }
        }
        url = builder.build().toString();
        return url;
    }

    //同步get
    public static int sendGetJsonMsg(String url,String param){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            url = getNewUrl(url,param);
            //初始化参数 如果有的话
//            RequestBody body = RequestBody.create(mediaType,param);
            Request.Builder builder = new Request.Builder().url(url).get();
            //初始化请求头
//            builder.addHeader("Content-Type","application/json");
            //获取请求信息
            Request request = builder.build();
            //同步get方式
            Response response =okHttpClient.newCall(request).execute();
            String result = response.body().string();
            stopwatch.stop();
            System.out.println("同步get返回result"+result+"-耗时"+stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            stopwatch.stop();
            System.out.println("报错e"+e+stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return -1;
        }
        return 1;
    }

    //异步get
    public static int sendAsyncGetJsonMsg(String url,String param){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            url = getNewUrl(url,param);
            //初始化参数 如果有的话
            Request.Builder builder = new Request.Builder().url(url).get();
            //初始化请求头
            //获取请求信息
            Request request = builder.build();
            //异步get方式
            CallBackFutrue callBackFutrue = new CallBackFutrue();
            okHttpClient.newCall(request).enqueue(callBackFutrue);
            Response response = callBackFutrue.get();
            String result = response.body().string();
            stopwatch.stop();
            System.out.println("异步get返回result"+result+"-耗时"+stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            stopwatch.stop();
            System.out.println("报错e"+e+stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return -1;
        }
        return 1;
    }


    public static void main(String[] args) {
        //url必须带http 不然报错
        String url = "127.0.0.1:9003/caffeine/query";
        url = "http://127.0.0.1:9003/caffeine/query";
        String param = "{\"accout\":1}";
//        param="accout=1";
        //同步get
        sendGetJsonMsg(url,param);
        //异步get
//        sendAsyncGetJsonMsg(url,param);
        url = "http://127.0.0.1:9003/caffeine/getList";
        param = "{\"id\":1}";
        //同步post
//        sendPostJsonMsg(url,param);
//        //异步post
//        sendAsyncPostJsonMsg(url,param);

    }
}
