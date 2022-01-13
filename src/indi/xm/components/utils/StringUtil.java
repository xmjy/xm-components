package indi.xm.components.utils;

/**
 * @ProjectName: xm-components
 * @Package: indi.xm.components.utils
 * @ClassName: StringUtil
 * @Author: albert.fang
 * @Description: 字符串工具类
 * @Date: 2022/1/13 14:43
 */
public class StringUtil {

    /**
     * 删除 str 前后的 del 指定字符
     *
     * @param str 输入的字符串
     * @param del 指定的要删除字符
     * @return java.lang.String
     * @author Albert.fang
     * @date 2022/1/11 10:17
     */
    public static String trim(String str,char del) {
        int len = str.length();
        int st = 0;
        char[] val = str.toCharArray();

        while ((st < len) && (val[st] == del)) {
            st++;
        }
        while ((st < len) && (val[len - 1] == del)) {
            len--;
        }
        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
    }

    /**
     * 删除字符串末尾指定字符
     *
     * @param str 字符串
     * @param del 指定字符
     *
     * @return java.lang.String
     * @author Albert.fang
     * @date 2022/1/12 9:53
     */
    public static String trimEnd(String str,char del) {
        int len = str.length();
        char[] val = str.toCharArray();

        while ((0 < len) && (val[len - 1] == del)) {
            len--;
        }
        return len < str.length() ? str.substring(0, len) : str;
    }

    /**
     * 检查str是否满足len位，不足的话，在右侧补充del字符
     *
     * @param str 待左对齐的字符串
     * @param len 规定长度
     * @param del 待填充的字符
     * @return 左对齐之后的字符串
     */
    public static String padRight(String str,int len,char del) {

        // 如果str不为空，并且长度大于等于len，那么直接返回
        if ( str != null && str.length() >= len) return str;

        StringBuilder sb;
        if (str == null) sb = new StringBuilder();
        else sb = new StringBuilder(str);

        int alreadyLen = sb.length();

        for (int i = 0; i < len - alreadyLen; i++) {
            sb.append(del);
        }

        return sb.toString();

    }

    /**
     * 检查str是否满足len位，不足的话，在左侧补充del字符
     *
     * @param str 待右对齐字符串
     * @param len 满足几位
     * @param del 待填充的字符
     * @return java.lang.String 左对齐之后的字符串
     * @author Albert.fang
     * @date 2022/1/12 11:10
     */
    public static String padLeft(String str,int len,char del) {

        // 如果str不为空，并且长度大于等于len，那么直接返回
        if ( str != null && str.length() >= len) return str;

        StringBuilder sb;
        if (str == null) sb = new StringBuilder();
        else sb = new StringBuilder(str);

        int alreadyLen = sb.length();

        for (int i = 0; i < len - alreadyLen; i++) {
            sb.insert(0,del);
        }

        return sb.toString();

    }

}
