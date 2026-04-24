package com.common.signature;

import com.common.exception.GlobalException;
import java.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class KeyGenerator {
    private String publicKey;
    private String privateKey;

    public KeyGenerator() {
        try{
            //初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            this.publicKey = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
            this.privateKey = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        }catch(Exception ex){
            throw new GlobalException("初始化密钥失败！", ex);
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

}
