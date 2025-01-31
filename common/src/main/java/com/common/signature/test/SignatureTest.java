package com.common.signature.test;

import com.common.signature.KeyGenerator;
import com.common.signature.RsaSignature;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 数字签名
 */
public class SignatureTest {

    public static void main(String[] args) throws Exception{
        //创建密钥
        KeyGenerator keyGenerator = new KeyGenerator();

        //1，客户端发送请求
        //明文
        String srcStr = "发送内容";
        // 将明文数据进行sha256,获得明文摘要
        String signStrSha256Hex = DigestUtils.sha256Hex(decode(srcStr));
        System.out.println("客户端摘要:"+signStrSha256Hex);
        //对摘要加签，获取数字信封
        String signStr = Base64Utils.encodeToString(RsaSignature.sign(keyGenerator.getPrivateKey(), signStrSha256Hex.getBytes()));
        System.out.println("签名："+signStr);
        //将明文、数字信封发送给服务端


        //2，服务端验签
        // 将明文数据进行sha256,获得明文摘要
        String signStrSha256Hex1 = DigestUtils.sha256Hex(decode(srcStr));
        System.out.println("服务端摘要:"+signStrSha256Hex1);
        //验签
        boolean result = RsaSignature.verify(keyGenerator.getPublicKey(), signStrSha256Hex1.getBytes(), Base64Utils.decodeFromString(signStr));
        System.out.println("验签结果："+result);
    }

    private static String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

}
