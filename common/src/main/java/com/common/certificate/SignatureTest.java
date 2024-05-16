package com.common.certificate;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 数字签名
 */
public class SignatureTest {

    static RSAPublicKey rsaPublicKey;
    static RSAPrivateKey rsaPrivateKey;
    static {
        try{
            //初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
        }catch(Exception ex){
        }
    }

    public static byte[] sign(byte[] src) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException{
        //执行签名
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
                new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(src);
        byte[] result = signature.sign();
        return result;
    }

    public static boolean verify(byte[] src, byte[] sign) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        //验证签名
        X509EncodedKeySpec x509EncodedKeySpec =
                new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(src);
        return signature.verify(sign);
    }

    private static String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static void main(String[] args) throws Exception{
        //1，客户端发送请求
        //明文
        String srcStr = "数字信封";
        // 将明文数据进行sha256,获得明文摘要
        String signStrSha256Hex = DigestUtils.sha256Hex(decode(srcStr));
        System.out.println("客户端摘要:"+signStrSha256Hex);
        //对摘要加签，获取数字信封
        String signStr = Base64Utils.encodeToString(SignatureTest.sign(signStrSha256Hex.getBytes()));
        System.out.println("签名："+signStr);
        //将明文、数字信封发送给服务端



        //2，服务端验签
        // 将明文数据进行sha256,获得明文摘要
        String signStrSha256Hex1 = DigestUtils.sha256Hex(decode(srcStr));
        System.out.println("服务端摘要:"+signStrSha256Hex1);
        //验签
        boolean result = SignatureTest.verify(signStrSha256Hex1.getBytes(), Base64Utils.decodeFromString(signStr));
        System.out.println("验签结果："+result);
    }

}
