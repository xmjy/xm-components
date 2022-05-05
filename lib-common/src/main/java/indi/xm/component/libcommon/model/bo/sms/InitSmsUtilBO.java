package indi.xm.component.libcommon.model.bo.sms;

import indi.xm.component.libcommon.service.api.SmsCenterFeignClient;
import lombok.Builder;
import lombok.Data;

/**
 * @author: albert.fang
 * @date: 2022/2/10 14:34
 */
@Data
@Builder
public class InitSmsUtilBO {
    private String banMobiles;
    private SmsCenterFeignClient smsCenterFeignClient;
}
