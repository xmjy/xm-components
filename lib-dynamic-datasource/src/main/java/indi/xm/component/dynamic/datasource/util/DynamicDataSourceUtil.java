package indi.xm.component.dynamic.datasource.util;

import indi.xm.component.libcommon.utils.StaticPropertiesUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据源切换工具
 * 注：不可用于异步线程(需要使用的话注意资源的释放(异步线程finally块中调用DynamicDataSourceUtil.remove()方法))
 *
 * @author: albert.fang
 * @date: 2021/10/18 17:13
 */
@Slf4j
public class DynamicDataSourceUtil {
    /**
     * 若第一优先级数据源没有内容，则使用这个数据源
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();
    /**
     * 第一优先级 使用的数据源
     */
    private static final ThreadLocal<String> THREAD_LOCAL_SWAP = new ThreadLocal<>();

    private static void putDataSource(String name) {
        THREAD_LOCAL_SWAP.set(name);
    }

    public static String getDataSource() {
        return null != THREAD_LOCAL_SWAP.get() ? THREAD_LOCAL_SWAP.get() : THREAD_LOCAL.get();
    }

    public static void removeDataSource() {
        THREAD_LOCAL_SWAP.remove();
    }

    public static void putDefaultDataSource(String name) {
        THREAD_LOCAL.set(name);
    }

    public static void remove() {
        THREAD_LOCAL_SWAP.remove();
        THREAD_LOCAL.remove();
    }

    /**
     * 根据dbid来切换分库
     *
     * @param dbId:
     * @return :
     * @author: albert.fang
     * @date: 2021/10/18 17:16
     */
    public static void setDataSourceByDbId(int dbId) {
        String dataSource = getDataSourceNameByDbId(dbId);
        log.debug("切换数据源至[{}]", dataSource);
        putDataSource(dataSource);
    }

    /**
     * 根据dbname来切换分库
     *
     * @param dbName:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/22 19:24
     */
    public static void setDataSourceByDbName(String dbName) {
        String dbApartName = StaticPropertiesUtil.getProperty("job51.multi.dbApartName");
        String[] dbNames = dbApartName.split(",");
        boolean dbNameValid = false;
        for (String dbNameTemp : dbNames) {
            if (dbNameTemp.equals(dbName)) {
                dbNameValid = true;
                break;
            }
        }
        if (!dbNameValid) {
            throw new IllegalArgumentException("dbName异常：" + dbName);
        }
        log.debug("切换数据源至[{}]", dbName);
        putDataSource(dbName);
    }

    /**
     * 设置默认的数据源 不可修改
     *
     * @param dbId 1-4
     * @return void
     * @author cheng.liang
     * @date 2021/12/29 14:31
     */
    public static void setDefaultDataSourceByDbId(Integer dbId) {
        if (dbId == null) {
            return;
        }
        String dataSource = getDataSourceNameByDbId(dbId);
        log.debug("切换数据源至[{}]", dataSource);
        putDefaultDataSource(dataSource);
    }

    /**
     * 通过dbId获取数据源名称
     *
     * @param dbId:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/10 11:00
     */
    private static String getDataSourceNameByDbId(Integer dbId){
        //获取配置文件中配置的数据源名称
        String dbApartName = StaticPropertiesUtil.getProperty("job51.multi.dbApartName");
        //根据DBId获取数据源
        int dbIdIndex = dbId - 1;
        if (dbIdIndex < 0) {
            throw new IllegalArgumentException("dbid异常：" + dbId);
        }
        return dbApartName.split(",")[dbIdIndex];
    }
}
