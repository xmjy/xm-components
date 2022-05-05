package indi.xm.component.libcommon.utils;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author xu.zhipeng
 * @ProjectName javamicroservice
 * @Description 使用51job公共组件的Socket类
 * @time 2021/11/25 13:41
 */
public class SocketUtil {

    private static final Logger log = LoggerFactory.getLogger(SocketUtil.class);
    public static final String CHECK_END = "1";


    /**
     * Socket连接获取响应结果
     *
     * @param host :
     * @param port :
     * @param sendBuf :
     * @param arrParams :
     * @param standardCharsets :
     * @return :
     */
    public static String getSocketResult(String host, int port, String sendBuf, Map<String, Object> arrParams, @Nullable Charset... standardCharsets) {
        StringBuilder res = new StringBuilder();
        String checkEnd = arrParams.getOrDefault("check_end", "").toString();
        String nLen = arrParams.getOrDefault("n_len", "0").toString();

        Socket client = null;
        try {
            if (StringUtils.isEmpty(host)) {
                log.error("未获取到Socket信息");
                return res.toString();
            }
            client = new Socket();
            //建立连接超时时间为5秒
            client.connect(new InetSocketAddress(host, port), 5000);
            //读数据超时时间 5秒
            client.setSoTimeout(5000);
            //建立连接后就可以往服务端写数据了
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
            //向主机发送请求
            dataOutputStream.writeBytes(sendBuf);
            dataOutputStream.flush();
            //接收数据
            InputStream inputStream = client.getInputStream();
            DataInputStream reader = new DataInputStream(inputStream);
            //等待服务端返回报文
            int count = 0;
            //总计等待5s时间
            while (inputStream.available() == 0) {
                try {
                    Thread.sleep(1000);
                    count++;
                    if (count > 3) {
                        log.error("getSocketResult方法等待服务端返回报文超时");
                        return "";
                    }
                } catch (InterruptedException e) {
                    log.error("Thread中断异常：" + e.getMessage(), e);
                    //当前线程中断异常，不处理，只记录，
                }
            }
            int dataLen = 0;
            if (CHECK_END.equals(checkEnd)) {
                byte[] receiveBytes = new byte[Integer.parseInt(nLen)];
                reader.read(receiveBytes);
                dataLen = unpackN(receiveBytes);
                if (dataLen <= 0) {
                    return "";
                }
            }
            byte[] receiveBytes = new byte[dataLen];
            int readCount = 0;
            while (true) {
                readCount += reader.read(receiveBytes, readCount, dataLen - readCount);
                if (CHECK_END.equals(checkEnd)) {
                    if (readCount >= dataLen) {
                        break;
                    }
                }
            }
            Charset charset = ObjectUtils.isEmpty(standardCharsets) ? StandardCharsets.UTF_8 : standardCharsets[0];
            res.append(new String(receiveBytes, charset));
            reader.close();
            inputStream.close();
            dataOutputStream.close();
        } catch (Exception e) {
            log.error("调用socket接口异常：{}", e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    log.error("Socket 关闭失败:{}", e.getMessage());
                }
            }
        }
        return res.toString();
    }


    /**
     * 将数据长度装入二进制字符
     *
     * @param data 数据长度
     * @return 前端字符串
     */
    public static String packN(int data) {
        //大端字节序->高位字节在前，低位字节在后;因为packN打包后的是无符号的字节，所以用short代替byte
        short[] bytes = new short[4];
        bytes[3] = (short) (data & 0xff);
        bytes[2] = (short) ((data & 0xff00) >> 8);
        bytes[1] = (short) ((data & 0xff0000) >> 16);
        bytes[0] = (short) ((data & 0xff000000) >> 24);
        //byte转字符
        char[] chars = new char[4];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char) bytes[i];
        }
        return new String(chars);
    }


    /**
     * 将包装的二进制字符就拆包为具体的数据长度
     *
     * @param bytes 二进制字符
     * @return 对应的数据长度
     */
    public static int unpackN(byte[] bytes) {
        int length = 4;
        if (bytes.length < length) {
            return 0;
        }
        return (0xff & bytes[3])
                | (0xff00 & (bytes[2] << 8))
                | (0xff0000 & (bytes[1] << 16))
                | (0xff000000 & (bytes[0] << 24));
    }

}
