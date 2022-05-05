
package indi.xm.component.dynamic.datasource.aop;

import indi.xm.component.dynamic.datasource.model.annotations.AppointDataSource;
import indi.xm.component.dynamic.datasource.util.DynamicDataSourceUtil;
import indi.xm.component.libcommon.utils.StaticPropertiesUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 动态数据源切面
 *
 * @author: albert.fang
 * @date: 2021/12/21 16:34
 */
@Aspect
@Component
@Order(1)
public class DynamicDataSourceAspect {

    @Pointcut("execution(public * com.job51.ehire.*.mapper..*.*(..))")
    public void switchDb() {
    }

    /**
     * 设置动态数据源
     *
     * @param annotation:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 10:49
     */
    private void setDynamicDataSource(AppointDataSource annotation) {
        DynamicDataSourceUtil.setDataSourceByDbName(annotation.value());
    }

    /**
     * 首先获取方法上的注解，若没有则获取类上的注解，若类上也没有注解，则取动态数据源变量，若变量为空，则取默认的数据源
     * 方法注解->类注解->动态数据源变量->默认数据源
     *
     * @param point:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 10:49
     */
    @Before(value = "switchDb()")
    public void beforeSwitchDb(JoinPoint point) {
        // 获取切入的 Method
        MethodSignature joinPointObject = (MethodSignature) point.getSignature();
        Method method = joinPointObject.getMethod();
        boolean flag = method.isAnnotationPresent(AppointDataSource.class);
        if (flag) {
            AppointDataSource annotation = method.getAnnotation(AppointDataSource.class);
            setDynamicDataSource(annotation);
        } else {
            // 如果方法上没有注解，则搜索类上是否有注解
            AppointDataSource classAnnotation = AnnotationUtils.findAnnotation(joinPointObject.getMethod().getDeclaringClass(), AppointDataSource.class);
            if (classAnnotation != null) {
                setDynamicDataSource(classAnnotation);
            }
            // 如果没有设置设置数据源用默认的数据源
            else if (DynamicDataSourceUtil.getDataSource() == null) {
                // 没有加数据库注解时设置默认数据源
                DynamicDataSourceUtil.setDataSourceByDbName(StaticPropertiesUtil.getProperty("job51.multi.defaultDbName"));
            }
        }
    }

    /**
     * 每条sql执行后删除动态数据源变量，查找数据源时因没有设置数据源而会使用默认的数据源
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 10:47
     */
    @After(value = "switchDb()")
    public void afterSwitchDs() {
        DynamicDataSourceUtil.removeDataSource();
    }

}