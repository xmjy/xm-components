package indi.xm.component.libcommon.config;

import indi.xm.component.libcommon.utils.JacksonUtil;
import indi.xm.component.libcommon.utils.StaticPropertiesUtil;
import indi.xm.component.libcommon.utils.TraceUtil;
import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.consumer.FastBatchConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 神策埋点
 *
 * @author albert.fang
 * @date 2022/3/28 15:15
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "sensorsServerUrl")
public class SensorsConfig {

    @Bean(destroyMethod = "shutdown")
    public ISensorsAnalytics init() {
        String sensorsServerUrl = StaticPropertiesUtil.getProperty("sensorsServerUrl");
        return new SensorsAnalytics(new FastBatchConsumer(sensorsServerUrl, failedData -> {
            //收集发送失败的数据
            log.error("日志同步到神策失败,未成功上报数据：" + JacksonUtil.toJsonString(failedData));
        }));
    }

    @Bean
    public TraceUtil traceUtil(ISensorsAnalytics iSensorsAnalytics){
        return new TraceUtil(iSensorsAnalytics);
    }
}