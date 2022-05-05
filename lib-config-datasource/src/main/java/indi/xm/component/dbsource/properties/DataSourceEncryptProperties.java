package indi.xm.component.dbsource.properties;

import indi.xm.component.dbsource.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @author albert.fang
 */
@Component
@ConfigurationProperties(prefix = "ehire.datasource.encrypt")
public class DataSourceEncryptProperties implements InitializingBean {
    private String filePath = "/www/common/config/aes256.key";
    private String key = "jScjb0fOf2ZsV00ljfmVWS9O2Hp8YRmS";
    private String iv = "FKTNr9uFfqyfe8Lg";
    private boolean enable = true;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.hasText(this.filePath)) {
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                File file = new File(this.filePath);
                if (file.exists()) {
                    inputStream = new FileInputStream(file);
                } else {
                    inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.filePath);
                }

                if (null == inputStream) {
                    DataSource.logger.info("数据仓库配置指定的配置加密文件不存在，使用默认key和iv");
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                this.setKey(reader.readLine());
                this.setIv(reader.readLine());
            } catch (IOException e) {
                DataSource.logger.info("数据仓库配置指定的配置加密文件无法获取，使用默认key和iv。" + e.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ignore) {
                }
            }
        }
    }
}
