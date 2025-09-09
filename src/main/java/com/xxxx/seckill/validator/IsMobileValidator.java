package com.xxxx.seckill.validator;

import com.xxxx.seckill.utils.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码格式验证器实现类
 * 
 * <p>这是 {@link IsMobile} 注解对应的验证器实现，实现了Bean Validation框架的
 * {@link ConstraintValidator} 接口，用于执行具体的手机号码格式验证逻辑。
 * 
 * <p>验证器的工作流程：
 * <ol>
 *   <li>框架调用 {@link #initialize(IsMobile)} 方法初始化验证器参数</li>
 *   <li>对每个需要验证的值调用 {@link #isValid(String, ConstraintValidatorContext)} 方法</li>
 *   <li>根据注解的required属性决定是否允许空值</li>
 *   <li>使用 {@link ValidatorUtil#isMobile(String)} 执行实际的格式验证</li>
 * </ol>
 * 
 * <p>验证规则：
 * <ul>
 *   <li>当required=true时：空值和格式错误的手机号都视为无效</li>
 *   <li>当required=false时：空值视为有效，只对非空值进行格式验证</li>
 * </ul>
 * 
 * @author seckill-project
 * @since 1.0
 * @see IsMobile 对应的约束注解
 * @see ValidatorUtil#isMobile(String) 具体的手机号码格式验证工具方法
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    /**
     * 存储注解中定义的required属性值
     * <p>该字段在initialize方法中被初始化，用于控制验证器是否允许空值通过验证
     */
    private boolean required = false;

    /**
     * 初始化验证器
     * <p>Bean Validation框架在创建验证器实例后会调用此方法，传入对应的约束注解实例
     * <p>通过此方法可以获取注解中定义的参数值，并保存到验证器的实例变量中
     * 
     * @param constraintAnnotation IsMobile注解实例，包含了注解上定义的所有参数
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        // 从注解中获取required参数的值，并保存到实例变量中
        required = constraintAnnotation.required();
    }

    /**
     * 执行验证逻辑的核心方法
     * <p>Bean Validation框架会为每个需要验证的值调用此方法
     * <p>该方法根据初始化时获取的required参数来决定验证策略
     * 
     * @param s 待验证的字符串值，可能为null
     * @param constraintValidatorContext 验证上下文，用于自定义错误消息等（本实现中未使用）
     * @return true表示验证通过，false表示验证失败
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 当required=true时，必须是有效的手机号码（包括非空检查）
        if (required) {
            // 直接调用工具类进行格式验证，空值会被ValidatorUtil.isMobile()认为是无效的
            return ValidatorUtil.isMobile(s);
        } else {
            // 当required=false时，空值被认为是有效的
            if (StringUtils.isEmpty(s)) {
                return true;  // 允许空值通过验证
            } else {
                // 非空值需要进行格式验证
                return ValidatorUtil.isMobile(s);
            }
        }
    }
    
}
