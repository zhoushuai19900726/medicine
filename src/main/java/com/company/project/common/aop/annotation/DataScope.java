package com.company.project.common.aop.annotation;

import java.lang.annotation.*;

/**
 * LogAnnotation
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
}
