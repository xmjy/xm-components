package indi.xm.component.dynamic.datasource.config.db;

import indi.xm.component.dbsource.DataSourceConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: albert.fang
 * @date: 2022/1/17 16:04
 */
@Configuration
@Slf4j
public class DatabaseConfig {
    @javax.annotation.Resource
    private DbProperties dbProperties;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;
    @Value("${mybatis.mapper-locations}")
    private String mybatisMapperLocations;
    @Value("${mybatis.type-handlers-package}")
    private String typeHandlersPackage;

    @javax.annotation.Resource
    private indi.xm.component.dbsource.DataSource dataSource;

    /**
     * 初始化数据源配置
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/2/21 17:15
     */
    private void initDbProperties() {
        try {
            String dbApartName = dbProperties.getDbApartName();
            String[] dbNames = dbApartName.split(",");
            Map<String, HikariDataSource> db = new HashMap<>(dbNames.length);
            for (String dbName : dbNames) {
                HikariDataSource hikariDataSource = new HikariDataSource();
                db.put(dbName, hikariDataSource);
            }
            dbProperties.setDb(db);
            for (String key : dbProperties.getDb().keySet()) {
                DataSourceConfig dataSourceConfig = dataSource.get(key);
                HikariDataSource hikariDataSource = dbProperties.getDb().get(key);
                hikariDataSource.setJdbcUrl(dataSourceConfig.getUrl());
                hikariDataSource.setUsername(dataSourceConfig.getUsername());
                hikariDataSource.setPassword(dataSourceConfig.getPassword());
                hikariDataSource.setPoolName(key + "hikariPool");
                hikariDataSource.setDriverClassName(dataSourceConfig.getDriverClassName());
                //池中最大连接数，包括闲置和使用中的连接，默认值-1，如果maxPoolSize小于1，则会被重置。
                //当minIdle<=0被重置为DEFAULT_POOL_SIZE则为10;如果minIdle>0则重置为minIdle的值
                hikariDataSource.setMaximumPoolSize(dataSourceConfig.getMaxActive());
                //池中维护的最小空闲连接数，默认值-1，minIdle<0或者minIdle>maxPoolSize,则被重置为maxPoolSize
                hikariDataSource.setMinimumIdle(dataSourceConfig.getMinIdle());
            }
        } catch (Exception e) {
            log.error("获取数据源配置异常：", e);
            throw new RuntimeException("获取数据源配置异常");
        }
    }

    /**
     * 动态数据源
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 17:28
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        log.info("--------------------  dynamicDataSource init ---------------------");
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        initDbProperties();
        // 按照目标数据源名称和目标数据源对象的映射存放在Map中
        Map<Object, Object> targetDataSources = new HashMap<>(16);
        String dbApartName = dbProperties.getDbApartName();
        String defaultDbName = dbProperties.getDefaultDbName();
        if (dbApartName != null) {
            String[] dbNames = dbApartName.split(",");
            for (String dbName : dbNames) {
                targetDataSources.put(dbName, dbProperties.getDb().get(dbName));
            }
        } else {
            log.error("job51.multi.dbApartName节点未配置");
            throw new RuntimeException("job51.multi.dbApartName节点未配置");
        }
        for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
            HikariDataSource hikariDataSource = (HikariDataSource) entry.getValue();
            hikariDataSource.setAutoCommit(false);
        }
        // 采用是想AbstractRoutingDataSource的对象包装多数据源
        dynamicDataSource.setTargetDataSources(targetDataSources);
        // 设置默认的数据源，当拿不到数据源时，使用此配置
        if (defaultDbName == null) {
            throw new RuntimeException("job51.multi.defaultDbName节点配置异常");
        }
        HikariDataSource defaultDataSource = dbProperties.getDb().get(defaultDbName);
        if (defaultDataSource == null) {
            throw new RuntimeException("job51.multi.defaultDbName节点配置异常");
        }
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        log.info("--------------------  sqlSessionFactory init ---------------------");
        try {
            SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
            sessionFactoryBean.setDataSource(dataSource);
            sessionFactoryBean.setTransactionFactory(new MultiDataSourceTransactionFactory());
            // 读取配置
            sessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
            sessionFactoryBean.setTypeHandlersPackage(typeHandlersPackage);
            //设置mapper.xml文件所在位置
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mybatisMapperLocations);
            sessionFactoryBean.setMapperLocations(resources);
            return sessionFactoryBean.getObject();
        } catch (IOException e) {
            log.error("mybatis resolver mapper*xml is error", e);
            return null;
        } catch (Exception e) {
            log.error("mybatis sqlSessionFactoryBean create error", e);
            return null;
        }
    }
}
