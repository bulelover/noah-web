package org.noah.file.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>上传文件工具类</p>
 *
 * @author wangheli
 * @version 1.0.0
 * @since 2020/9/13 19:32
 */
@Component
public class FileUtils {

    protected static String uploadPath;// 上传文件路径

    private FileUtils() {
    }

    @Value("${config.basedir}")
    public void setUploadPath(String uploadPath) {
        FileUtils.uploadPath = uploadPath;
    }

    /**
     * <p>依据后缀名获取文件目录</p>
     *
     * @return 文件上传地址（默认other）  java.lang.String
     */
    public static String createPath() {
        if(uploadPath.length()>2 && uploadPath.indexOf(":") != 1){
            String relativelyPath=System.getProperty("user.dir");
            return relativelyPath.substring(0,2) + uploadPath;
        }
        return uploadPath;
    }


    /**
     * <p>获取文件后缀名</p>
     *
     * @param fileName 文件名
     * @return
     * @author wangheli
     * @since 2020/11/19 19:39
     */
    public static String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    /**
     * <p>下载文件</p>
     *
     * @param buffer   文件流字节
     * @param fileName 文件名
     * @param response 请求响应体
     * @return
     * @author wangheli
     * @since 2020/11/19 20:01
     */
    public static void download(byte[] buffer, String fileName, String fileType, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType(fileType);
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),
                StandardCharsets.ISO_8859_1));
        response.addHeader("Content-Length", "" + buffer.length);
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    /**
     * <p>预览文件</p>
     *
     * @param buffer   文件流字节
     * @param fileName 文件名
     * @param response 请求响应体
     * @return
     * @author wangheli
     * @since 2020/11/19 20:01
     */
    public static void preview(byte[] buffer, String fileName, String fileType, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType(fileType);
        response.addHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(),
                StandardCharsets.ISO_8859_1));
        response.addHeader("Content-Length", "" + buffer.length);
        //将图片缓存起来
        response.addHeader("Cache-Control","max-age=604800");
        OutputStream toClient = response.getOutputStream();
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

}
