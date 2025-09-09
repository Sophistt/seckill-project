package com.xxxx.seckill.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import com.xxxx.seckill.validator.IsMobile;

public class LoginVo {

    @NotNull
    @IsMobile(required = true) // 自定义注解
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
    
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
