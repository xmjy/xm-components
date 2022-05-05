package indi.xm.component.libcommon.exception;

/**
 * 自定义异常码枚举请实现这个接口
 *
 * @author: albert.fang
 * @date: 2022/3/9 16:22
 */
public interface ErrorCodeEnumTag {
    /**
     * 获取异常码
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:23
     */
    String getErrorCode();

    /**
     * 获取异常信息
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/3/9 16:23
     */
    String getErrorMessage();
}
