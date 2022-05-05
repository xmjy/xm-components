package indi.xm.component.libcommon.model.enums;

import indi.xm.component.libcommon.exception.ErrorCodeEnumTag;

/**
 * 异常码枚举（5位，可数字、大小写字母）
 * 第一位区分项目
 * 2-3位区分业务
 * 4-5位区分异常
 *
 * @author: albert.fang
 * @date: 2021/10/22 10:02
 */
public enum CommonErrorCodeEnum implements ErrorCodeEnumTag {
    /**
     * 98-所有项目公共异常
     */
    //controller接口参数校验-- @valid注解抛出的异常，统一走这个异常码，异常信息为注解的异常信息
    PARAMETER_ERROR("9800", "参数异常"),
    //未定义异常码抛出的异常，统一走这个异常码，异常信息为抛出的异常携带的异常信息
    DYNAMIC_INFO_ERROR("9801", "动态信息异常"),
    SIGN_ERROR("9802", "sign签名不正确"),
    TOKEN_NOT_EMPTY("9803", "token不能为空"),
    TOKEN_INVALID("9804", "token无效"),
    TIMESTAMP_FORMAT_ERROR("9805", "时间戳格式不对"),
    TIMESTAMP_EXPIRE("9806", "超时请求"),
    /**
     * 99-系统级异常
     */
    SYSTEM_BUSY_ERROR("9999", "现在系统忙，请稍候再试！");

    /**
     * 错误码
     */
    private final String errorCode;
    /**
     * 错误信息
     */
    private final String errorMessage;

    CommonErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = getErrorCodePrefix() + errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 获取异常码前缀
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:45
     */
    public String getErrorCodePrefix() {
        return "C";
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
