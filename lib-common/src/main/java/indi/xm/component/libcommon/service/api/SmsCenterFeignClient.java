package indi.xm.component.libcommon.service.api;

import indi.xm.component.libcommon.model.dto.sms.SendSmsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: albert.fang
 * @date: 2022/2/10 15:59
 */
@FeignClient(name = "smsCenter", url = "${ehire.sms.url.domain}")
public interface SmsCenterFeignClient {
    /**
     * 描述
     *
     * @param sendSmsDTO:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/10 16:26
     */
    @PostMapping(value = "sms/SmsService.asmx/SendSmsNoCustomer", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    String sendMsg(SendSmsDTO sendSmsDTO);

    /**
     * 描述
     *
     * @param sendSmsDTO:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/10 16:26
     */
    @PostMapping(value = "sms/SmsService.asmx/SendSms_WithSign", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    String sendInternationalMsg(SendSmsDTO sendSmsDTO);
}
