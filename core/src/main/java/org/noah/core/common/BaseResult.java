package org.noah.core.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResult<T> implements Serializable {
    private static final String SUCCESS_MSG="操作成功";

    @ApiModelProperty(value = "成功标志 1成功 0失败", position = 10)
    private Integer code;
    @ApiModelProperty(value = "操作消息", position = 20)
    private String msg;

    @ApiModelProperty(value = "成功标识 true/false", position = 30)
    public boolean isSuccess(){
        if(this.getCode() == null) {
            return false;
        }
        return this.getCode() == 1;
    }

    @ApiModelProperty(value = "返回数据对象", position = 40)
    private T data;

    /**
     * 将结果成功失败的bool值传入自动返回成功或失败的信息
     * @param isSuccess 是否成功
     * @param errorMsg 错误消息
     * @return BaseResult
     */
    public static BaseResult<String> auto(boolean isSuccess, String errorMsg){
        return isSuccess?succeed():failure(errorMsg);
    }

    /**
     * 将结果成功失败的bool值传入自动返回成功或失败的信息
     * @param isSuccess 是否成功
     * @param errorMsg 错误消息
     * @return BaseResult
     */
    public static <T> BaseResult<T> auto(boolean isSuccess, T data, String errorMsg){
        return isSuccess?succeed(data):failure(errorMsg);
    }


    /**
     * 成功消息
     * @param data 返回结果
     * @return BaseResultVo
     */
    public static <T> BaseResult<T> succeed(T data){
        return succeed(SUCCESS_MSG,data);
    }

    /**
     * 成功消息
     * @param data 返回结果
     * @return BaseResultVo
     */
    public static <T> BaseResult<T> succeed(String msg, T data){
        return custom(1,msg,data);
    }

    /**
     * 成功消息
     * @param msg 返回消息
     * @return BaseResultVo
     */
    public static <T> BaseResult<T> succeed(String msg){
        return custom(1,msg, null);
    }
    /**
     * 成功消息 无参返回
     * @return BaseResultVo
     */
    public static <T> BaseResult<T> succeed(){
        return custom(1,SUCCESS_MSG, null);
    }

    /**
     * 失败消息
     * @param errorMsg 返回错误消息
     * @return BaseResultVo
     */
    public static <T> BaseResult<T> failure(String errorMsg){
        return failure(errorMsg,null);
    }

    /**
     * 失败消息
     * @param errorMsg 返回错误消息
     * @return BaseResultVo
     */
    public static <T> BaseResult<T> failure(String errorMsg, T data){
        return custom(0,errorMsg,data);
    }

    private static <T> BaseResult<T> custom(int code, String msg, T data){
        BaseResult<T> vo = new BaseResult<>();
        vo.setCode(code);
        vo.setMsg(msg);
        vo.setData(data);
        return vo;
    }

    public static void main(String[] args) {

    }
}
