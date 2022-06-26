package org.noah.core.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseResult;
import org.noah.core.exception.BusinessException;
import org.noah.core.log.pojo.log.SysLog;
import org.noah.core.log.service.ISysLogService;
import org.noah.core.satoken.LoginUser;
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.BaseUtils;
import org.noah.core.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 日志记录自定义注解
 * </p>
 * @author  SouthXia
 * @since 2020-07-08
 */
@Aspect
@Component
public class LogAspect {

    private final ISysLogService sysLogService;

    @Autowired
    public LogAspect(ISysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @Around(value = "@annotation(org.noah.core.annotation.Log) && @annotation(noahLog)",
            argNames = "joinPoint,noahLog")
    public Object doAround(ProceedingJoinPoint joinPoint, Log noahLog) throws Throwable {
        Object obj;
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        long startTime = System.currentTimeMillis();
        Object target = joinPoint.getTarget();
        // 获取参数名称
//        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        SysLog log = new SysLog();

        Log a =  target.getClass().getAnnotation(Log.class);
        log.setId(IDUtils.generate());
        log.setTransName(a==null?noahLog.value():a.value()+"/"+noahLog.value());
        log.setTransType(""+noahLog.type());
        log.setTransFirm(noahLog.firm());
        if(noahLog.type() != 2){
            log.setTransIn(JSON.toJSONString(joinPoint.getArgs(), SerializerFeature.WriteMapNullValue));
        }
        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {
            StringBuilder errorStack = new StringBuilder();
            if(e.getStackTrace() != null && e.getStackTrace().length>0){
                for(int i=0; i<10; i++){
                    errorStack.append(" >>").append(e.getStackTrace()[i]);
                }
//                e.printStackTrace();
            }
            //如果是自定义异常，直接抛出，不记日志
            if(BusinessException.class.getSimpleName().equals(e.getClass().getSimpleName())){
                throw e;
            }
            //记录相关异常日志
            BaseResult<String> result = BaseResult.failure(e.getMessage() == null? e +";"+ errorStack
                    : e.getMessage()+";"+ errorStack);
            // 获取执行的方法名
            long endTime = System.currentTimeMillis();
            // 打印耗时的信息
            long diffTime = endTime - startTime;
            log.setTransTimes(diffTime);
            log.setTransOut(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            //错误日志
            log.setTransLevel(9);
            setOperator(log);
            sysLogService.createLog(log);
            throw e;
        }
        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        // 打印耗时的信息
        long diffTime = endTime - startTime;
        log.setTransTimes(diffTime);
        log.setTransOut(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue));
        setOperator(log);
        sysLogService.createLog(log);
        return obj;
    }

    private void setOperator(SysLog log){
        log.setTransIp(BaseUtils.getRemoteHost());
        log.setCreateTime(LocalDateTime.now());
        LoginUser user = TokenUtils.getLoginUser();
        if(user != null){
            log.setCreateUserId(user.getId());
            log.setCreateLoginName(user.getLoginName());
            log.setCreateRealName(user.getRealName());
        }else{
            log.setCreateLoginName("--");
            log.setCreateRealName("--");
        }
    }

    private static Map getFieldsName(JoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException {
        String classType = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 参数值
        Object[] args = joinPoint.getArgs();
        Class<?>[] classes = new Class[args.length];
        for (int k = 0; k < args.length; k++) {
            if (!args[k].getClass().isPrimitive()) {
                // 获取的是封装类型而不是基础类型
                String result = args[k].getClass().getName();
                if(result.contains(".entity.query")){
                    classes[k] = Object.class;
                }else {
                    Class s = map.get(result);
                    classes[k] = s == null ? args[k].getClass() : s;
                }
            }
        }
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
        Method method = Class.forName(classType).getMethod(methodName, classes);
//        Method method = Class.forName(classType).getMethod(methodName);
        // 参数名
        String[] parameterNames = pnd.getParameterNames(method);
        // 通过map封装参数和参数值
        HashMap<String, Object> paramMap = new HashMap<>();
        if(parameterNames == null){
            return paramMap;
        }
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }

    private static HashMap<String, Class> map = new HashMap<String, Class>() {
        {
            put("java.lang.Integer", int.class);
            put("java.lang.Double", double.class);
            put("java.lang.Float", float.class);
            put("java.lang.Long", long.class);
            put("java.lang.Short", short.class);
            put("java.lang.Boolean", boolean.class);
            put("java.lang.Char", char.class);
        }
    };
}
