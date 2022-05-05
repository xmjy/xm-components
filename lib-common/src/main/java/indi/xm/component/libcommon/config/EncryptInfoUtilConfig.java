package indi.xm.component.libcommon.config;

import indi.xm.component.libcommon.utils.EhireStringUtil;
import indi.xm.component.libcommon.utils.EncryptInfoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: albert.fang
 * @date: 2021/12/28 13:29
 */
@Configuration
public class EncryptInfoUtilConfig {
    @Value("${ehire.datasource.encrypt.file-path:}")
    private String encryptKeyFilePath;
    @Value("${ehire.datasource.encrypt.key:jScjb0fOf2ZsV00ljfmVWS9O2Hp8YRmS}")
    private String key;
    @Value("${ehire.datasource.encrypt.iv:FKTNr9uFfqyfe8Lg}")
    private String iv;

    @Bean
    public EncryptInfoUtil encryptInfoUtil() {
        if (!EhireStringUtil.isEmpty(encryptKeyFilePath)) {
            return new EncryptInfoUtil(encryptKeyFilePath);
        } else {
            return new EncryptInfoUtil(key, iv);
        }
    }
}
