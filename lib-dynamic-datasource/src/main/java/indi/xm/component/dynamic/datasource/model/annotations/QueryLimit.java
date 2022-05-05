package indi.xm.component.dynamic.datasource.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库集合查询时依据查询上限拆分查询语句，并将查询结果合并返回
 *
 * @author: albert.fang
 * @date: 2021/9/16 16:50
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryLimit {
    int limitCount() default 1000;
}
