package indi.xm.component.dynamic.datasource.config.db;

import indi.xm.component.dynamic.datasource.util.DynamicDataSourceUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author: albert.fang
 * @date: 2022/3/9 17:29
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 数据源路由，此方用于产生要选取的数据源逻辑名称
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceUtil.getDataSource();
    }
}
