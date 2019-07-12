package com.ccnu.nrcci.hbnmhmap.HttpService;

import android.os.Looper;
import android.support.annotation.Nullable;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 用于向服务器发送请求，并获取返回结果
 * Created by xinjay on 2017/9/8.
 */

public class OkHttpUtil {

    private final static OkHttpClient myOkHttpClient = new OkHttpClient();
    private final static MediaType defaultMediaType = MediaType.parse("application/json; charset=utf-8");

    /**
     * 发送get请求
     *
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public String getRequest(final String url) throws Exception {
        return getRequest(url, String.class);
    }

    /**
     * 发送get请求的泛型版本
     *
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    @SuppressWarnings(value = {"unchecked"})
    public <T> T getRequest(final String url, @Nullable Class<T> c) throws Exception {
        FutureTask<byte[]> task = new FutureTask<>(
                new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        //构建请求
                        Request request = new Request.Builder().url(url).build();
                        //绑定请求
                        Call call = myOkHttpClient.newCall(request);
                        //执行请求，并返回响应结果
                        Response response = call.execute();
                        if (response.code() == 200) {
                            byte[] result = response.body().bytes();
                            return result;
                        }
                        return null;
                    }
                }
        );
        new Thread(task).start();
        T result = null;
        if (c == null)
            result = (T) new BytesReaderUtil().getObject(task.get(), String.class);
        else
            result = (T) new BytesReaderUtil().getObject(task.get(), c);
        return result;
    }


    /**
     * 发送post请求
     *
     * @param url 发送请求的URL
     * @param map 请求参数，为字典类型
     * @return 服务器响应字符串
     * @throws Exception
     */
    public String postRequest(final String url, final Map<String, Object> map, @Nullable final MediaType mediaType) throws Exception {
        return postRequest(url, map, mediaType, String.class);
    }

