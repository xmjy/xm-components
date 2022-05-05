package indi.xm.component.libcommon.model.bo.sms;

import lombok.Data;

/**
 * 发送短信参数
 *
 * @author: albert.fang
 * @date: 2022/2/10 14:23
 */
@Data
public class SendSmsParamBO {
    private String systemType;
    private String mobileNum;
    private String sign;
    private String keyNum;
    private String msgType;
    private String content;
    private String customer;
}
