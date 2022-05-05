package indi.xm.component.libcommon.model.vo;

import indi.xm.component.libcommon.model.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一接口响应格式
 *
 * @author: albert.fang
 * @date: 2022/2/23 9:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> {

    /**
     * 返回最终结果，0表示失败，1表示成功
     */
    private String result;
    /**
     * 返回状态码
     */
    private String code;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 返回消息
     */
    private String msg;

    /**
     * 成功
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:48
     */
    public static <T> Result<T> success() {
        return Result.success(null);
    }

    /**
     * 成功
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:49
     */
    public static <T> Result<T> success(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * 失败
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:49
     */
    public static <T> Result<T> fail() {
        return Result.fail(null);
    }

    /**
     * 失败
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:50
     */
    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }

    /**
     * 失败
     *
     * @param data:
     * @param code:
     * @param message:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:49
     */
    public static <T> Result<T> fail(T data, String code, String message) {
        Result<T> result = Result.fail();
        result.setCode(code);
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败
     *
     * @param code:
     * @param message:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:49
     */
    public static <T> Result<T> fail(String code, String message) {
        return Result.fail(null, code, message);
    }

    /**
     * 构建
     *
     * @param data:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:50
     */
    private static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        //包含data为数组或集合为空时，则赋值为null
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    /**
     * 构建
     *
     * @param data:
     * @param resultCodeEnum:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/23 9:50
     */
    private static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(data);
        result.setResult(resultCodeEnum.getResult());
        result.setCode(resultCodeEnum.getCode());
        result.setMsg(resultCodeEnum.getMsg());
        return result;
    }

}