    /**
     * 发送post请求的泛型版本
     *
     * @param url 发送请求的URL
     * @param map 请求参数，为字典类型
     * @param c   欲转换到的类型，默认为String
     * @return 返回指定类型T的对象
     * @throws Exception
     */
    @SuppressWarnings(value = {"unchecked"})
    public <T> T postRequest(final String url, final Map<String, Object> map, @Nullable final MediaType mediaType, @Nullable Class<T> c) throws Exception {
        FutureTask<byte[]> task = new FutureTask<byte[]>(
                new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        OkHttpClient client = myOkHttpClient.newBuilder()
                                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                                .build();
                        ObjectMapper objectMapper = new ObjectMapper();


                        //构建请求体
                        RequestBody body;
                        if (mediaType == null) {
                            //将map封装为json字符串

                            String json = getJsonValue(objectMapper, map);
                            body = RequestBody.create(defaultMediaType, json);
                        } else if (mediaType.equals(MediaType.parse("multipart/form-data"))) {
                            String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gWaaSaswd";
                            MediaType meType = MediaType.parse("multipart/form-data; boundary=" + boundary);
                            // String data = ("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"files\"\r\n\r\n[{\"name\":\"43b6c1c8-6caf-4732-8d49-19ce0e5460af\"},{\"byname\":\"1508412864818.jpg\"},{\"extname\":\".jpg\"},{\"type\":\"图片\"},{\"size\":\"4183\"},{\"path\":\"http://58.49.122.250:8002/group1/M00/00/01/wKgTyFnojYyAUC3dAEFczgNOgx0120.jpg\"}]\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"orgid\"\r\n\r\n00000000-0000-0000-0000-000000000000\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"fkder\"\r\n\r\nadmin\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"content\"\r\n\r\nDJ\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"attid\"\r\n\r\nd72ebd39-4e71-4303-8bb9-18cfea7cabef\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"remark\"\r\n\r\n12154\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"cautionid\"\r\n\r\n7\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
                            String form = getFormData(map, boundary);
                            body = RequestBody.create(meType, form);
                        } else {
                            throw new Exception("不支持的MediaType");
                        }
                        //构建请求
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();

                        //绑定请求
                        Call call = client.newCall(request);
                        try {
                            //执行请求，并返回响应结果
                            Response response = call.execute();
                            if (response.code() == 200) {
                                // 获取服务器响应字节数组
                                byte[] result = response.body().bytes();
                                return result;
                            }
                        } catch (IllegalArgumentException e1) {
                            // TODO Auto-generated catch block
                            System.out.println("#######" + "连接超时");
                            Looper.prepare();
                        } catch (SocketTimeoutException e2) {
                            System.out.println("######" + "响应超时");
                            Looper.prepare();
                        } catch (Exception e3) {
                            System.out.println("######" + "超时");
                            Looper.prepare();
                        }
                        return null;
                    }
                });
        new

                Thread(task).

                start();

        T result = null;
        if (c == null)
            result = (T) new

                    BytesReaderUtil().

                    getObject(task.get(), String.class);
        else
            result = (T) new

                    BytesReaderUtil().

                    getObject(task.get(), c);
        return result;
    }


    public String postRequest(final String url, final String params) throws Exception {
        return postRequest(url, params, defaultMediaType, String.class);
    }

    /**
     * 发送post请求的泛型版本
     *
     * @param url       发送请求的URL
     * @param params    发送的请求参数
     * @param mediaType 参数的类型
     * @param c         欲转换到的类型，默认为String
     * @return 返回指定类型T的对象
     * @throws Exception
     */
    @SuppressWarnings(value = {"unchecked"})
    public <T> T postRequest(final String url, final Object params, @Nullable final MediaType mediaType, @Nullable final Class<T> c) throws Exception {
        FutureTask<byte[]> task = new FutureTask<byte[]>(
                new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        OkHttpClient client = myOkHttpClient.newBuilder()
                                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                                .build();

                        RequestBody body;
                        //构建请求体
                        if (c.equals(String.class))
                            body = RequestBody.create(mediaType, params.toString());
                        else
                            body = RequestBody.create(mediaType, getBytes(params));

                        //构建请求
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();

                        //绑定请求
                        Call call = client.newCall(request);
                        try {
                            //执行请求，并返回响应结果
                            Response response = call.execute();
                            if (response.code() == 200) {
                                // 获取服务器响应字节数组
                                byte[] result = response.body().bytes();
                                return result;
                            }
                        } catch (IllegalArgumentException e1) {
                            // TODO Auto-generated catch block
                            System.out.println("#######" + "连接超时");
                            Looper.prepare();
                        } catch (SocketTimeoutException e2) {
                            System.out.println("######" + "响应超时");
                            Looper.prepare();
                        } catch (Exception e3) {
                            System.out.println("######" + "超时");
                            Looper.prepare();
                        }
                        return null;
                    }
                });
        new Thread(task).start();
        T result = null;
        if (c == null)
            result = (T) new BytesReaderUtil().getObject(task.get(), String.class);
        else
            result = (T) new BytesReaderUtil().getObject(task.get(), c);
        System.out.println(result);
        return result;
    }

    /**
     * 构建Json请求字符串，与Content-Type:application/json; charset=utf-8相对应
     *
     * @param objectMapper 用于将请求参数封装为Json字符串
     * @param object       欲封装为json字符串的对象
     * @return 返回Json字符串
     */
    public static String getJsonValue(ObjectMapper objectMapper, Object object) {
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            objectMapper.writeValue(writer, object);
            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("JSON 格式化出错", e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将Object对象转换为二进制数组
     *
     * @param object 需要转换的对象
     * @return 返回转换后的二进制数组
     */
    private static byte[] getBytes(Object object) {
        ByteArrayOutputStream byt = new ByteArrayOutputStream();
        try {
            ObjectOutputStream obj = new ObjectOutputStream(byt);
            obj.writeObject(object);
            return byt.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Map<String,Object>对象转换为form-data结构字符串
     *
     * @param map 需要转换的字典
     * @return 返回转换后的字符串
     */
    private static String getFormData(Map<String, Object> map, String bountry) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Object> item : map.entrySet()) {
            //builder.append("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n");
            builder.append("--" + bountry + "\r\n");
            builder.append("Content-Disposition: form-data; name=\"");
            builder.append(item.getKey() + "\"\r\n\r\n" + item.getValue());
            builder.append("\r\n");
        }
        builder.append("--" + bountry+"--");
        String result = builder.toString();
        return result;
    }
}
