package com.common.util.encryptionalgorithm;

import com.common.exception.GlobalException;
import com.common.signature.RsaSignature;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtils {
    public static String publicKey;
    public static String privateKey;
    //加签、验签算法
    static String alg = "SHA256WithRSA";

    static {
        try{
            //初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            publicKey = Base64Utils.encodeToString(rsaPublicKey.getEncoded());
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
            privateKey = Base64Utils.encodeToString(rsaPrivateKey.getEncoded());
        }catch(Exception ex){
            throw new GlobalException("初始化密钥失败！", ex);
        }
    }

    /**
     * 加签
     * @param privateKey
     * @param src
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] sign(String privateKey, byte[] src) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        //执行签名
        // PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
        //         new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
                new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(alg);
        signature.initSign(priKey);
        signature.update(src);
        byte[] result = signature.sign();
        return result;
    }

    /**
     * 验签
     * @param publicKey
     * @param src
     * @param sign
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws CertificateException
     */
    public static boolean verify(String publicKey, byte[] src, byte[] sign) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException, CertificateException {
        // //验证签名
        // X509EncodedKeySpec x509EncodedKeySpec =
        //         new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        X509EncodedKeySpec x509EncodedKeySpec =
                new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance(alg);
        signature.initVerify(pubKey);
        signature.update(src);
        return signature.verify(sign);
    }

    public static String encrypt(String publicKey, String srcData) throws Exception{
        if(publicKey != null){
            try{
                // 获取公钥
                X509EncodedKeySpec x509EncodedKeySpec =
                        new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey));
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);
                //使用RSA公钥加密
                Cipher cipher = Cipher.getInstance("RSA");
                //根据公钥，对Cipher对象进行初始化
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                //加密，结果保存进resultBytes，并返回
                byte[] resultBytes = cipher.doFinal(srcData.getBytes());
                return Base64.getEncoder().encodeToString(resultBytes);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public static String decrypt(String privateKey, String encryptedData){
        if(privateKey != null){
            try{
                //获取私钥
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
                        new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey));
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                //使用RSA私钥解密
                Cipher cipher = Cipher.getInstance("RSA");
                //根据私钥对Cipher对象进行初始化
                cipher.init(Cipher.DECRYPT_MODE, priKey);
                //解密并将结果保存进resultBytes
                byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
                return new String(decryptedData);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    private String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        String data = "Hello Dylan dfetrfv人民路多么了";
        String encryptedData = RSAUtils.encrypt(RSAUtils.publicKey, data);
        System.out.println("加密后的数据: " + encryptedData);
        String decryptedData = RSAUtils.decrypt(RSAUtils.privateKey, encryptedData);
        System.out.println("解密后的数据: " + decryptedData);

        String signData = Base64.getEncoder().encodeToString(RSAUtils.sign(RSAUtils.privateKey, data.getBytes()));
        System.out.println("sign数据: " + signData);
        boolean result = RSAUtils.verify(RSAUtils.publicKey, data.getBytes(), Base64.getDecoder().decode(signData));
        System.out.println("验签结果："+ result);
    }
}
