package indi.xm.component.libcommon.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: albert.fang
 * @date: 2022/1/13 15:51
 */
public class JacksonUtil {
    private static ObjectMapper om = new ObjectMapper();

    static {
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * json字符串转map（map等价于fastjson的jsonobject）
     *
     * @param str:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/13 16:28
     */
    public static Map<String, Object> jsonStringToMap(String str) {
        try {
            if (str == null) {
                return new HashMap<>(16);
            }
            return om.readValue(str, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return new HashMap<>(16);
        }
    }

    /**
     * json字符传转list（list等价于fastjson的jsonarray）
     *
     * @param str:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/13 16:32
     */
    public static <T> List<T> jsonStringToList(String str) {
        try {
            if (str == null) {
                return new ArrayList<>();
            }
            return om.readValue(str, new TypeReference<List<T>>() {
            });
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 对象转json字符串
     *
     * @param obj:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/13 16:28
     */
    public static String toJsonString(Object obj) {
        try {
            if (obj == null) {
                return "";
            }
            if (obj instanceof String) {
                return String.valueOf(obj);
            }
            return om.writeValueAsString(obj);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * json字符串转对象
     *
     * @param str:
     * @param clazz:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/13 16:30
     */
    public static <T> T jsonStringToBean(String str, Class<T> clazz) {
        try {
            if (str == null) {
                return null;
            }
            if (clazz.isInstance(str)) {
                return clazz.cast(str);
            }
            return om.readValue(str, clazz);
        } catch (Exception e) {
            return null;
        }
    }

}
