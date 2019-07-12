package com.ccnu.nrcci.hbnmhmap.HttpService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.conn.ConnectTimeoutException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by wanjunbi on 2017/3/3.
 *
 */

public class HttpUtil {

    /**
     * 用于只用传入一个参数时
     *
     * @param path servlet地址
     * @return
     * @throws Exception
     */
    public String GetRequest(final String path) throws Exception {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                StringBuffer result = new StringBuffer();
                try {
                    String searchPath = path;
                    URL url = new URL(searchPath);
                    HttpURLConnection con = null;
                    con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(5000);
                    //con.setRequestMethod("GET");
                    con.setDoOutput(false);
                    con.setDoInput(true);
                    con.setRequestProperty("Accept-Language", "zh-CN");
                    con.setRequestProperty("Charset", "UTF-8");
                    //con.setRequestProperty("Content-Type","application/json");

                    BufferedReader bufferedReader = null;
                    String str = null;
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((str = bufferedReader.readLine()) != null) {
                        result.append(str);
                    }

                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                    return result.toString();
                } catch (ConnectTimeoutException eTimeOut) {
                    System.out.println("eTimeOut,连接超时");
                } catch (SocketTimeoutException eSocketTimeOut) {
                    System.out.println("eSocketTimeOut,响应超时");
                } catch (FileNotFoundException eFileNotFound) {
                    System.out.println("eFileNotFound,由FileInputStream,FileOutputStream抛出:  " + eFileNotFound.getMessage());
                } catch (IOException eIO) {
                    eIO.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        new Thread(task).start();
        return task.get();
    }

    /**
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
    public String PostRequest(final String path, final Map<String, String> params) throws Exception {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() {
                        StringBuffer result = new StringBuffer();

                        try {

                            URL url = new URL(path);
                            HttpURLConnection conn = null;
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            //conn.setRequestMethod("POST");
                            //
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setRequestProperty("Accept-Language", "zh-CN");
                            conn.setRequestProperty("Charset", "UTF-8");
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.connect();

                            System.out.println("param:" + params.toString());

                            //转化为JSON发送数据
                            ObjectMapper mapper = new ObjectMapper();
                            String request = mapper.writeValueAsString(params);
                            System.out.println("requestJSON:" + request);

                            //map转化为key=value&的格式
                            /*
                            StringBuffer request = new StringBuffer();//!!!!不能=null
                            Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
                            String key = null;String value = null;
                            //for(String key : params.keySet()){
                            while (it.hasNext()){
                                Map.Entry<String,String> entry = it.next();
                                key = entry.getKey();value = entry.getValue();
                                System.out.println("key:"+key+"   value:"+value);
                                request.append(key).append("=").append(value).append("&");
                            }
                            request.deleteCharAt(request.length()-1);
                            System.out.println("request: "+request);*/

                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
                            if (request != null) {
                                outputStreamWriter.write(request);
                                outputStreamWriter.flush();
                            }
                            System.out.println("已发送请求");

                            //获取返回JSON内容
                            BufferedReader bufferedReader = null;
                            InputStream is = conn.getInputStream();
                            bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                            String str = null;
                            while ((str = bufferedReader.readLine()) != null) {
                                result.append(str);
                            }

                            System.out.println("inputstream:" + is.toString());
                            System.out.println("result:" + result.toString());

                            if (outputStreamWriter != null) {
                                outputStreamWriter.close();
                            }

                            if (conn != null) {
                                conn.disconnect();
                                conn = null;
                            }
                            return result.toString();
                        } catch (ConnectTimeoutException eTimeOut) {
                            System.out.println("eTimeOut,连接超时");
                        } catch (SocketTimeoutException eSocketTimeOut) {
                            System.out.println("eSocketTimeOut,响应超时");
                        } catch (FileNotFoundException eFileNotFound) {
                            System.out.println("eFileNotFound:  " + eFileNotFound.getMessage());
                        } catch (IOException eIO) {
                            eIO.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
        );
        new Thread(task).start();
        return task.get();
    }

    public String PostRequest(final String path, final String request) throws Exception {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() {
                        StringBuffer result = new StringBuffer();

                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = null;
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            //conn.setRequestMethod("POST");
                            //
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setRequestProperty("Accept-Language", "zh-CN");
                            conn.setRequestProperty("Charset", "UTF-8");
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.connect();

                            //System.out.println("sql:"+request);

                            //转化为JSON发送数据
                            //ObjectMapper mapper = new ObjectMapper();
                            //String requestJSON = mapper.writeValueAsString(request);
                            System.out.println("requestJSON:" + request);

                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
                            if (request != null) {
                                outputStreamWriter.write(request);
                                outputStreamWriter.flush();
                            }
                            System.out.println("已发送请求");

                            //获取返回JSON内容
                            BufferedReader bufferedReader = null;
                            InputStream is = conn.getInputStream();
                            bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                            String str = null;
                            while ((str = bufferedReader.readLine()) != null) {
                                result.append(str);
                            }

                            System.out.println("inputstream:" + is.toString());
                            System.out.println("result:" + result.toString());

                            if (outputStreamWriter != null) {
                                outputStreamWriter.close();
                            }
                            if (is != null){
                                is.close();
                            }

                            if (conn != null) {
                                conn.disconnect();
                                conn = null;
                            }
                            return result.toString();
                        } catch (ConnectTimeoutException eTimeOut) {
                            System.out.println("eTimeOut,连接超时");
                        } catch (SocketTimeoutException eSocketTimeOut) {
                            System.out.println("eSocketTimeOut,响应超时");
                        } catch (FileNotFoundException eFileNotFound) {
                            System.out.println("eFileNotFound:  " + eFileNotFound.getMessage());
                        } catch (IOException eIO) {
                            eIO.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
        );
        new Thread(task).start();
        return task.get();
    }

    public Bitmap PicRequest(final String path) {
        FutureTask<Bitmap> task = new FutureTask<Bitmap>(
                new Callable<Bitmap>() {
                    @Override
                    public Bitmap call() {
                        Bitmap bitmap = null;
                        try {
                            URL url = new URL(path);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            con.setDoInput(true);
                            con.setDoOutput(false);
                            con.setConnectTimeout(10000);
                            con.setRequestProperty("Accept", "image/gif, image/jpeg, image/jpg, image/pjpeg, ");
                            con.setRequestProperty("Accept-Language", "zh-CN");
                            con.setRequestProperty("Charset", "UTF-8");
                            con.setRequestProperty("Connection", "Keep-Alive");

                            int status = con.getResponseCode();
                            InputStream inputStream;
                            if (status != HttpURLConnection.HTTP_OK) {
                                inputStream = con.getErrorStream();
                            } else {
                                inputStream = con.getInputStream();//
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            }
                            inputStream.close();
                        } catch (IOException eIO) {
                            eIO.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return bitmap;
                    }
                }
        );
        Thread thread = new Thread(task);
        thread.start();

        Bitmap result = null;
        try {
            result = task.get();
        } catch (ExecutionException eExecutuin) {
            //由于中断线程而无法获取结果的异常
            //return the cause of this throwable or null if the cause is nonexistent or unknown
            eExecutuin.getCause();
        } catch (InterruptedException eInterrupted) {
            //线程阻塞
            //中断线程
            thread.interrupt();
        }

        return result;
    }

    public String PicUpload(final String path, final List<File> picsFile) throws Exception {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() {
                        String BOUNDARY = UUID.randomUUID().toString();//随机生成边界标志
                        String PREFIX = "--";
                        String LINE_END = "\r\n";
                        StringBuffer result = new StringBuffer();

                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = null;
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(10 * 1000);

                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);
                            conn.setRequestProperty("connection", "keep-alive");
                            conn.setRequestProperty("Charset", "UTF-8");
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary=" + BOUNDARY);
                            conn.connect();

                            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                            StringBuffer sb = new StringBuffer();

                            Iterator<File> iterator = picsFile.iterator();
                            while (iterator.hasNext()) {

                                //多张照片此处循环
                                File picFile = iterator.next();

                                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                                sb.append("Content-Disposition: form-data; name=\"devfile\"; filename=\"" + picFile.getName() + "\"" + LINE_END);
                                sb.append("Content-Type: image/jpeg" + LINE_END).append(LINE_END);
                                dataOutputStream.write(sb.toString().getBytes());

                                //inputStream读取picFile照片文件内容
                                InputStream is = new FileInputStream(picFile);
                                byte[] bytes = new byte[1024];
                                int len = 0;

                                while ((len = is.read(bytes)) != -1) {
                                    dataOutputStream.write(bytes, 0, len);
                                }
                                is.close();
                            }
                            dataOutputStream.write(LINE_END.getBytes());
                            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                            dataOutputStream.write(end_data);
                            dataOutputStream.flush();
                            System.out.println("已发送请求");

                            //获取返回流
                            if (conn.getResponseCode() < 400) {
                                //获取返回JSON内容
                                BufferedReader bufferedReader = null;

                                InputStream resultStream = conn.getInputStream();
                                bufferedReader = new BufferedReader(new InputStreamReader(resultStream, "utf-8"));
                                String str = null;
                                while ((str = bufferedReader.readLine()) != null) {
                                    result.append(str);
                                }
                            } else {
                                BufferedReader bufferedReader = null;
                                InputStream resultStream = conn.getErrorStream();
                                bufferedReader = new BufferedReader(new InputStreamReader(resultStream, "utf-8"));
                                String str = null;
                                while ((str = bufferedReader.readLine()) != null) {
                                    result.append(str);
                                }
                            }

                            if (dataOutputStream != null) {
                                dataOutputStream.close();
                            }

                            if (conn != null) {
                                conn.disconnect();
                                conn = null;
                            }
                            return result.toString();
                        } catch (ConnectTimeoutException eTimeOut) {
                            System.out.println("eTimeOut,连接超时");
                        } catch (SocketTimeoutException eSocketTimeOut) {
                            System.out.println("eSocketTimeOut,响应超时");
                        } catch (FileNotFoundException eFileNotFound) {
                            System.out.println("eFileNotFound:  " + eFileNotFound.getMessage());
                        } catch (IOException eIO) {
                            eIO.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
        );
        new Thread(task).start();
        return task.get();
    }

}