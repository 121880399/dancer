package com.shx.dancer.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.shx.dancer.BaseApplication;
import com.shx.dancer.common.LogGloble;
import com.shx.dancer.common.SystemConfig;
import com.shx.dancer.dialog.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by 邵鸿轩 on 2017/7/4.
 */

public class HttpManager {
    private static HttpManager mHttpManager;
    private String TAG = "httplog";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String BASE_URL = "http://xxx.com/openapi";//请求接口根地址
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    private HttpManager() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor(TAG))
                .connectTimeout(3 * 10000L, TimeUnit.MILLISECONDS)
                .readTimeout(3 * 10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);


    }

    public static HttpManager getInstance() {
        if (mHttpManager == null) {
            synchronized (HttpManager.class) {
                mHttpManager = new HttpManager();

            }
        }
        return mHttpManager;
    }

    public void doDownloadFile(String url, String fileName, FileCallBack fileCallBack) {
        OkHttpUtils.get().tag("download").url(url).build().execute(fileCallBack);
    }
//    /**
//     *  okHttp同步请求统一入口
//     * @param actionUrl  接口地址
//     * @param requestType 请求类型
//     */
//    public void requestSyn( ZCRequest request) {
//        switch (request.getRequestType()) {
//            case TYPE_GET:
//                requestGetBySyn(request);
//                break;
//            case TYPE_POST_JSON:
//                requestPostBySyn(request);
//                break;
//            case TYPE_POST_FORM:
//                requestPostBySynWithForm(request);
//                break;
//        }
//    }
//
    public void doPost(final ZCRequest request, final HttpCallBack callBack) {
        if (!isNetworkAvailable(BaseApplication.getContext())) {
            ToastUtil.getInstance().toastInCenter(BaseApplication.getContext(), "网络异常");
            return;
        }
        String url = SystemConfig.BASEURL + request.getUrl();

        String requestStr = MyJSON.toJSONString(request.getParams());
        LogGloble.d("http", requestStr);
        PostFormBuilder builder=OkHttpUtils
                .post()
                .url(url);
        for (String key : request.getParams().keySet()) {
            //userid+字段名称用base64编码+.jpg
//            String filename= new String(Base64.encode((UserInfo.getUserInfoInstance().getId()+key).getBytes(),Base64.DEFAULT))+".jpg";
            builder.addParams(key,request.getParams().get(key));
        }
        builder .tag(this)
                .build()
                .execute(new Callback() {
                    @NonNull
                    @Override
                    public Object parseNetworkResponse(@NonNull okhttp3.Response response, int id) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, @NonNull Exception e, int id) {
                        HttpTrowable httpTrowable = new HttpTrowable(e.getMessage(), "999999");
                        callBack.doFaild(httpTrowable, request.getUrl());
                    }

                    @Override
                    public void onResponse(Object result, int id) {
                        LogGloble.d("http：", "url--" + request.getUrl() + "--result--" + result);
                        ZCResponse response = null;
                        try {
                            response = MyJSON.parseObject((String) result, ZCResponse.class);
                            if (!httpCalllBackPreFilter(response, request.getUrl())) {
                                callBack.doSuccess(response, request.getUrl());
                            } else {
                                HttpTrowable httpTrowable = new HttpTrowable(response.getMessage(), response.getMessageCode());
                                callBack.doFaild(httpTrowable, request.getUrl());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HttpTrowable httpTrowable = new HttpTrowable(e.getMessage(), "99999");
                            callBack.doFaild(httpTrowable, request.getUrl());
                        }
                    }
                });
    }

    public void doGet(final ZCRequest request, final HttpCallBack callBack) {
//        if (!isNetworkAvailable(BaseApplication.getContext())) {
//            ToastUtil.getInstance().toastInCenter(BaseApplication.getContext(), "网络异常");
//            return;
//        }
//        String url = SystemConfig.BASEURL + request.getUrl();
//
//        String requestStr = MyJSON.toJSONString(request.getParams());
//        LogGloble.d("http", requestStr);
//        OkHttpUtils
//                .get()
//                .url(url)
//                .params(request.getParams())
//                .tag(this)
//                .build()
//                .execute(new Callback() {
//                    @NonNull
//                    @Override
//                    public Object parseNetworkResponse(@NonNull okhttp3.Response response, int id) throws Exception {
//                        return response.body().string();
//                    }
//
//                    @Override
//                    public void onError(Call call, @NonNull Exception e, int id) {
//                        HttpTrowable httpTrowable = new HttpTrowable(e.getMessage(), "999999");
//                        callBack.doFaild(httpTrowable, request.getUrl());
//                    }
//
//                    @Override
//                    public void onResponse(Object result, int id) {
//                        LogGloble.d("http：", "url--" + request.getUrl() + "--result--" + result);
//                        ZCResponse response = null;
//                        try {
//                            response = MyJSON.parseObject((String) result, ZCResponse.class);
//                            if (!httpCalllBackPreFilter(response, request.getUrl())) {
//                                callBack.doSuccess(response, request.getUrl());
//                            } else {
//                                HttpTrowable httpTrowable = new HttpTrowable(response.getMessage(), response.getMessageCode());
//                                callBack.doFaild(httpTrowable, request.getUrl());
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            HttpTrowable httpTrowable = new HttpTrowable(e.getMessage(), "99999");
//                            callBack.doFaild(httpTrowable, request.getUrl());
//                        }
//                    }
//                });
    }
    /**
     * 统一过滤
     *
     * @param response
     * @param method
     * @return
     */
    public static boolean httpCalllBackPreFilter(@NonNull ZCResponse response, String method) {
        if (response.getMessageCode().equals("10000")) {
            return false;
        }
        ToastUtil.getInstance().toastInCenter(BaseApplication.getContext(), response.getMessage());
        return true;
    }

    public void doCancleDownloadFile() {
        OkHttpUtils.getInstance().cancelTag("download");
    }

    // 检测网络
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 文件上传的接口请求
     */
    public void upLoadOneFile(final ZCRequest request, final Map<String, File> fileMap, final HttpCallBack callBack) {
        if (!isNetworkAvailable(BaseApplication.getContext())) {
            ToastUtil.getInstance().toastInCenter(BaseApplication.getContext(), "网络异常");
            return;
        }
        String url = SystemConfig.BASEURL+request.getUrl();
        String requestStr = MyJSON.toJSONString(request.getParams());
        LogGloble.d("http",requestStr);
        // 使用multipart表单上传文件
        PostFormBuilder builder= OkHttpUtils.post();
        for (String key : fileMap.keySet()) {
            //userid+字段名称用base64编码+.jpg
//            String filename= new String(Base64.encode((UserInfo.getUserInfoInstance().getId()+key).getBytes(),Base64.DEFAULT))+".jpg";
            String filename= "avatar"+".jpg";
            builder.addFile(key,filename,fileMap.get(key));
        }
        builder.url(url)
                .addHeader("charset","utf-8")
                .addParams("data",requestStr)
                .build()//
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ToastUtil.getInstance().toastInCenter((Context) callBack,"服务器异常");
                        HttpTrowable httpTrowable=new HttpTrowable(e.getMessage(),"99999");
                        callBack.doFaild(httpTrowable, request.getUrl());
                    }

                    @Override
                    public void onResponse(Object result, int id) {
                        LogGloble.d("http：", "url--" + request.getUrl()  + "--result--" + result);
                        ZCResponse response = null;
                        try {
                            response = MyJSON.parseObject((String) result, ZCResponse.class);
                            if (!httpCalllBackPreFilter(response, request.getUrl())) {
                                callBack.doSuccess(response, request.getUrl());
                            }else{
                                HttpTrowable httpTrowable=new HttpTrowable(response.getMessage(),response.getMessageCode());
                                callBack.doFaild(httpTrowable,request.getUrl());
                            }
                            ToastUtil.getInstance().toastInCenter((Context) callBack,response.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogGloble.d("http",e.getMessage());
                        }
                    }
                });


    }
}
