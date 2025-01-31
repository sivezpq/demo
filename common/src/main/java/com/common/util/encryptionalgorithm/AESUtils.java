package com.common.util.encryptionalgorithm;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {
    private static final String AES_ALGORITHM = "AES";
    // AES加密模式为CBC，填充方式为PKCS5Padding。
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    // AES密钥为16个字符。
    private static final String AES_KEY = "1234567890123456";
    // AES初始化向量为16个字符。
    private static final String AES_IV = "abcdefghijklmnop";

    /**
     * 使用AES算法加密数据。
     *
     * @param data 要加密的数据。
     * @return 加密后的数据，使用Base64编码。
     */
    public static String encrypt(String data) throws Exception{
        // 将AES密钥转换为SecretKeySpec对象。
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        // 将AES初始化向量转换为IvParameterSpec对象。
        IvParameterSpec ivParameterSpec = new IvParameterSpec(AES_IV.getBytes());
        // 获取AES算法的Cipher实例。
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        // 初始化Cipher为加密模式，设置密钥和初始化向量。
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        // 加密数据。
        byte[] encryptedData = cipher.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        // 使用Base64编码加密后的数据。
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 使用AES算法解密数据。
     *
     * @param encryptedData 使用Base64编码的加密数据。
     * @return 解密后的数据。
     */
    public static String decrypt(String encryptedData) throws Exception{
        // 将AES密钥转换为SecretKeySpec对象。
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(),AES_ALGORITHM);
        // 将AES初始化向量转换为IvParameterSpec对象。
        IvParameterSpec ivParameterSpec = new IvParameterSpec(AES_IV.getBytes());
        // 获取AES算法的Cipher实例。
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        // 初始化Cipher为解密模式，设置密钥和初始化向量。
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        // 使用Base64解码加密数据。
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        // 解密数据。
        byte[] decryptedData = cipher.doFinal(decodedData);
        // 返回解密后的数据。
        return new String(decryptedData,java.nio.charset.StandardCharsets.UTF_8);
    }
    public static void main(String[] args) throws Exception {
        String data = "Hello Dylan";
        String encryptedData = AESUtils.encrypt(data);
        System.out.println("加密后的数据: " + encryptedData);

        String decryptedData = AESUtils.decrypt(encryptedData);
        System.out.println("解密后的数据: " + decryptedData);
    }
}
