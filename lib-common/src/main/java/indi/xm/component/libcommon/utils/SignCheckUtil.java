package indi.xm.component.libcommon.utils;

import indi.xm.component.libcommon.exception.ServiceException;
import indi.xm.component.libcommon.model.enums.CommonErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: albert.fang
 * @date: 2022/2/22 14:16
 */
@Slf4j
public class SignCheckUtil {

    public static final boolean SIGN_ERROR = false;
    public static final boolean SIGN_CORRECT = true;
    public static final String TIMESTAMP_NAME = "timestamp";
    public static final String SIGN_NAME = "sign";
    public static final int TIMESTAMP_LENGTH = 10;

    private static final String INNER_E_HIRE_API_KEY = StaticPropertiesUtil.getProperty("innerehire_java_api_key");
    private static final String OPEN_E_HIRE_API_KEY = StaticPropertiesUtil.getProperty("openehire_java_api_key");

    /**
     * 校验sign签名是否正确(外网校验，带headerToken)
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/23 16:42
     */
    public static boolean checkSign(Map<String, Object> requestParam, String headerToken) {
        //校验时间戳
        String time = String.valueOf(requestParam.get(TIMESTAMP_NAME));
        if (time == null || time.length() != TIMESTAMP_LENGTH) {
            throw new ServiceException(CommonErrorCodeEnum.TIMESTAMP_FORMAT_ERROR);
        }
        //校验sign
        String sign = String.valueOf(requestParam.get(SIGN_NAME));
        String newKey = generateSign(requestParam, headerToken);
        if (sign == null || !sign.equalsIgnoreCase(newKey)) {
            return SIGN_ERROR;
        }
        return SIGN_CORRECT;
    }

    /**
     * 校验sign签名是否正确（内网校验，不带headerToken）
     *
     * @param requestParam:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/22 14:21
     */
    public static boolean checkSign(Map<String, Object> requestParam) {
        return checkSign(requestParam, "");
    }

    /**
     * 外网生成sign
     *
     * @param requestParam:
     * @param headerToken:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/22 14:25
     */
    public static String generateSign(Map<String, Object> requestParam, String headerToken) {
        String key = OPEN_E_HIRE_API_KEY;
        if (headerToken == null || "".equals(headerToken)) {
            headerToken = "";
            key = INNER_E_HIRE_API_KEY;
        }
        String paramBodyOrderStr = headerToken + getOrderedKeys(requestParam) + key;
        return Md5Util.md5LowerDouble(paramBodyOrderStr);
    }

    /**
     * 内网生成sign
     *
     * @param requestParam:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/22 14:26
     */
    public static String generateSign(Map<String, Object> requestParam) {
        return generateSign(requestParam, "");
    }

    /**
     * 生成10位时间戳
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/2/22 14:24
     */
    public static long generateTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    private static String getOrderedKeys(Map<String, Object> parameterMap) {
        List<String> list = new ArrayList<>();
        for (String s : parameterMap.keySet()) {
            if (!SIGN_NAME.equals(s)) {
                list.add(s);
            }
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (String o : list) {
            sb.append(JacksonUtil.toJsonString(parameterMap.get(o)));
        }
        return sb.toString();
    }
}
