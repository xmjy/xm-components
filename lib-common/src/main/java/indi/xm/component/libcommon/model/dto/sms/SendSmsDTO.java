package indi.xm.component.libcommon.model.dto.sms;

import lombok.Data;

/**
 * @author: albert.fang
 * @date: 2022/2/10 16:25
 */
@Data
public class SendSmsDTO {
    private String systemType;
    private String mobileNum;
    private String sign;
    private String keyNum;
    private String msgType;
    private String content;
    private String customer;
}
