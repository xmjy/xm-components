package indi.xm.component.dbsource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.xm.component.dbsource.properties.DataSourceEncryptProperties;
import indi.xm.component.dbsource.properties.DataSourceNacosProperties;
import indi.xm.component.dbsource.util.Aes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author albert.fang
 */
@Component
public class DataSource {
    public static Logger logger = LoggerFactory.getLogger(DataSource.class);
    private boolean inited = false;
    private DataSourceEncryptProperties dataSourceEncryptProperties;
    private final NacosConfigManager nacosConfigManager;
    private DataSourceNacosProperties dataSourceNacosProperties;
    private Map<String, DataSourceConfig> dataSourceConfigMap;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Aes aes;

    public DataSource(NacosConfigManager nacosConfigManager) {
        this.nacosConfigManager = nacosConfigManager;
    }

    public DataSourceConfig get(String dataSourceName) throws NacosException, JsonProcessingException {
        if (!this.inited) {
            this.init();
        }

        if (this.dataSourceConfigMap.containsKey(dataSourceName)) {
            return this.dataSourceConfigMap.get(dataSourceName);
        }

        DataSource.logger.error("lib-config-datasource错误：" + dataSourceName + "不存在");
        throw new RuntimeException("lib-config-datasource错误：" + dataSourceName + "不存在");
    }

    public List<DataSourceConfig> get(String... dataSourceName) throws NacosException, JsonProcessingException {
        List<DataSourceConfig> list = new LinkedList<>();
        for (String s : dataSourceName) {
            list.add(this.get(s));
        }
        return list;
    }

    /**
     * 重新加载配置
     */
    public void reload() {
        this.inited = false;
    }

    @Autowired
    public void setDataSourceNacosProperties(DataSourceNacosProperties dataSourceNacosProperties) {
        this.dataSourceNacosProperties = dataSourceNacosProperties;
    }

    @Autowired
    public void setDataSourceEncryptProperties(DataSourceEncryptProperties dataSourceEncryptProperties) {
        this.dataSourceEncryptProperties = dataSourceEncryptProperties;
    }

    @Autowired
    public void setAes(Aes aes) {
        this.aes = aes;
    }

    /**
     * 初始化datasource数据
     */
    private void init() throws NacosException, JsonProcessingException {
        if (this.inited) {
            return;
        }

        if (this.dataSourceNacosProperties.getDataId() == null || "".equals(this.dataSourceNacosProperties.getDataId())) {
            DataSource.logger.error("lib-config-datasource错误：未指定dataid");
            throw new RuntimeException("lib-config-datasource错误：未指定dataid");
        }

        if (this.dataSourceNacosProperties.getGroup() == null || "".equals(this.dataSourceNacosProperties.getGroup())) {
            DataSource.logger.error("lib-config-datasource错误：未指定group");
            throw new RuntimeException("lib-config-datasource错误：未指定group");
        }

        String config = this.nacosConfigManager.getConfigService().getConfig(this.dataSourceNacosProperties.getDataId(), this.dataSourceNacosProperties.getGroup(), 5000);
        Map<String, DataSourceConfig> map = this.objectMapper.readValue(config, new TypeReference<Map<String, DataSourceConfig>>() {
        });

        if (this.dataSourceEncryptProperties.isEnable()) {
            map.forEach((key, value) -> {
                map.get(key).setPassword(this.aes.decrypt(map.get(key).getPassword(), this.dataSourceEncryptProperties.getKey(), this.dataSourceEncryptProperties.getIv()));
            });
        }

        DataSource.logger.info("数据仓库配置载入成功,dataId:" + this.dataSourceNacosProperties.getDataId() + "group:" + dataSourceNacosProperties.getGroup());
        this.dataSourceConfigMap = map;

        if (!this.inited) {
            this.inited = true;
        }
    }
}
