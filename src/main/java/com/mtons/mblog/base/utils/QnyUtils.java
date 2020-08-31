package com.mtons.mblog.base.utils;

import com.google.gson.Gson;
import com.mtons.mblog.base.consts.VariableName;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;

/**
 * 类作用描述：七牛云操作
 **/
public class QnyUtils
{

    private static Logger logger = LoggerFactory.getLogger(QnyUtils.class);

    //  设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY = VariableName.accessKey;

    private static String SECRET_KEY = VariableName.secretKey;

    //  对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开）
    private static String bucketname = VariableName.bucket;

    //  密钥配置
    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    private static Configuration cfg = new Configuration(Zone.huanan());

    //  创建上传对象
    private static UploadManager uploadManager = new UploadManager(cfg);

    //  简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken()
    {
        return auth.uploadToken(bucketname);
    }

    //  上传文件
    public static String uploadFile(MultipartFile file)
    {
        try
        {
            // 获取后缀名
            String originalFilename = file.getOriginalFilename();
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 获取时间戳
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSSss");
            Calendar calendar = Calendar.getInstance();
            String dateName = df.format(calendar.getTime());

            logger.info("fileName:{},new fileName:{}", originalFilename, dateName + suffixName);

            // 开始上传
            Response response = uploadManager.put(file.getInputStream(), dateName + suffixName, getUpToken(), null, null);

            logger.info("response:{}", response);

            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            logger.info("putRet:{}", putRet);

            return putRet.key;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    //  文件下载
    public static void download(String sourceUrl, String fileName, HttpServletResponse response)
    {
        String downloadUrl = auth.privateDownloadUrl(sourceUrl);

        downloadSource(downloadUrl, fileName, response);
    }

    //  请求获取文件资源
    private static void downloadSource(String downloadUrl, String fileName, HttpServletResponse response)
    {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(downloadUrl).build();
        okhttp3.Response resp = null;
        try
        {
            resp = client.newCall(req).execute();
            if (resp.isSuccessful())
            {
                ResponseBody body = resp.body();
                InputStream is = body.byteStream();
                byte[] data = readInputStream(is);

                // 设置强制下载不打开
                response.setContentType("application/force-download");
                // 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                //读取要下载的文件，保存到文件输入流
                FileInputStream in = byteToFile(data, fileName);
                //创建输出流
                OutputStream out = response.getOutputStream();
                //创建缓冲区
                byte buffer[] = new byte[1024];
                int len = 0;
                //循环将输入流中的内容读取到缓冲区当中
                while ((len = in.read(buffer)) > 0)
                {
                    //输出缓冲区的内容到浏览器，实现文件下载
                    out.write(buffer, 0, len);
                }
                //关闭文件输入流
                in.close();
                //关闭输出流
                out.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //  读取字节输入流内容
    private static byte[] readInputStream(InputStream is)
    {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try
        {
            while ((len = is.read(buff)) != -1)
            {
                writer.write(buff, 0, len);
            }
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }

    //  字节转FileInputStream
    public static FileInputStream byteToFile(byte[] bytes, String fileName)
    {
        File file = new File(fileName);
        FileInputStream fileInputStream = null;
        try
        {
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            fileInputStream = new FileInputStream(file);
            file.deleteOnExit();
            return fileInputStream;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return fileInputStream;
    }
}