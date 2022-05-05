package indi.xm.component.libcommon.model.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author xu.zhipeng
 * @ProjectName javamicroservice
 * @Description 统一返回结果集
 * @time 2021/10/21 10:24
 */
@ToString
@Getter
public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS("1", "200", "成功"),
    /**
     * 失败
     */
    FAIL("0", "201", "失败");

    private final String result;
    private final String code;
    private final String msg;


    ResultCodeEnum(String result, String code, String msg) {
        this.result = result;
        this.code = code;
        this.msg = msg;
    }
}
