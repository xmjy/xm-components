package indi.xm.component.libcommon.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: albert.fang
 * @date: 2021/12/28 13:37
 */
@Slf4j
public class EhireStringUtil extends org.apache.commons.lang3.StringUtils {

    private static final String ILLEGAL_CHARACTERS = "[\\\\/:\\\\*\\\\?\\\\\"<>\\\\|]";
    public static final int LINUX_FILE_MAX_LENGTH = 200;
    private static final String REGEX_HTML = "<|(\\[)|(\\^)|>|]|(\\+)|/|！|(\")|#|%|&|(\\()|(\\))|(\\*)|(\\.)|:|;|=|(\\?)|@|_|(\\{)|}|`|~|(\\|)|script";
    /**
     * 32位 --- 对应Integer
     */
    private static final int NUM_32BIT = 32;
    /**
     * 64位 --- 对应Long
     */
    private static final int NUM_64BIT = 64;

    /**
     * 逗号分隔string转longlist
     *
     * @param ids 逗号分隔
     * @return java.util.List<java.lang.Long>
     * @author xinpeng.Fu
     * @date 2020/4/22 10:09
     **/
    public static List<Long> getIdsFromString(String ids) {
        if (isBlank(ids)) {
            return new ArrayList<>(0);
        }
        String[] idStrs = ids.split(",");
        try {
            return Stream.of(idStrs).map(Long::parseLong).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("数据异常");
        }
    }

