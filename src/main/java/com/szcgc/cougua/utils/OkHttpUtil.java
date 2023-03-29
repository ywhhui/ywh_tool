package com.szcgc.cougua.utils;

import com.google.common.base.Stopwatch;
import com.google.gson.reflect.TypeToken;
import com.szcgc.cougua.constant.HttpMethodType;
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
     * get获取新的url
     * @param url
     * @param param
     * @return
     */
    private static String getNewUrl(String url,String param){
        if(StringUtils.isEmpty(param)){
            return url;
        }
        //java里面所有的new 都是占用内存的 所以当需要时候再new
        Map<String,String> paramMap = new HashMap<>();
        //先考虑json格式的str  考虑有key=value的格式
        //兼容key=val格式的入参
        if(param.contains("{")){
            //json需要指定具体的对象 new TypeToken<Map<String,String>>(){}.getType()
            paramMap = GsonUtils.gson.fromJson(param,new TypeToken<Map<String,String>>(){}.getType());
        }else{
            //注意点 split方法是有内存消耗的
            String[] paramArray = param.split("&");
            for (int i = 0; i <paramArray.length; i++) {
                String[] paramArr = paramArray[i].split("=");
                //防止key=null的情况
                if(paramArr.length !=2){
                    continue;
                }
                paramMap.put(paramArr[0],paramArr[1]);
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



    /**
     * 公共的http请求
     * @param url
     * @param param get的reqParam 和post的body
     * @param httpMethodType
     */
    public static void sendHttpRequest(String url, String param, HttpMethodType httpMethodType){
        Stopwatch stopwatch = Stopwatch.createStarted();
        Response response =null;
        try {
            if(httpMethodType.equals(HttpMethodType.SYNCGET)){
                url = getNewUrl(url,param);
                //初始化参数 如果有的话
                Request.Builder builder = new Request.Builder().url(url).get();
                //初始化请求头
//            builder.addHeader("Content-Type","application/json");
                //获取请求信息
                Request request = builder.build();
                //同步get方式
                 response =okHttpClient.newCall(request).execute();
            }else if(httpMethodType.equals(HttpMethodType.ASYNCGET)){
                url = getNewUrl(url,param);
                //初始化参数 如果有的话
                Request.Builder builder = new Request.Builder().url(url).get();
                //初始化请求头
                //获取请求信息
                Request request = builder.build();
                //异步get方式
                CallBackFutrue callBackFutrue = new CallBackFutrue();
                okHttpClient.newCall(request).enqueue(callBackFutrue);
                response = callBackFutrue.get();
            }else if(httpMethodType.equals(HttpMethodType.SYNCPOST)){
                //初始化参数 如果有的话
                RequestBody body = RequestBody.create(mediaType,param);
                Request.Builder builder = new Request.Builder().url(url).post(body);
                //初始化请求头
                builder.addHeader("Content-Type","application/json");
                //获取请求信息
                Request request = builder.build();
                //同步post方式
                 response = okHttpClient.newCall(request).execute();
            }else if(httpMethodType.equals(HttpMethodType.ASYNCPOST)){
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
                response = callBackFutrue.get();
            }
            String result = response.body().string();
            stopwatch.stop();
            System.out.println("返回result"+result+"-耗时"+stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            stopwatch.stop();
            System.out.println("报错e"+e+stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }

    }

    public static void main(String[] args) {
        //url必须带http 不然报错
        String url = "127.0.0.1:9003/caffeine/query";
        url = "http://127.0.0.1:9003/caffeine/query";
        String param = "{\"accout\":1}";
//        param="accout=1";
        //同步get
        sendHttpRequest(url,param,HttpMethodType.SYNCGET);
        //异步get
//         sendHttpRequest(url,param,HttpMethodType.ASYNCGET);
        url = "http://127.0.0.1:9003/caffeine/getList";
        param = "{\"id\":1}";
        //同步post
//        sendHttpRequest(url,param,HttpMethodType.SYNCPOST);
//        //异步post
//        sendHttpRequest(url,param,HttpMethodType.ASYNCPOST);

    }
}
