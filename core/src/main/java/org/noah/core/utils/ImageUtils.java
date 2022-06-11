package org.noah.core.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {

    /**
     * 判断文件流是否为合法图片
     * @param file 图片文件
     * @return
     */
    public static boolean isImage(File file) {
        return isImage((Object)file);
    }

    /**
     * 判断文件流是否为合法图片
     * @param srcInputStream 图片文件的流
     */
    public static boolean isImage(InputStream srcInputStream) {
        return isImage((Object)srcInputStream);
    }

    /**
     * 判断文件流是否为合法图片
     * @param url 图片地址
     */
    public static boolean isImage(URL url) {
        return isImage((Object)url);
    }

    /**
     * 图片文件读取
     * @param obj (URL|InputStream|File)
     */
    private static boolean isImage(Object obj) {
        Image image = null;
        if (obj != null) {
            try {
                if (obj instanceof URL) {
                    image = ImageIO.read((URL)obj);
                } else if (obj instanceof InputStream) {
                    image = ImageIO.read((InputStream)obj);
                } else if (obj instanceof File) {
                    image = ImageIO.read((File)obj);
                } else {
                    throw new IllegalArgumentException("不支持这种类型["+obj.getClass().getCanonicalName()+"]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image != null && image.getWidth(null) > 0 && image.getHeight(null) > 0;
        }
        return false;
    }
}
