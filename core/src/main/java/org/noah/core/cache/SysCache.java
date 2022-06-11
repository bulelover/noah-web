package org.noah.core.cache;

import org.noah.core.utils.CacheUtils;

public class SysCache {
    private static final String CACHE_NAME= "sysCache";

    public static String get(String key) {
        return CacheUtils.get(CACHE_NAME,key);
    }

    public static void put(String key, String value) {
        CacheUtils.put(CACHE_NAME, key, value);
    }

    public static void remove(String key) {
        CacheUtils.remove(CACHE_NAME, key);
    }

    public static void clear(){
        CacheUtils.clear(CACHE_NAME);
    }
}
