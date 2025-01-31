package com.common.signature.thirdapi.server;

import com.common.exception.GlobalException;
import com.common.util.DateUtils;
import com.common.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Validate {
    public boolean validate(HttpServletRequest request) {
        String timestamp = request.getHeader("timestamp");
        String nonceStr = request.getHeader("nonceStr");
        String signature = request.getHeader("signature");

        // 验证时间戳是否超时
        long NONCE_STR_TIMEOUT_SECONDS = 60L;
        if (StringUtils.isEmpty(timestamp) || DateUtils.between(new Date(timestamp), DateUtils.getNowDate(), TimeUnit.SECONDS) > NONCE_STR_TIMEOUT_SECONDS) {
            throw new GlobalException("invalid timestamp");
        }

        // // 验证nonceStr是否已使用
        // Boolean haveNonceStr = redisTemplate.hasKey(nonceStr);
        // if (StringUtils.isEmpty(nonceStr) || Objects.isNull(haveNonceStr) || haveNonceStr) {
        //     throw new GlobalException("invalid nonceStr");
        // }

        // // 验证签名
        // //用RsaSignature进行校验
        // if (StringUtils.isEmpty(signature) || !Objects.equals(signature, this.signature(timestamp, nonceStr, request))) {
        //     throw new GlobalException("invalid signature");
        // }

        // 将nonceStr存入Redis并设置过期时间
        // redisTemplate.opsForValue().set(nonceStr, nonceStr, NONCE_STR_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        return true;
    }
}
