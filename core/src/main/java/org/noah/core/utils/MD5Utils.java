package org.noah.core.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class MD5Utils {

    /**
     * MD5带盐加密
     * @param data 待加密数据
     * @param salt 盐值
     * @return 加密字符串
     */
    public static String encodeSalt(String data, String salt){
        assert salt != null;
        StringBuilder sb = new StringBuilder(salt);
        return encode(sb.reverse() +encode(data)+salt);
    }

    /**
     * MD5加密
     * @param data 待加密数据
     * @return 已加密字符串
     */
    public static String encode(String data) {
        return encode(data, "UTF-8");
    }

    /**
     * MD5加密
     * @param data 待加密数据
     * @return 已加密字符串
     */
    public static String encode(String data, String charset) {
        // 执行消息摘要
        try {
            return DigestUtils.md5DigestAsHex(data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encodeSalt("admin","admin"));
    }
}
