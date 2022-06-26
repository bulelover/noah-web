package org.noah.core.filter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.log.pojo.log.SysRequest;
import org.noah.core.log.service.ISysRequestService;
import org.noah.core.satoken.LoginUser;
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.BaseUtils;
import org.noah.core.utils.IDUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet Filter implementation class ParameterFilter
 * 参数去空格过滤器
 */
@Component
public class ParameterFilter implements Filter {

    private final ISysRequestService sysRequestService;

    /**
     * Default constructor.
     */
    public ParameterFilter(ISysRequestService sysRequestService) {
        // Auto-generated constructor stub
        this.sysRequestService = sysRequestService;
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Auto-generated method stub
        // place your code here
        HttpServletRequest hr=(HttpServletRequest) request;

        String url = hr.getServletPath().trim();

        //记录所有访问日志
        this.recordRequestLog(hr);

        //不需要过滤的url，这里可以使用一个配置文件配置这些url，项目启动时读入内存一个map中，然后在这里进行判断
        boolean matched =  "预留".equals(url);
        if (matched){
            //我定义的是urlFilterMap，然后在这里urlFilterMap.containsValue(url)进行判断
            chain.doFilter(hr, response);
        }else{
            ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper((HttpServletRequest)request);
            chain.doFilter(requestWrapper, response);
        }
    }

    private void recordRequestLog(HttpServletRequest request){
        SysRequest log = new SysRequest();
        log.setId(IDUtils.generate());
        log.setParams(this.getParams(request.getParameterMap()));
        log.setUri(request.getServletPath().trim());
        log.setMethod(request.getMethod());
        log.setRemoteAddr(BaseUtils.getRemoteHost(request));
        log.setBrowserName(BaseUtils.getBrowserName(request));
        log.setBrowserVersion(BaseUtils.getBrowserVersion(request));
        log.setOsName(BaseUtils.getOsName(request));
        //这里非web上下文，需要通过request获取
        LoginUser user = TokenUtils.getLoginUser(request);
        if(user != null){
            log.setCreateUserId(user.getId());
            log.setCreateLoginName(user.getLoginName());
            log.setCreateRealName(user.getRealName());
        }else{
            log.setCreateLoginName("--");
            log.setCreateRealName("--");
        }
        log.setCreateTime(LocalDateTime.now());
        //记录所有的请求日志
        this.sysRequestService.createRequestLog(log);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // Auto-generated method stub
    }

    /**
     * 设置请求参数
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getParams(Map paramMap){
        if (paramMap == null){
            return "";
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        return params.toString();
    }

    /**
     * 缩略字符串（不区分中英文字符）
     * @param str 目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (StringUtils.isBlank(html)){
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

}
