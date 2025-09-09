package com.xxxx.seckill.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;

import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public RespBean handleGlobalException(GlobalException e) {
        return RespBean.error(e.getRespBeanEnum());
    }
    
    @ExceptionHandler(BindException.class)
    public RespBean handleBindException(BindException e) {
        RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
        respBean.setMessage("参数校验异常: " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return respBean;
    }
    
    @ExceptionHandler(Exception.class)
    public RespBean handleGeneralException(Exception e) {
        log.error("异常信息: ", e);
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
