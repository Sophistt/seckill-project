package com.xxxx.seckill.utils;

import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class ValidatorUtil {
    
    private static final Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");

    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        return mobile_pattern.matcher(mobile).matches();
    }
}
