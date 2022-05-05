package indi.xm.component.libcommon.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author: albert.fang
 * @date: 2021/10/29 16:09
 */
@Component
public class StaticPropertiesUtil {

    private static Environment environment;

    @Autowired
    public StaticPropertiesUtil(Environment env) {
        environment = env;
    }

    public static String getProperty(String key) {
        String value = environment.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException(String.format("%s配置不存在", key));
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = environment.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
