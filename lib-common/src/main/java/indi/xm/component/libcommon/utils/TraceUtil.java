package indi.xm.component.libcommon.utils;
import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
import com.sensorsdata.analytics.javasdk.bean.EventRecord;
import com.sensorsdata.analytics.javasdk.bean.SuperPropertiesRecord;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * 埋点上报
 *
 * @author albert.fang
 * @date 2022/3/30 10:51
 */
@Slf4j
public class TraceUtil {
    private final ISensorsAnalytics iSensorsAnalytics;

    public TraceUtil(ISensorsAnalytics iSensorsAnalytics){
        this.iSensorsAnalytics = iSensorsAnalytics;
    }

    public void track(Map<String,Object> commonParamsMap,String distinctId,Boolean isLoginId,String eventName,Map<String,Object> properties){
        EventRecord eventRecord;
        try {
            //设置公共属性,以后上传的每一个事件都附带该属性
            SuperPropertiesRecord propertiesRecord = SuperPropertiesRecord.builder().
                    addProperties(commonParamsMap)
                    .build();
            iSensorsAnalytics.registerSuperProperties(commonParamsMap);
            eventRecord = EventRecord.builder().setDistinctId(distinctId).isLoginId(isLoginId).setEventName(eventName).addProperties(properties).build();
            iSensorsAnalytics.track(eventRecord);
        } catch (InvalidArgumentException e) {
            log.error("神策日志上报出错：" + e.toString());
        }
    }

}
