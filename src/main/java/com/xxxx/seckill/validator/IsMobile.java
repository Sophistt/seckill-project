package com.xxxx.seckill.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * 手机号码格式验证注解
 * 
 * <p>这是一个自定义的Bean Validation约束注解，用于验证字符串是否符合中国大陆手机号码格式。
 * 支持11位数字格式，以1开头的手机号码。
 * 
 * <p>使用示例：
 * <pre>
 * public class UserLoginForm {
 *     &#64;IsMobile(required = true, message = "请输入正确的手机号码")
 *     private String mobile;
 * }
 * </pre>
 * 
 * <p>该注解通过 {@link IsMobileValidator} 实现具体的验证逻辑。
 * 
 * @author seckill-project
 * @since 1.0
 * @see IsMobileValidator 对应的验证器实现类
 */
// 定义注解可以应用的目标位置：方法、字段、注解类型、构造器、参数、类型使用等
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
// 指定注解保留策略为运行时，使得反射可以在运行时读取注解信息
@Retention(RUNTIME)
// 表示该注解应该包含在JavaDoc中
@Documented
// 标识这是一个Bean Validation约束注解，并指定对应的验证器实现类
@Constraint(
	validatedBy = {
		IsMobileValidator.class  // 关联的验证器实现类
	}
)
public @interface IsMobile {

	/**
	 * 是否必填字段
	 * <p>当设置为true时，空值将被认为是无效的
	 * <p>当设置为false时，空值将通过验证，只有非空值才会进行格式校验
	 * 
	 * @return 是否必填，默认为true
	 */
	boolean required() default true;

	/**
	 * 验证失败时的错误消息
	 * <p>这是Bean Validation规范要求的标准属性，用于定义验证失败时返回的错误信息
	 * 
	 * @return 错误消息文本，默认为"手机号码格式错误"
	 */
    String message() default "手机号码格式错误";

	/**
	 * 验证组
	 * <p>Bean Validation规范要求的标准属性，用于分组验证场景
	 * <p>可以通过指定不同的组来在不同场景下启用不同的验证规则
	 * 
	 * @return 验证组数组，默认为空（使用默认组）
	 */
	Class<?>[] groups() default { };

	/**
	 * 负载信息
	 * <p>Bean Validation规范要求的标准属性，用于携带约束的元数据信息
	 * <p>通常用于传递严重级别等附加信息，供验证框架或应用程序使用
	 * 
	 * @return 负载信息数组，默认为空
	 */
	Class<? extends Payload>[] payload() default { };
}
