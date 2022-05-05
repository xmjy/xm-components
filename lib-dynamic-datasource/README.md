提供多数据源连接、切换功能和多数据源事务处理功能。



## 接入方法

1.加依赖

```
        <lib.dynamic.datasource.version>1.0-SNAPSHOT</lib.dynamic.datasource.version>
        <dependency>
            <groupId>com.job51.ehire.component</groupId>
            <artifactId>lib-dynamic-datasource</artifactId>
            <version>${lib.dynamic.datasource.version}</version>
        </dependency>
```

2.写配置

```
#数据库配置
job51:
  multi:
    #数据源列表，逗号分隔
    dbApartName: oracle-ehire1,oracle-ehire2,oracle-ehire3,oracle-ehire4,oracle-ehireinternal,mysql-ehire-order,sqlserver-resume
    #默认数据源
    defaultDbName: oracle-ehire1
#mybatis配置
mybatis:
  mapper-locations: classpath*:mapper/**/*.xml
  type-aliases-package: com.job51.ehire.order.entity
  type-handlers-package: com.job51.ehire.order.config.mybatis
```

3.加注解

```
@SpringBootApplication(scanBasePackages = {"com.job51.ehire"}, exclude = {DataSourceAutoConfiguration.class})
```

## 使用方法

1.通过注解切换数据源（加在mapper类或方法上）

```
@AppointDataSource("oracle-ehireinternal")
```

2.通过方法切换数据源（dbId从1开始）

```
DynamicDataSourceUtil.setDataSourceByDbId(2);
DynamicDataSourceUtil.setDataSourceByDbName("oracle-ehireinternal");
DynamicDataSourceUtil.setDefaultDataSourceByDbId(2);
```

3.事务处理

```
@Transactional
```

