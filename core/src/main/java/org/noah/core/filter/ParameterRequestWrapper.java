package org.noah.core.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 过滤器包装类  引用 url： http://zhangzhaoaaa.iteye.com/blog/1853787
 * add by xnn on 2016-06-28
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private static final List<String> HTTP_METHODS = Arrays.asList("PUT", "PATCH", "DELETE");
    private final FormHttpMessageConverter formConverter = new AllEncompassingFormHttpMessageConverter();


    private final Map<String , String[]> params = new HashMap<>();

    public ParameterRequestWrapper(HttpServletRequest request) throws IOException {
        // 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
        super(request);
        //将参数表，赋予给当前的Map以便于持有request中的参数
        Map<String, String[]> requestMap;
        if (HTTP_METHODS.contains(request.getMethod())) {
            MultiValueMap<String, String> paramsMap = this.parseIfNecessary(request);
            if(paramsMap != null){
                Set<String> nameSet = new LinkedHashSet<>(paramsMap.keySet());
                Enumeration<String> names = Collections.enumeration(nameSet);
                requestMap = new LinkedHashMap<>();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String[] parameterValues = super.getParameterValues(name);
                    List<String> formParam = paramsMap.get(name);
                    if (formParam == null) {
                        requestMap.put(name, parameterValues);
                    } else if (parameterValues != null && this.getQueryString() != null) {
                        List<String> result = new ArrayList<>(parameterValues.length + formParam.size());
                        result.addAll(Arrays.asList(parameterValues));
                        result.addAll(formParam);
                        requestMap.put(name, StringUtils.toStringArray(result));
                    } else {
                        requestMap.put(name, StringUtils.toStringArray(formParam));
                    }
                }
                this.params.putAll(requestMap);
            }
        }
        this.params.putAll(request.getParameterMap());
        this.modifyParameterValues();
    }

    public Enumeration<String> getParameterNames() {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(Collections.list(super.getParameterNames()));
        names.addAll(this.params.keySet());
        return Collections.enumeration(names);
    }

    public static class MyServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream bis;
        public MyServletInputStream(ByteArrayInputStream bis){
            this.bis=bis;
        }
        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }
        @Override
        public int read(){
            return bis.read();
        }
    }

    /**
     * 重写getInputStream方法  post类型的请求参数必须通过流才能获取到值
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        /* 非json类型，直接返回 */
        if(!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
            return super.getInputStream();
        }
        //为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), StandardCharsets.UTF_8);
        if (StringUtils.isEmpty(json)) {
            return super.getInputStream();
        }
//        System.out.println("去除POST请求数据两端的空格前参数："+json);
        Map<String,Object> map= jsonStringToMap(json);
//        System.out.println("去除POST请求数据两端的空格后参数："+JSON.toJSONString(map));
        ByteArrayInputStream bis = new ByteArrayInputStream(JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        return new MyServletInputStream(bis);
    }

    public static Map<String, Object> jsonStringToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        for (String k : jsonObject.keySet()) {
            Object o = jsonObject.get(k);
            if (o instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (Object obj : (JSONArray) o) {
                    list.add(jsonStringToMap(obj.toString()));
                }
                map.put(k, list);
            } else if (o instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k, jsonStringToMap(o.toString()));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                // map.put(k, o.toString().trim());
                if (o instanceof String) {
                    map.put(k, o.toString().trim());
                } else {
                    map.put(k, o);
                }
            }
        }
        return map;
    }

    /**
     * 将parameter的值去除空格后重写回去
     */
    public void modifyParameterValues(){
        Set<String> set = params.keySet();
        for (String key : set) {
            String[] values = params.get(key);
            values[0] = values[0].trim();
            params.put(key, values);
        }
    }

    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }
    /**
     * 重写getParameterValues
     */
    @Override
    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }


    @Nullable
    private MultiValueMap<String, String> parseIfNecessary(final HttpServletRequest request) throws IOException {
        if (!this.shouldParse(request)) {
            return null;
        } else {
            HttpInputMessage inputMessage = new ServletServerHttpRequest(request) {
                public InputStream getBody() throws IOException {
                    return request.getInputStream();
                }
            };
            return this.formConverter.read(null, inputMessage);
        }
    }

    private boolean shouldParse(HttpServletRequest request) {
        if (!HTTP_METHODS.contains(request.getMethod())) {
            return false;
        } else {
            try {
                MediaType mediaType = MediaType.parseMediaType(request.getContentType());
                return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
            } catch (IllegalArgumentException var3) {
                return false;
            }
        }
    }

}
