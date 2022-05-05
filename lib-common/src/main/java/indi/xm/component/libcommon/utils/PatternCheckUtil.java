package indi.xm.component.libcommon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Benjamin
 * @date 2021/12/27 12:30
 * @description:
 */
public class PatternCheckUtil {
    private static final String EMAIL_CHECK_REG = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*))@([A-Za-z0-9]+[-A-Za-z0-9_]*\\.)+[A-Za-z]{2,}$";
    private static final String CN_EN_NUM_REG = "[a-zA-Z0-9\u4E00-\u9FA5]*";
    private static final String URL_REGULAR = "(?i)(((https?|ftp)://)?((?<![@.][a-z0-9_-]{0,100})[a-z0-9_-]+|www)(\\.[a-z0-9_-]+)*\\.(com(\\.cn)?|net|cn|cc|tv|hk)|www\\.长头发\\.com)";
    private static final String URL_REGULAR_WHITE = "^(\\d{0,2}\\.)?(?i)(com(\\.cn)?|top|tech|cn|ink|biz|cc|info|name|tv|org|hk|ca|((asp|ado|vs|dot|vb|spring|mvc|accord|sap|(visual)?studio|(visual)?basic|php|unity|java|vc|visual|abp|add|adomd|k2|log4|aforge|ajax|akka|aod|castle|csharp|dapper|delphi|ext|erp|lucene|ml|microsoft|mybatis|osgi|solr|(vs|visualstudio)\\d{4}?)\\.)?net)$";

    /**
     * 验证手机号码
     *
     * @param mobile :
     * @return :
     */
    public static boolean isMobliePhone(String mobile) {
        //判断手机号(移动和联通和电信)
        String reg = "^(1[0-9]{10}$)";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(mobile).matches();
    }

    /**
     * 判断手机号是否高风险号段
     *
     * @param mobile :
     * @return :
     */
    public static boolean isSafeMobilePhone(String mobile) {
        //判断手机号是否是高风险170，171，165，167
        return !mobile.startsWith("170") && !mobile.startsWith("171") && !mobile.startsWith("165") && !mobile.startsWith("167");
    }

    /**
     * 判断是不是6位数字
     *
     * @param code :
     * @return :
     */
    public static boolean checkNumCodePattern(String code) {
        String reg = "^([0-9]{6}$)";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(code).matches();
    }

    public static boolean isNum(String str) {
        String reg = "^([0-9]*$)";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str).matches();
    }


    /**
     * 检测是否符合email格式
     *
     * @param email :
     * @return :
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_CHECK_REG);
        return pattern.matcher(email).matches();
    }

    /**
     * 获取中英文字符串实际长度
     *
     * @param str :
     * @return :
     */
    public static int getRealLength(String str) {
        int lenTotal = 0;
        int n = str.length();
        char strWord;
        for (int i = 0; i < n; i++) {
            strWord = str.charAt(i);
            if (strWord > 127) {
                lenTotal = lenTotal + 2;
            } else {
                lenTotal = lenTotal + 1;
            }
        }

        return lenTotal;
    }

    /**
     * 检测名字是否是由中文英文和字母组成
     *
     * @param name :
     * @return :
     */
    public static boolean isCnEnNum(String name) {
        Pattern pattern = Pattern.compile(CN_EN_NUM_REG);
        return pattern.matcher(name).matches();
    }

    /**
     * 正则验证是否匹配到网址 正则读取配置节点UrlRegular 再读取正则UrlRegularWhite去除白名单
     *
     * @param text 字符串文本
     * @return true 是 false否
     */
    public static boolean regularMatchUrl(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        Pattern url = Pattern.compile(URL_REGULAR, Pattern.CASE_INSENSITIVE);
        Pattern white = Pattern.compile(URL_REGULAR_WHITE, Pattern.CASE_INSENSITIVE);
        Matcher matcher = url.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
            String temp = matcher.group(0);
            boolean flag = white.matcher(temp).matches();
            if (!flag) {
                return true;
            } else {
                count--;
            }
        }
        return count > 0;
    }


}
