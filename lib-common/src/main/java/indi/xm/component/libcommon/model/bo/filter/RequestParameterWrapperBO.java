package indi.xm.component.libcommon.model.bo.filter;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedCaseInsensitiveMap;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * 处理请求参数
 *
 * @author: albert.fang
 * @date: 2022/3/7 15:38
 */
@Slf4j
@ToString
public class RequestParameterWrapperBO extends HttpServletRequestWrapper {
    private final LinkedCaseInsensitiveMap<String[]> map = new LinkedCaseInsensitiveMap<>();
    /**
     * 请求携带的参数
     */
    private final byte[] requestBodySource;
    /**
     * 过滤后的参数
     */
    private byte[] requestBodyTarget;

    public byte[] getRequestBodySource() {
        return this.requestBodySource;
    }

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestParameterWrapperBO(HttpServletRequest request) throws IOException {
        super(request);
        //get和post参数,无requestBody参数
        map.putAll(request.getParameterMap());
        int contentLength = request.getContentLength() == -1 ? 0 : request.getContentLength();
        requestBodySource = new byte[contentLength];
        readRequestBody(request);
    }

    /**
     * 从request流读取body的参数
     *
     * @author cheng.liang
     * @date 2021/12/28 16:20
     */
    private void readRequestBody(ServletRequest request) throws IOException {
        InputStream in = request.getInputStream();
        int len;
        int index = 0;
        byte[] data = new byte[1024];
        while ((len = in.read(data)) != -1) {
            System.arraycopy(data, 0, requestBodySource, index, len);
            index += len;
        }
        requestBodyTarget = filterRequestBody(requestBodySource);
    }

    /**
     * 过滤requestBody参数
     *
     * @param requestBodySource:
     * @return :
     * @author: albert.fang
     * @date: 2022/3/7 15:58
     */
    protected byte[] filterRequestBody(byte[] requestBodySource) {
        return requestBodySource;
    }

    @Override
    public String getCharacterEncoding() {
        String encode = super.getCharacterEncoding();
        return encode == null ? StandardCharsets.UTF_8.name() : encode;
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
        return super.getParameterValues(parameter);
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
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(this.map);
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
        return super.getHeader(name);
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBodyTarget);
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
