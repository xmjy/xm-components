package indi.xm.component.libcommon.config;

import indi.xm.component.libcommon.model.bo.sms.InitSmsUtilBO;
import indi.xm.component.libcommon.service.api.SmsCenterFeignClient;
import indi.xm.component.libcommon.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: albert.fang
 * @date: 2022/2/10 15:02
 */
@Configuration
@ConditionalOnProperty(name = "ehire.sms.url.domain")
@EnableFeignClients("com.job51.ehire.component.libcommon.service.api")
public class SmsUtilConfig {
    @Value("${ehire.sms.banMobiles:}")
    private String banMobiles;

    @Resource
    private SmsCenterFeignClient smsCenterFeignClient;

    @Bean
    public SmsUtil smsUtil(InitSmsUtilBO initSmsUtilBO) {
        return new SmsUtil(initSmsUtilBO);
    }

    @Bean
    public InitSmsUtilBO initSmsUtilBO() {
        return InitSmsUtilBO.builder()
                .banMobiles(banMobiles)
                .smsCenterFeignClient(smsCenterFeignClient)
                .build();
    }
}