    /**
     * longlist转逗号分隔string
     *
     * @param ids list
     * @return java.util.List<java.lang.Long>
     * @author xinpeng.Fu
     * @date 2020/4/22 10:09
     **/
    public static String getStringFromIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return "";
        }
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * 转义sql特殊字符
     *
     * @param likeField like的字符串
     * @return java.lang.String
     * @author xinpeng.Fu
     * @date 2020/7/4 15:04
     **/
    public static String toMysqlNormalString(String likeField) {
        if (isBlank(likeField)) {
            return likeField;
        }
        return likeField.replaceAll("\\\\", "\\\\\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }

    /**
     * 获取字符长度
     *
     * @param str String参数
     * @return int
     * @author Andy.liu
     * @date 2020/4/26 14:46
     */
    public static int getStrLength(String str) {
        char[] chars = str.toCharArray();
        int sum = 0;
        for (int i = 0; i < chars.length; i++) {
            if (str.charAt(i) > 127) {
                sum = sum + 2;
            } else {
                sum = sum + 1;
            }
        }
        return sum;
    }

    /**
     * 获取字符串在linux上的长度
     *
     * @param str 字符串
     * @return 长度
     */
    public static int getLinuxStrLength(String str) {
        char[] chars = str.toCharArray();
        int sum = 0;
        for (int i = 0; i < chars.length; i++) {
            char temp = str.charAt(i);
            if (temp <= 255) {
                sum = sum + 1;
            } else if (isChinese(temp)) {
                sum = sum + 3;
            } else {
                sum = sum + 4;
            }
        }
        return sum;
    }


    /**
     * 判断是否为中文字符
     *
     * @param c :
     * @return :
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 以字符串在linux上的长度的算法截断字符串
     *
     * @param str    字符串
     * @param length 截断的长度
     * @return 截断后的字符串
     */
    public static String subStrByLinuxLength(String str, int length) {
        if (getLinuxStrLength(str) <= length) {
            return str;
        } else {
            char[] chars = str.toCharArray();
            int sum = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                char temp = str.charAt(i);
                if (temp <= 255) {
                    sum = sum + 1;
                } else if (isChinese(temp)) {
                    sum = sum + 3;
                } else {
                    sum = sum + 4;
                }
                if (sum <= length) {
                    sb.append(temp);
                } else {
                    break;
                }
            }
            return sb.toString();
        }
    }

    /**
     * 文件名称长度截断和非法字符过滤
     *
     * @param name 文件名称
     * @return java.lang.String
     * @author Andy.liu
     * @date 2020/7/11 11:05
     */
    public static String getValidFileName(String name) {
        String fileName = name.replaceAll(ILLEGAL_CHARACTERS, "");
        if (getLinuxStrLength(fileName) > LINUX_FILE_MAX_LENGTH) {
            fileName = subStrByLinuxLength(fileName, LINUX_FILE_MAX_LENGTH);
        }
        return fileName;
    }

    /***
     * 对object中的所有字符串类型成员变量的值，执行trim操作
     * @param obj 传参对象
     */
    public static void trimObject(Object obj) {
        if (obj == null) {
            return;
        }
        Field[] fieldsList = obj.getClass().getDeclaredFields();
        try {
            for (Field f : fieldsList) {
                f.setAccessible(true);
                Object vObj = f.get(obj);
                if ("java.lang.String".equals(f.getType().getName()) && (vObj instanceof String)) {
                    String str = (String) vObj;
                    str = str.trim();
                    f.set(obj, str);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("trimObject IllegalAccessException:{}", e.toString());
        }
    }

    /**
     * 删除 str 前后的 del 指定字符
     *
     * @param str 输入的字符串
     * @param del 指定的要删除字符
     * @return java.lang.String
     * @author Albert.fang
     * @date 2022/1/11 10:17
     */
    public static String trim(String str, char del) {
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
     * 删除字符串开头指定字符
     *
     * @param str 字符串
     * @param del 指定字符
     * @return java.lang.String
     * @author Albert.fang
     * @date 2022/1/12 9:50
     */
    public static String trimStart(String str, char del) {
        int len = str.length();
        char[] val = str.toCharArray();
        int st = 0;
        while ((st < len) && (val[st] == del)) {
            st++;
        }
        return st > 0 ? str.substring(st) : str;
    }

    /**
     * 删除字符串末尾指定字符
     *
     * @param str 字符串
     * @param del 指定字符
     * @return java.lang.String
     * @author Albert.fang
     * @date 2022/1/12 9:53
     */
    public static String trimEnd(String str, char del) {
        int len = str.length();
        char[] val = str.toCharArray();

        while ((0 < len) && (val[len - 1] == del)) {
            len--;
        }
        return len < str.length() ? str.substring(0, len) : str;
    }


    /**
     * 匹配两个字符串中间的内容,下标1的内容
     *
     * @param strMailTemplateContent source
     * @param start                  start
     * @param end                    end
     * @return java.lang.String
     * @author justin.huang
     * @date 2022/1/28 14:48
     */
    public static String getMidStrEx(String strMailTemplateContent, String start, String end) {
        String rgex = start + "(.*?)" + end;
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(strMailTemplateContent);
        if (m.find()) {
            return m.group(1);
        }
        return "";

    }

    /**
     * 判断字符串是否全是数字，不用判断数字区间
     *
     * @param s 字符串
     * @return 是数字返回true，否则返回false
     */
    public static boolean isNumericlCheck(String s) {
        //根据ASCII判断:0-->48,以此类推
        char[] chars = s.toCharArray();
        boolean flag = true;
        for (char ch : chars) {
            if (ch < 48 || ch > 57) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    /**
     * 判断字符串是否符合对应位数的数字，判断32位的int和64位的long
     *
     * @param s 字符串
     * @param i 长度位数，如32位，64位，对应int,long
     * @return 是数字返回true，不是返回false
     */
    public static boolean isNumericlCheck(String s, int i) {
        boolean b = false;
        if (i == NUM_32BIT) {
            try {
                Integer.parseInt(s);
                b = true;
            } catch (NumberFormatException e) {
                return b;
            }
        }
        if (i == NUM_64BIT) {
            try {
                Long.parseLong(s);
                b = true;
            } catch (NumberFormatException e) {
                return b;
            }
        }
        return b;
    }


    /**
     * 判断多个字符值是否超过长度，是否超过个数
     *
     * @param s      需要进行分割的字段，按照英文逗号分割: ,
     * @param count  子串数量
     * @param length 子串长度
     * @return 超过返回true，否则返回false
     */
    public static boolean isOverMaxCountAndLength(String s, int count, int length) {
        boolean flag = false;
        String[] splitStrs = s.split(",");
        int countflag = 0;
        for (String str : splitStrs) {
            countflag++;
            if (str.length() > length || countflag > count) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    /**
     * 字符串数组去重，并去除其中为""或者null的字符串，可修改算法进一步优化
     *
     * @param strings 字符数组
     * @return 返回去重后的List
     */
    public static ArrayList<String> distinctAndNotEmptyString(String[] strings) {
        List<String> arrayList = Arrays.asList(strings);
        ArrayList<String> stringArrayList = new ArrayList<>(arrayList);
        Set<String> uniqueSet = new HashSet<>(stringArrayList);
        uniqueSet.remove("");
        stringArrayList.clear();
        stringArrayList.addAll(uniqueSet);
        return stringArrayList;
    }


    /**
     * 判断字符是否包含HTML标签
     *
     * @param s :
     * @return :
     */
    public static boolean isContainHtml(String s) {
        Pattern patternHtml = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        //find()方法是部分匹配，是查找输入串中与模式匹配的子串
        //matches()是全部匹配，是将整个输入串与模式匹配，如果要验证一个输入的数据是否为数字类型或其他类型，一般要用matches()
        return patternHtml.matcher(s).find();
    }
}
