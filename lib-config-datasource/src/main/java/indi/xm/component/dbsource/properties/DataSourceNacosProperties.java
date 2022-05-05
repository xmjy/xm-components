package indi.xm.component.dbsource.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author albert.fang
 */
@Component
@ConfigurationProperties(prefix = "ehire.datasource.nacos")
public class DataSourceNacosProperties implements InitializingBean {
    @Value("${spring.profiles.active}")
    private String env;
    private String group = "DEFAULT_GROUP";
    private String dataId = "ehire-config-datasource";

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.dataId.endsWith(this.env)) {
            this.dataId = this.dataId + "-" + this.env;
        }
    }
}
