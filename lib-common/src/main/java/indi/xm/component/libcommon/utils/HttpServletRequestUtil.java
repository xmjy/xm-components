package indi.xm.component.libcommon.utils;

import indi.xm.component.libcommon.model.bo.filter.RequestParameterWrapperBO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: albert.fang
 * @date: 2021/12/31 9:41
 */
public class HttpServletRequestUtil {
    private static final String GET = "GET";

    /**
     * 获取request表单参数
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/23 16:42
     */
    public static Map<String, Object> getRequestFormParam(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>(16);
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> {
            if (value != null && value[0] != null) {
                data.put(key, value[0]);
            }
        });
        return data;
    }

    /**
     * 获取requestbody参数
     *
     * @param request:
     * @return : 对象
     * @author: albert.fang
     * @date: 2021/12/23 16:27
     */
    public static Map<String, Object> getRequestBodyParam(RequestParameterWrapperBO request) {
        Map<String, Object> data = new HashMap<>(16);
        try {
            String text = new String(request.getRequestBodySource(), request.getCharacterEncoding());
            data = JacksonUtil.jsonStringToMap(text);
        } catch (Exception ignore) {
        }
        return data;
    }

    /**
     * 获取请求对象
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/31 9:45
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new IllegalArgumentException("requestAttributes不能为空");
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获取header参数
     *
     * @param request:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/31 9:47
     */
    public static Map<String, Object> getHeaderParam(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>(16);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String nextHeaderName = headerNames.nextElement();
            String nextHeaderValue = request.getHeader(nextHeaderName);
            data.put(nextHeaderName, nextHeaderValue);
        }
        return data;
    }

    /**
     * 获取请求参数（表单或body参数）
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/1/13 15:42
     */
    public static Map<String, Object> getRequestParam(HttpServletRequest request) {
        Map<String, Object> requestParam = new HashMap<>(16);
        if (GET.equalsIgnoreCase(request.getMethod())) {
            requestParam = HttpServletRequestUtil.getRequestFormParam(request);
        } else {
            if (request instanceof RequestParameterWrapperBO) {
                RequestParameterWrapperBO requestWrapper = (RequestParameterWrapperBO) request;
                requestParam = HttpServletRequestUtil.getRequestBodyParam(requestWrapper);
            }
            if (requestParam.size() == 0) {
                requestParam = HttpServletRequestUtil.getRequestFormParam(request);
            }
        }
        return requestParam;
    }
}
