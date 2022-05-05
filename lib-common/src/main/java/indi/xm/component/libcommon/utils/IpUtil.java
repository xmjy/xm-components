package indi.xm.component.libcommon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author kuikui.he
 * @date 2019年8月8日下午5:15:00
 * @Description:
 */
@Component
public class IpUtil {
    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
    private static final String UNKNOW = "unknown";
    private static final String WIN10IP = "0:0:0:0:0:0:0:1";
    private static final String LOCALIP = "127.0.0.1";
    private static final String COMMA = ",";
    private static final int LENGTH = 15;

    /**
     * 获取当前网络ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        //根据公司定义的请求头获取ip
        String nsClientIp = request.getHeader("ns_clientip");
        if (!EhireStringUtil.isEmpty(nsClientIp)) {
            return nsClientIp;
        }
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOW.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOW.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOW.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOW.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOW.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALIP.equals(ipAddress) || WIN10IP.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    logger.error("IpUtil getHostAddress error");
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        // "***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > LENGTH) {
            if (ipAddress.indexOf(COMMA) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(COMMA));
            }
        }
        return ipAddress;
    }

    /**
     * 校验内网ip
     *
     * @param request :
     * @param innerAuthorizedIpFilter :
     * @return :
     */
    public static boolean checkInnerIp(HttpServletRequest request, String innerAuthorizedIpFilter) {
        boolean isValidIp = false;
        if (EhireStringUtil.isEmpty(innerAuthorizedIpFilter)) {
            logger.info("检查内网IP时InnerAuthorizedIPFilter为空");
            return false;
        }
        // 配直节点中IP或域名过滤值，如：192.168.1.*,10.100.*.*
        String[] arrFilters = innerAuthorizedIpFilter.split(",");
        // 获取域名
        String strDomain = request.getServerName();
        // 单个过滤值分段
        String[] arrFilterBlocks = null;
        // 用户客户端IP地址分段
        String[] arrClientIpBlocks = null;
        for (String filter : arrFilters) {
            arrFilterBlocks = filter.split("\\.");
            arrClientIpBlocks = IpUtil.getIpAddr(request).trim().split("\\.");

            // 1. IP合法性过滤
            if (arrFilterBlocks.length > 0 && arrFilterBlocks.length == arrClientIpBlocks.length) {
                int iRight = 0;
                for (int i = 0; i < arrFilterBlocks.length; i++) {
                    // 过滤器IP节点不等于*且与客户端IP地址不相等执行下一个循环
                    if ("*".equals(arrFilterBlocks[i]) || arrFilterBlocks[i].equals(arrClientIpBlocks[i])) {
                        iRight++;
                    } else {
                        break;
                    }
                }
                if (arrFilterBlocks.length == iRight) {
                    isValidIp = true;
                }
            }

            // 2. 域名合法性过滤（支持通配符：*.51job.com;*.ehire.51job.com）
            if (!EhireStringUtil.isEmpty(strDomain)) {
                if (filter.contains("*")) {
                    // 从最后一个通配符后一位位置开始截取
                    int iStartPos = filter.lastIndexOf("*") + 1;
                    String strSearchWords = "";

                    // 防止截取位置超过字符串的字符最大位
                    if (iStartPos < filter.length()) {
                        strSearchWords = filter.substring(iStartPos);
                    }
                    if (strSearchWords.trim().length() > 0 && strDomain.contains(strSearchWords)) {
                        isValidIp = true;
                    }
                } else {
                    if (strDomain.equals(filter)) {
                        isValidIp = true;
                    }
                }
            }
        }
        return isValidIp;
    }
}