package indi.xm.component.dynamic.datasource.config.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 数据库配置
 *
 * @author: albert.fang
 * @date: 2021/12/15 15:25
 */
@Component
@ConfigurationProperties(prefix = "job51.multi")
@Data
public class DbProperties {
    private Map<String, HikariDataSource> db = new HashMap<>();
    private String defaultDbName;
    private String dbApartName;
}