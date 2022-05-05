package indi.xm.component.libcommon.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 敏感信息加解密工具类
 *
 * @author: albert.fang
 * @date: 2021/9/22 9:38
 */
public class EncryptInfoUtil {

    private final String KEY;
    private final String IV;
    public static final int MIN_DESENSITIZE_LENGTH = 6;

    public EncryptInfoUtil(String keyFilePath) {
        File file = new File(keyFilePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在：path=" + keyFilePath);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            KEY = reader.readLine();
            IV = reader.readLine();
            if (KEY == null || IV == null) {
                throw new RuntimeException("密钥不能为空");
            }
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败，文件路径:" + keyFilePath);
        }
    }

    public EncryptInfoUtil(String key, String iv) {
        KEY = key;
        IV = iv;
    }


    /**
     * 加密
     *
     * @param text:
     * @return :
     * @author: albert.fang
     * @date: 2021/9/22 10:13
     */
    public String encrypt(String text) {
        return encrypt(text, KEY, IV);
    }

    /**
     * 解密
     *
     * @param text:
     * @return :
     * @author: albert.fang
     * @date: 2021/9/22 10:13
     */
    public String decrypt(String text) {
        return decryptEncryptedText(text, KEY, IV);
    }


    /**
     * 加密
     *
     * @param text: 待加密文本
     * @param key:  密钥
     * @param iv:   iv参数
     * @return : 加密后的结果
     * @author: albert.fang
     * @date: 2021/9/22 9:39
     */
    public String encrypt(String text, String key, String iv) {
        if (text == null || "".equals(text)) {
            return "";
        }
        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8), 0, ciper.getBlockSize());
            ciper.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedCiperBytes = Base64.getEncoder().encode((ciper.doFinal(text.getBytes(StandardCharsets.UTF_8))));
            return new String(encryptedCiperBytes, StandardCharsets.UTF_8);
        } catch (Throwable ex) {
            return "";
        }
    }

    /**
     * 解密
     *
     * @param text: 待解密文本
     * @param key:  密钥
     * @param iv:   iv参数
     * @return : 解密后的结果
     * @author: albert.fang
     * @date: 2021/9/22 9:39
     */
    public String decrypt(String text, String key, String iv) {
        if (text == null || "".equals(text)) {
            return "";
        }
        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8), 0, ciper.getBlockSize());
            ciper.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedCiperBytes = ciper.doFinal(Base64.getDecoder().decode(text));
            return new String(decryptedCiperBytes, StandardCharsets.UTF_8);
        } catch (Throwable ex) {
            return text;
        }
    }

    /**
     * 先判断字符串是否已加密，若已加密，则先解密再返回；若未加密，则直接返回
     *
     * @param text: 待解密文本
     * @param key:  密钥
     * @param iv:   iv参数
     * @return : 解密后的结果
     * @author: albert.fang
     * @date: 2021/9/23 11:13
     */
    public String decryptEncryptedText(String text, String key, String iv) {
        if (text == null || "".equals(text)) {
            return "";
        }
        return isBase64(text) ? decrypt(text, key, iv) : text;
    }

    /**
     * 脱敏
     *
     * @param text: 待脱敏文本
     * @return : 脱敏后的文本
     * @author: albert.fang
     * @date: 2021/9/24 9:42
     */
    public String desensitize(String text) {
        return desensitize(text, 3, 2);
    }

    /**
     * 脱敏
     *
     * @param text:              待脱敏文本
     * @param frontRemainLength: 前面保留的位数
     * @param backRemainLength:  后面保留的位数
     * @return : 脱敏后的文本
     * @author: albert.fang
     * @date: 2021/9/24 9:40
     */
    private static String desensitize(String text, int frontRemainLength, int backRemainLength) {
        if (text == null || "".equals(text)) {
            return "";
        }
        //保留位数必须大于0
        if (frontRemainLength < 0 || backRemainLength < 0) {
            return "";
        }
        //长度小于6的完全展示，可视为无效信息
        if (text.length() < MIN_DESENSITIZE_LENGTH) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i < frontRemainLength || i >= text.length() - backRemainLength) {
                stringBuilder.append(text.charAt(i));
            } else {
                stringBuilder.append("*");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 判断是否是base64字符串
     *
     * @param str: 字符串
     * @return : 是否是base64字符串
     * @author: albert.fang
     * @date: 2021/9/23 11:11
     */
    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }
}
