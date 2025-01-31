package com.common.signature;

import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaSignature {
    static String alg = "SHA256WithRSA";

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

    private String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
