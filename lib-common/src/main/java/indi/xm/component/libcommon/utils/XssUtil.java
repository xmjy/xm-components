package indi.xm.component.libcommon.utils;

/**
 * @author: albert.fang
 * @date: 2021/12/21 16:48
 */
public class XssUtil {
    /**
     * 防止xss过滤
     *
     * @param value: 待过滤参数
     * @return :
     * @author: albert.fang
     * @date: 2021/12/21 16:50
     */
    public static String xssEncode(String value) {
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("%3C", "&lt;").replaceAll("%3E", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("%28", "&#40;").replaceAll("%29", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }
}
