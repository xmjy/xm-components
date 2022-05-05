package indi.xm.component.dbsource.util;

import indi.xm.component.dbsource.DataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author albert.fang
 */
@Component
public class Aes {

    /**
     * AES-128-CBC, 默认方式加密，使用PKCS5Padding方式填充
     *
     * @param data String 要加密的字符串
     * @param key  String 加密用key，不可为空
     * @param iv   String 加密用iv，不可为空
     * @return String 加密结果，Base64编码
     */
    public String encrypt(String data, String key, String iv) {
        return this.encrypt(data, key, iv, "AES/CBC/PKCS5Padding");
    }

    /**
     * AES-128-CBC, 默认方式解密，使用PKCS5Padding方式填充
     *
     * @param data String 要加密的字符串
     * @param key  String 加密用key，不可为空
     * @param iv   String 加密用iv，不可为空
     * @return String 加密结果，Base64编码
     */
    public String decrypt(String data, String key, String iv) {
        return this.decrypt(data, key, iv, "AES/CBC/PKCS5Padding");
    }

    /**
     * AES-128-CBC, 加密使用AES/CBC/NoPadding方式填充，对应PHP中的OPENSSL_ZERO_PADDING模式加密
     *
     * @param data String 要加密的字符串
     * @param key  String 加密用key，不可为空
     * @param iv   String 加密用iv，不可为空
     * @return String 加密结果，Base64编码
     */
    public String encryptWithNoPadding(String data, String key, String iv) {
        return this.encrypt(data, key, iv, "AES/CBC/NoPadding");
    }

    /**
     * AES-128-CBC,加密使用AES/CBC/NoPadding方式填充，对应PHP中的OPENSSL_ZERO_PADDING模式解密
     *
     * @param data String 待解密的内容
     * @param key  String 加密用key，不可为空
     * @param iv   String 加密用iv，不可为空
     * @return String 解密结果
     */
    public String decryptWithNoPadding(String data, String key, String iv) {
        return this.decrypt(data, key, iv, "AES/CBC/NoPadding");
    }

    /**
     * AES加密
     *
     * @param data           要加密的数据
     * @param key            秘钥
     * @param iv             偏移量
     * @param transformation 算法
     * @return 解密结果
     */
    public String encrypt(String data, String key, String iv, String transformation) {

        assert StringUtils.hasText(key) && StringUtils.hasText(iv) && StringUtils.hasText(transformation);

        if (!StringUtils.hasText(data)) {
            return "";
        }

        try {
            byte[] keyBytes = key.getBytes();
            Cipher cipher = Cipher.getInstance(transformation);
            byte[] plaintext;
            if ("AES/CBC/NoPadding".equals(transformation)) {
                int blockSize = cipher.getBlockSize();
                byte[] dataBytes = data.getBytes();
                int length = dataBytes.length;
                //计算需填充长度
                if (length % blockSize != 0) {
                    length = length + (blockSize - (length % blockSize));
                }
                plaintext = new byte[length];
                //填充
                System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            } else {
                plaintext = data.getBytes();
            }

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(iv.getBytes()));
            return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException e) {
            DataSource.logger.error("加密失败，原始字符：" + data + "。错误信息：" + e.getMessage());
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param data           待解密字符串
     * @param key            秘钥
     * @param iv             偏移量
     * @param transformation 算法
     * @return 解密结果
     */
    public String decrypt(String data, String key, String iv, String transformation) {

        assert StringUtils.hasText(key) && StringUtils.hasText(iv) && StringUtils.hasText(transformation);

        if (!StringUtils.hasText(data)) {
            return "";
        }

        try {
            byte[] keyBytes = key.getBytes();
            Cipher cipher = Cipher.getInstance(transformation);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), ivParameterSpec);
            String decryptData = new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes())));
            return decryptData.trim();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException e) {
            DataSource.logger.error("解密失败，原始字符：" + data);
            return null;
        }
    }
}