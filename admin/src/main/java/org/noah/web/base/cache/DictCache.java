package org.noah.web.base.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.utils.CacheUtils;
import org.noah.web.sys.pojo.dict.SysDictItemVO;

import java.util.List;
import java.util.Map;

public class DictCache {
    private static final String CACHE_NAME= "dictCache";
    private static final String DICT_ID_CODE= "dictIdCode";
    private static final String DICT_ALL= "DICT_ALL";

    public static List<SysDictItemVO> getItemList(String dictId){
        String res = CacheUtils.get(CACHE_NAME, dictId);
        if(StringUtils.isBlank(res)){
            return null;
        }
        return JSON.parseArray(res, SysDictItemVO.class);
    }

    public static void putItemList(String dictId, String value){
        CacheUtils.put(CACHE_NAME, dictId, value);
    }
    public static void removeItemList(String dictId){
        CacheUtils.remove(CACHE_NAME, dictId);
    }

    public static Map<String, Object> getAllDict(){
        String res = CacheUtils.get(CACHE_NAME, DICT_ALL);
        if(StringUtils.isBlank(res)){
            return null;
        }
        return JSON.parseObject(res);
    }

    public static void putAllDict(String value){
        CacheUtils.put(CACHE_NAME, DICT_ALL, value);
    }
    public static void removeAllDict(){
        CacheUtils.remove(CACHE_NAME, DICT_ALL);
    }


    public static String getDictIdByCode(String code){
        String res = CacheUtils.get(CACHE_NAME, DICT_ID_CODE);
        if(StringUtils.isBlank(res)){
            return null;
        }
        JSONObject json = JSON.parseObject(res);
        return json.getString(code);
    }

    public static void putDictIdByCode(String code, String dictId){
        String res = CacheUtils.get(CACHE_NAME, DICT_ID_CODE);
        JSONObject json;
        if(!StringUtils.isBlank(res)){
            json = JSON.parseObject(res);
        }else {
            json = new JSONObject();
        }
        json.put(code, dictId);
        CacheUtils.put(CACHE_NAME, DICT_ID_CODE, json.toJSONString());
    }

    public static void removeDictIdByCode(String code){
        String res = CacheUtils.get(CACHE_NAME, DICT_ID_CODE);
        JSONObject json;
        if(StringUtils.isBlank(res)){
           return;
        }
        json = JSON.parseObject(res);
        json.put(code,null);
        CacheUtils.put(CACHE_NAME, DICT_ID_CODE, json.toJSONString());
    }

}
