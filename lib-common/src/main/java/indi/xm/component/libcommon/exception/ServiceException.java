package indi.xm.component.libcommon.exception;

import indi.xm.component.libcommon.model.enums.CommonErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一服务异常类
 *
 * @author: albert.fang
 * @date: 2022/3/9 16:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ServiceException extends RuntimeException {
    private ErrorCodeEnumTag baseException;
    private String errorCode;
    private String message;
    private Object data;

    /**
     * 基本公共异常构造
     *
     * @param baseException:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:14
     */
    public ServiceException(ErrorCodeEnumTag baseException) {
        super();
        this.baseException = baseException;
        this.errorCode = baseException.getErrorCode();
        this.message = baseException.getErrorMessage();
    }

    /**
     * 基本公共异常构造
     *
     * @param baseException:
     * @param throwable:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:15
     */
    public ServiceException(ErrorCodeEnumTag baseException, Throwable throwable) {
        super(throwable);
        this.baseException = baseException;
        this.errorCode = baseException.getErrorCode();
        this.message = baseException.getErrorMessage();
    }

    /**
     * 基本公共异常构造
     *
     * @param errorCode:
     * @param errorMessage:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:15
     */
    public ServiceException(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.message = errorMessage;
    }

    /**
     * 动态异常信息
     *
     * @param errorMessage:
     * @author: albert.fang
     * @date: 2021/12/9 14:16
     */
    public ServiceException(String errorMessage) {
        super();
        this.errorCode = CommonErrorCodeEnum.DYNAMIC_INFO_ERROR.getErrorCode();
        this.message = errorMessage;
    }

    /**
     * 含有数据的异常
     *
     * @param errorCode:
     * @param errorMessage:
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:15
     */
    public ServiceException(String errorCode, String errorMessage, Object data) {
        super();
        this.errorCode = errorCode;
        this.message = errorMessage;
        this.data = data;
    }

}
