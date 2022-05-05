package indi.xm.component.libcommon.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5加密工具类
 *
 * @author: albert.fang
 * @date: 2021/10/18 17:29
 */
public class Md5Util {

    /**
     * md5加密（大写）
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2021/10/18 17:30
     */
    public static String md5Upper(String data) {
        return DigestUtils.md5Hex(data).toUpperCase();
    }

    /**
     * md5加密（小写）
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2021/10/18 17:30
     */
    public static String md5Lower(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 两次md5加密（大写）
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2021/10/18 17:30
     */
    public static String md5UpperDouble(String data) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(data).toUpperCase()).toUpperCase();
    }

    /**
     * 两次md5加密（小写）
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2021/10/18 17:31
     */
    public static String md5LowerDouble(String data) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(data));
    }
}