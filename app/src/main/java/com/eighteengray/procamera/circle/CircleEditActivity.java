package com.eighteengray.procamera.circle;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.eighteengray.procamera.activity.BaseActivity;
import com.eighteengray.procamera.model.network.retrointerface.ICloudAlbum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CircleEditActivity extends BaseActivity
{
    private boolean isEdit = true;
    private boolean isDrag = true;

    Retrofit retrofit;
    ICloudAlbum iCloudAlbum;

    OkHttpClient mOkHttpClient;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Retrofit
        retrofit = new Retrofit.Builder().baseUrl("https://pixabay.com/zh/")
        .addConverterFactory(GsonConverterFactory.create()).build();
        iCloudAlbum = retrofit.create(ICloudAlbum.class);
        Call<String> call = iCloudAlbum.getCloudImageList();
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {

            }
        });

    }


    private void getAsynHttp() {
        mOkHttpClient=new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        okhttp3.Call mcall= mOkHttpClient.newCall(request);
        mcall.enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {

            }
        });

        mcall.cancel();
    }

    private void postAsynHttp() {
        mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("size", "10")
                .build();
        Request request = new Request.Builder()
                .url("http://api.1-blog.com/biz/bizserver/article/list.do")
                .post(formBody)
                .build();
        okhttp3.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {

            }
        });
    }

    private void postAsynFile() {
        mOkHttpClient=new OkHttpClient();
        File file = new File("/sdcard/wangshu.txt");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {

            }
        });
    }

    private void downAsynFile() {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize)).build();
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    Log.i("wangshu", "IOException");
                    e.printStackTrace();
                }
                Log.d("wangshu", "文件下载成功");
            }
        });
    }

    private void sendMultipart(){
        mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "wangshu")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException
            {

            }
        });
    }

}
