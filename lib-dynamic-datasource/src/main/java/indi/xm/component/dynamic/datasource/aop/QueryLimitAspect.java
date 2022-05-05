
package indi.xm.component.dynamic.datasource.aop;

import indi.xm.component.dynamic.datasource.model.annotations.QueryLimit;
import indi.xm.component.dynamic.datasource.util.DynamicDataSourceUtil;
import indi.xm.component.libcommon.utils.EhireStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * QueryLimit注解处理
 * 数据库in集合操作时依据操作上限拆分操作语句，并将操作结果合并返回
 * 参数必须有一个是list类型的，不需要是第一个参数
 * 返回值只能是list或int类型的
 * 支持增删改查
 *
 * @author: albert.fang
 * @date: 2021/9/16 16:52
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class QueryLimitAspect {

    @Pointcut("@annotation(indi.xm.component.dynamic.datasource.model.annotations.QueryLimit)")
    public void queryLimit() {
    }

    @Around(value = "queryLimit()")
    @SuppressWarnings("all")
    public Object aroundQueryLimit(ProceedingJoinPoint point) throws Throwable {
        //获取方法上的注解
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        boolean isQueryLimitPresent = method.isAnnotationPresent(QueryLimit.class);
        //执行注解处理
        if (isQueryLimitPresent) {
            //获取注解上自定义的limitCount
            QueryLimit annotation = method.getAnnotation(QueryLimit.class);
            int limitCount = annotation.limitCount();

            //获取方法参数
            Object[] args = point.getArgs();
            List ids = null;
            int argIndex = 0;
            //找到第一个List类型的参数
            for (argIndex = 0; argIndex < args.length; argIndex++) {
                if (args[argIndex] instanceof List) {
                    ids = (List) args[argIndex];
                    break;
                }
            }
            if (CollectionUtils.isEmpty(ids)) {
                throw new IllegalArgumentException("未找到list类型的参数");
            }
            List tempApIds = new ArrayList<>();
            List resultList = new ArrayList<>();
            Integer resultInt = 0;
            boolean listFlag = true;

            int offset = 0;
            int total = ids.size();
            int toIndex = 0;

            //主分库问题
            String dataSource = DynamicDataSourceUtil.getDataSource();
            while (offset < total) {
                toIndex = Math.min(offset + limitCount, total);
                tempApIds = ids.subList(offset, toIndex);
                args[argIndex] = tempApIds;

                if (!EhireStringUtil.isEmpty(dataSource)) {
                    DynamicDataSourceUtil.setDataSourceByDbName(dataSource);
                }
                Object obj = point.proceed(args);
                if (obj instanceof List) {
                    resultList.addAll((List) obj);
                } else if (obj instanceof Integer) {
                    resultInt += (Integer) obj;
                    listFlag = false;
                } else {
                    throw new IllegalArgumentException("不支持的返回值类型");
                }
                offset += limitCount;
            }
            return listFlag ? resultList : resultInt;
        } else {
            return point.proceed();
        }
    }

}