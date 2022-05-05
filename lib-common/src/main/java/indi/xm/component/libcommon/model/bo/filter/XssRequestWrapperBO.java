package indi.xm.component.libcommon.model.bo.filter;

import indi.xm.component.libcommon.utils.EhireStringUtil;
import indi.xm.component.libcommon.utils.XssUtil;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * xss参数处理
 *
 * @author: albert.fang
 * @date: 2022/3/7 15:34
 */
@Slf4j
@ToString
public class XssRequestWrapperBO extends RequestParameterWrapperBO {
    /**
     * 构造xss请求包装器
     *
     * @param request:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/7 16:03
     */
    public XssRequestWrapperBO(HttpServletRequest request) throws IOException {
        super(request);
    }

    @Override
    protected byte[] filterRequestBody(byte[] requestBodySource) {
        try {
            String text = new String(requestBodySource, getCharacterEncoding());
            if (!EhireStringUtil.isEmpty(text)) {
                text = XssUtil.xssEncode(text);
                return text.getBytes(getCharacterEncoding());
            }
        } catch (Exception e) {
            log.error("filterRequestBody 异常：" + e);
        }
        return requestBodySource;
    }

    /**
     * 删除空白字符
     *
     * @param parameter 参数名称
     * @return java.lang.String[]
     * @author Andy.liu
     * @date 2020/3/26 20:13
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] results = super.getParameterValues(parameter);
        if (null == results) {
            return null;
        }
        int count = results.length;
        String[] trimResults = new String[count];
        for (int i = 0; i < count; i++) {
            if (EhireStringUtil.isEmpty(results[i])) {
                trimResults[i] = results[i];
            } else {
                trimResults[i] = XssUtil.xssEncode(results[i]);
            }
        }
        return trimResults;
    }

    /**
     * 覆盖getParameter方法，将请求参数名和参数值都做xss过滤
     *
     * @param name 获取参数名称
     * @return java.lang.String
     * @author Andy.liu
     * @date 2020/3/26 20:13
     */
    @Override
    public String getParameter(String name) {
        //检验参数是否为空为或为null
        String value = super.getParameter(XssUtil.xssEncode(name));
        if (!EhireStringUtil.isEmpty(value)) {
            value = XssUtil.xssEncode(value);
        }
        return value;
    }

    /**
     * 覆盖getHeader方法，将请求头中参数名和参数值都做xss过滤
     *
     * @param name 名称
     * @return java.lang.String
     * @author Andy.liu
     * @date 2020/3/26 20:15
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(XssUtil.xssEncode(name));
        if (value != null) {
            value = XssUtil.xssEncode(value);
        }
        return value;
    }
}
