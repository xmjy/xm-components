package indi.xm.component.libcommon.service.api.interceptor;

import indi.xm.component.libcommon.service.api.SignCheckFeignClientTag;
import indi.xm.component.libcommon.utils.JacksonUtil;
import indi.xm.component.libcommon.utils.SignCheckUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 对于需要sign校验的feign接口，自动加上sign和timestamp参数(仅限调用内网接口)
 *
 * @author: albert.fang
 * @date: 2022/2/22 17:08
 */
@Component
public class SignCheckFeignInterceptor implements RequestInterceptor {
    private static final String GET = "GET";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    @Override
    public void apply(RequestTemplate template) {
        Class<?> type = template.feignTarget().type();
        if (SignCheckFeignClientTag.class.isAssignableFrom(type)) {
            String method = template.method();
            if (GET.equalsIgnoreCase(method)) {
                //GET请求
                long timestamp = SignCheckUtil.generateTimestamp();
                template.query(SignCheckUtil.TIMESTAMP_NAME, timestamp + "");
                Map<String, Object> requestParam = new HashMap<>(16);
                Map<String, Collection<String>> queries = template.queries();
                for (String name : queries.keySet()) {
                    Collection<String> values = queries.get(name);
                    if (values != null && values.size() > 0) {
                        requestParam.put(name, values.toArray()[0]);
                    }
                }
                String sign = SignCheckUtil.generateSign(requestParam);
                template.query(SignCheckUtil.SIGN_NAME, sign);
            } else {
                //POST请求
                Map<String, Collection<String>> headers = template.headers();
                Collection<String> contentTypes = headers.get(CONTENT_TYPE);
                if (contentTypes == null || contentTypes.size() == 0) {
                    throw new IllegalArgumentException("Content-Type不能为空");
                }
                String contentType = String.valueOf(contentTypes.toArray()[0]);
                if (contentType.contains(APPLICATION_JSON)) {
                    //POST请求-application/json
                    byte[] body = template.body();
                    String text = new String(body, StandardCharsets.UTF_8);
                    Map<String, Object> requestParam = JacksonUtil.jsonStringToMap(text);
                    long timestamp = SignCheckUtil.generateTimestamp();
                    requestParam.put(SignCheckUtil.TIMESTAMP_NAME, timestamp + "");
                    String sign = SignCheckUtil.generateSign(requestParam);
                    requestParam.put(SignCheckUtil.SIGN_NAME, sign);
                    String newText = JacksonUtil.toJsonString(requestParam);
                    template.body(newText.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                } else {
                    throw new IllegalArgumentException("未知的Content-Type:" + contentType);
                }
            }
        }
    }
}
