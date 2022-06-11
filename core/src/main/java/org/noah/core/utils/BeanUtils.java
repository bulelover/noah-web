package org.noah.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.noah.core.exception.BusinessException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Bean复制工具类简便调用 封装
 * </p>
 *
 * @author SouthXia
 * @since 2020-07-08
 */
public class BeanUtils {

    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private BeanUtils() {
    }

    /**
     * 利用fastjson将对象转为指定的类型对象
     * @param object 待转的对象
     * @param clazz 指定的对象类
     * @param <T> 泛型 指定的对象类型
     * @return 指定的对象
     */
    public static <T> T parse(Object object, Class<T> clazz){
        return JSON.parseObject(JSON.toJSONString(object),clazz);
    }

    public static <T> List<T> parseList(List list, Class<T> clazz){
        List<T> res = new ArrayList<>();
        for(Object o : list){
            res.add(parse(o, clazz));
        }
        return res;
    }

    /**
     * 将对象转为Map类型
     * @param object 待转对象
     * @return Map集合
     */
    public static Map<String,Object> toMap(Object object){
        return JSON.parseObject(JSON.toJSONString(object)).getInnerMap();
    }


    /**
     * 对象复制
     * @param source 被复制对象，为空会抛出异常
     * @param target 复制的结果，为空会抛出异常
     */
    public static void copyProperties(Object source, Object target) {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(target, "target must not be null");

        BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass());
        beanCopier.copy(source, target, null);
    }

    /**
     * 对象复制
     * @param source 被复制对象，为空会抛出异常
     * @param clazz 复制类型，为空会抛出异常
     * @param <T>
     */
    public static <T> T copyObject(Object source, Class<T> clazz) {
        T result;
        try {
            result = clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("fail to create instance of type" + clazz.getCanonicalName(), e);
        }

        copyProperties(source, result);
        return result;
    }
    /**
     * 复制列表
     *
     * @param list   被复制列表
     * @param classz 复制类型
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> list, Class<T> classz) {
        List<T> resultList = new LinkedList<>();
        if (CollectionUtils.isEmpty(list)) {
            return resultList;
        }
        for (Object obj1 : list) {
            resultList.add(copyObject(obj1, classz));
        }
        return resultList;
    }

    private static BeanCopier getBeanCopier(Class<?> source, Class<?> target) {
        String key = generateKey(source, target);
        return BEAN_COPIER_MAP.computeIfAbsent(key, x -> BeanCopier.create(source, target, false));
    }

    private static String generateKey(Class<?> source, Class<?> target) {
        return source.getCanonicalName().concat(target.getCanonicalName());
    }

    public static Field[] getBeanFields(Class cls, Field[] fs){
        fs = ArrayUtils.addAll(fs, cls.getDeclaredFields());
        if(cls.getSuperclass()!=null){
            Class clsSup = cls.getSuperclass();
            fs = getBeanFields(clsSup,fs);
        }
        return fs;

    }

    /**
     * 根据类反射动态获取UpdateWrapper的Set
     * 但where条件一定需要自己设置 切记！！！
     * @param obj 实体对象
     * @param <T> 实体对象类型
     * @param nullableColumn 设置当值为null的时候，仍然set值的字段属性 集合
     * @return UpdateWrapper
     */
    public static <T> UpdateWrapper<T> getUpdateSetWrapper(T obj, List<String> nullableColumn){
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = null;
        fields = BeanUtils.getBeanFields(obj.getClass(), fields);
        //过滤已经注解的字段 记录日志
        for (Field field : fields) {
            boolean hasAnnotation = field.isAnnotationPresent(TableField.class);
            if (hasAnnotation) {
                // 获取属性的名字
                String name = field.getName();
                Type type = field.getGenericType();
                //关键。。。可访问私有变量
                field.setAccessible(true);
                // 将属性的首字母大写
                String Name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                TableField tf = field.getAnnotation(TableField.class);
                String columnName = tf.value();
                Method m = null;
                try {
                    m = obj.getClass().getMethod("get" + Name);
                    Object o = m.invoke(obj);
                    if(o != null){
                        wrapper.set(columnName,o);
                    }else{
                        //匹配需要更新null值的字段
                        if(nullableColumn != null && nullableColumn.size() >0 && (nullableColumn.contains(name) || nullableColumn.contains(columnName))){
                            wrapper.set(columnName,o);
                        }
                    }
                } catch (Exception e) {
                    throw new BusinessException(e.getMessage());
                }
            }
        }
        return wrapper;
    }

    /**
     * 判断你一个类是否存在某个属性（字段）
     *
     * @param field 字段
     * @param obj   类对象
     * @return true:存在，false:不存在, null:参数不合法
     */
    public static Boolean isExistField(String field, Object obj) {
        if (obj == null || StringUtils.isBlank(field)) {
            return null;
        }
        Object o = JSON.toJSON(obj);
        JSONObject jsonObj = new JSONObject();
        if (o instanceof JSONObject) {
            jsonObj = (JSONObject) o;
        }
        return jsonObj.containsKey(field);
    }

    /**
     * 通过Field动态设置属性值
     * @param t 类对象
     * @param key 要设置的属性
     * @param value 要设置的属性值
     * @param <T> 对象类型
     */
    public static <T> void setValue(T t, String key, Object value){
        if(isExistField(key, t)){
            try {
                Field f = t.getClass().getDeclaredField(key);
                f.setAccessible(true);
                f.set(t, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
            }
        }
    }
    /**
     * 通过Field动态获取属性值
     * @param t 类对象
     * @param key 要设置的属性
     * @param <T> 对象类型
     */
    public static <T> Object getValue(T t, String key){
        if(isExistField(key, t)){
            try {
                Field f = t.getClass().getDeclaredField(key);
                f.setAccessible(true);
                return f.get(t);
            } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过lambada表达式获取属性名称
     */
    public static <T,R> String getPropertyName(SFunction<T,R> function){
        LambdaMeta meta = LambdaUtils.extract(function);
        return StringUtils.removePrefixAfterPrefixToLower(meta.getImplMethodName(),3);
    }

    public static void main(String[] args) {

    }
}
