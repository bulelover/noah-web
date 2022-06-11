package org.noah.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制器支持类
 * @author noah_xia
 * @version 210819
 */
public abstract class BaseController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());




    //=====================  消息辅助返回快捷方法 ==========================

    protected BaseResult<String> success(){
        return BaseResult.succeed();
    }

    protected BaseResult<String> result(boolean isSuccess){
        return isSuccess?BaseResult.succeed():BaseResult.failure("保存失败");
    }
    protected BaseResult<String> result(boolean isSuccess, String data){
        return isSuccess?BaseResult.succeed("保存成功", data):BaseResult.failure("保存失败");
    }

    protected <T> BaseResult<T> success(T data){
        return BaseResult.succeed(data);
    }

    protected <T> BaseResult<T> success(String msg){
        return BaseResult.succeed(msg);
    }

    protected <T> BaseResult<T> success(String msg, T data){
        return BaseResult.succeed(msg, data);
    }

    protected <T> BaseResult<T> failure(String msg){
        return BaseResult.failure(msg);
    }

    protected <T> BaseResult<T> failure(String msg, T data){
        return BaseResult.failure(msg, data);
    }

}
