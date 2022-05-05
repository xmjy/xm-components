package indi.xm.component.libcommon.config;

import feign.Request;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: albert.fang
 * @date: 2022/1/13 15:09
 */
@Configuration
@ConditionalOnMissingBean(name = "openfeignConfig")
public class OpenfeignConfig {
    /**
     * 重试次数
     */
    @Value("${feign.retry.retryCount:3}")
    private int retryCount;

    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @ConditionalOnMissingBean(name = "feignFormEncoder")
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Autowired
    public void setMessageConverters(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public Retryer feignRetryer(){
        // period=100 发起当前请求的时间间隔,单位毫秒。请求真正的时间间隔与当前的尝试次数和period有关，但不会大于maxPeriod。
        // maxPeriod=1000 发起当前请求的最大时间间隔,单位毫秒
        // maxAttempts=4 重试次数是3，因为包括第一次
        return new CustomRetry(100, 1000, retryCount + 1);
    }

    /**
     * 自定义的重试器
     *
     * @author: albert.fang
     * @date: 2022/3/10 17:03
     */
    static class CustomRetry extends Retryer.Default {

        CustomRetry(long period, long maxPeriod, int maxAttempts){
            super(period, maxPeriod, maxAttempts);
        }

        @Override
        public void continueOrPropagate(RetryableException e) {
            if(!e.method().equals(Request.HttpMethod.GET)){
                throw e;
            }
            super.continueOrPropagate(e);
        }
    }
}
