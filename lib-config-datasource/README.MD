# 使用方式

## 引入
```xml
<dependency>
    <groupId>com.job51.ehire.component</groupId>
    <artifactId>lib-config-datasource</artifactId>
    <version>版本</version>
</dependency>
```
## Nacos配置
```json
{
	"redisCluster2": {
		"url": "10.100.6.222:7000,10.100.6.222:7001,10.100.6.222:7002,10.100.6.222:7003,10.100.6.222:7004,10.100.6.222:7005",
		"password": "NIVw6j9AhLkzmf/3VlE6LoR0CsoVOP00vVAqD6XxPUE=",
		"max-redirects": 5,
		"max-idle": 5,
		"min-idle": 2,
		"max-active": 5,
		"max-wait": 5000,
		"time-between-eviction-runs": 4000,
		"command-timeout": 3000,
		"shutdown-timeout": 6000
	}
}
```

## 项目必须要有的依赖：
```xml
<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

## bootstrap.yml配置，指定数据库配置文件的group和dataid
```yml
ehire:
  datasource:
    nacos:
      data-id: ehire-config-datasource-dev
      group: DEFAULT_GROUP
    encrypt:
      enable: true
      key: jScjb0fOf2ZsV00ljfmVWS9O2Hp8YRmS
      iv: FKTNr9uFfqyfe8Lg
      file-path: /www/common/config/aes256.key
```
由于是基于nacos的，所以你的项目需要自行配置nacos

## Bean定义
如果没有定制化的bean，可以不用定义，直接springboot会统一管理DataSource对象
```java
@Configuration
class Demo {
    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Bean
    public DataSource getDataSource() {
        //需要依赖项目传入配置所在的nacos的地址
        return new DataSource(this.nacosConfigManager);
    }
}
```

## 获取配置
```java
@RestController
public class AController {
    @Autowired
    private DataSource dataSource;

    @RequestMapping("index")
    public String get() throws JsonProcessingException, NacosException {
        //单个
        DataSourceConfig dataSourceConfig = this.dataSource.get("oracle-51job");
        //多个，返回结果为LinkedList
        List<DataSourceConfig> dataSourceConfigs = this.dataSource.get("oracle-51job", "oracle-51job", "oracle-51job");
        return "asd";
    }
}
```