package com.openfeign;

import com.retry.annotation.BackoffAnnotation;
import com.retry.annotation.RetryAnnotation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @ClassName RestTemplateService
 * @Description: RestTemplate接口
 * @Author dong
 * @Date 2019/11/19
 * @Version V1.0
 **/
@FeignClient(name = "feignCustomService", url = "${openfeign.url}")
public interface OpenFeignService {

    @GetMapping(value = "huaweicloud/test")
    @RetryAnnotation(maxAttempt = 6, backoff = @BackoffAnnotation(delay = 500L, multiplier = 4, maxDelay = 20000L))
    public String test();
}
