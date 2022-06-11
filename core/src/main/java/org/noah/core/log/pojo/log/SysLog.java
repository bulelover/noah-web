package org.noah.core.log.pojo.log;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author Noah
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLog implements Serializable {


    /**
     * 主键
     */
    private String id;

    /**
     * 交易摘要
     */
    @TableField("TRANS_NAME")
    private String transName;

    /**
     * 交易入参
     */
    @TableField("TRANS_IN")
    private String transIn;

    /**
     * 交易出参
     */
    @TableField("TRANS_OUT")
    private String transOut;

    /**
     * 0查询类  1非查询类  2登录类
     */
    @TableField("TRANS_TYPE")
    private String transType;

    /**
     * 操作时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 操作人登录用户名
     */
    @TableField("CREATE_LOGIN_NAME")
    private String createLoginName;

    /**
     * 操作人真实姓名
     */
    @TableField("CREATE_REAL_NAME")
    private String createRealName;

    /**
     * 访问IP
     */
    @TableField("TRANS_IP")
    private String transIp;

    /**
     * 交易对接厂商（非内部交易）
     */
    @TableField("TRANS_FIRM")
    private String transFirm;

    /**
     * 1正常 0删除
     */
    private String flag;

    /**
     * 交易耗时（ms）
     */
    @TableField("TRANS_TIMES")
    private Long transTimes;

    /**
     * 0 正常业务交易日志  9 错误交易日志
     */
    @TableField("TRANS_LEVEL")
    private Integer transLevel;


}
