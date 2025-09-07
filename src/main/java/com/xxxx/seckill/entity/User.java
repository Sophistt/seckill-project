package com.xxxx.seckill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Claude Code Generator
 * @since 2025-09-07
 */
@Getter
@Setter
@ToString
@TableName("t_user")
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableField("nickname")
    private String nickname;

    /**
     * MD5(MD5(password+固定salt)+salt)
     */
    @TableField("password")
    @ApiModelProperty("MD5(MD5(password+固定salt)+salt)")
    private String password;

    @TableField("salt")
    private String salt;

    /**
     * 头像
     */
    @TableField("head")
    @ApiModelProperty("头像")
    private String head;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    @TableField("register_date")
    private LocalDateTime registerDate;

    /**
     * 最后一次登录时间
     */
    @ApiModelProperty("最后一次登录时间")
    @TableField("last_login_date")
    private LocalDateTime lastLoginDate;

    /**
     * 登录次数
     */
    @ApiModelProperty("登录次数")
    @TableField("login_count")
    private Integer loginCount;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
