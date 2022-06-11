package org.noah.core.utils;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CacheUtils {

    @Autowired
    private CacheChannel cacheChannel;

    private static CacheUtils utils;

    @PostConstruct
    public void init() {
        utils = this;
        utils.cacheChannel = this.cacheChannel;
    }


    public static void put(String cacheName, String key, String value) {
        utils.cacheChannel.set(cacheName, key, value);
    }


    public static void clear(String cacheName){
        utils.cacheChannel.clear(cacheName);
    }


/*    public static String queryCache() {
        List<String> list = new ArrayList<>();
        // 2.8.2 版本 keys有bug会陷入死循环 改为2.7.7
        Collection<String> keys = utils.cacheChannel.keys(SYS_CACHE);
        for(String key : keys){
            list.add("key:"+key+" => value:"+get(SYS_CACHE, key));
        }
        return list.toString();
    }*/


    public static String get(String cacheName, String key) {
        CacheObject obj = utils.cacheChannel.get(cacheName,key);
        if(obj.getValue() != null){
            return obj.asString();
        }
        return null;
    }

    public static void remove(String cacheName,String key) {
        utils.cacheChannel.evict(cacheName, key);
    }

}
