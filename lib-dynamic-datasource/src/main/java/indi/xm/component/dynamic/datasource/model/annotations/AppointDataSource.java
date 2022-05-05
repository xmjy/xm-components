package indi.xm.component.dynamic.datasource.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以用在类上或方法上，用来标注数据源
 *
 * @author: albert.fang
 * @date: 2022/3/9 17:23
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppointDataSource {
    String value();
}
