package com.resttemplate.service.impl;

import com.resttemplate.service.RestTemplateService;
import com.retry.annotation.BackoffAnnotation;
import com.retry.annotation.RetryAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName RestTemplateServiceImpl
 * @Description: restTemperate请求封装类
 * @Author dong
 * @Date 2019/11/19
 * @Version V1.0
 **/
@Service(value = "restTemplateService")
public class RestTemplateServiceImpl implements RestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    @RetryAnnotation(maxAttempt = 3, backoff = @BackoffAnnotation(delay = 500L, multiplier = 4, maxDelay = 20000L))
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    @Override
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     *
     * @param url 请求URL
     * @param request 请求参数
     * @param headersMap 请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @param <T> 响应对象封装类
     * @return
     */
    @Override
    public <T> ResponseEntity<T> post(String url, Object request, Map<String, String> headersMap, Class<T> responseType, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headersMap);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(request,headers);
        return restTemplate.postForEntity(url, httpEntity, responseType, uriVariables);
    }

    /**
     *
     * @param url 请求URL
     * @param request 请求参数
     *                (如post请求参数中包含中文,将String类型改成Object,将Object强转为String编码格式不是UTF-8)
     * @param headers 请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @param <T> 响应对象封装类
     * @return
     */
    @Override
    public <T> ResponseEntity<T> post(String url, Object request, HttpHeaders headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(url, httpEntity, responseType, uriVariables);
    }

    /**
     *
     * @param url
     * @param httpEntity
     * @param responseType
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> post(String url, HttpEntity httpEntity, Class<T> responseType) {
        return restTemplate.postForEntity(url,httpEntity,responseType);
    }

    /**
     *
     * @param url 请求URL
     * @param paramEntity URL中的变量，与Map中的key对应
     * @return
     */
    @Override
    public ResponseEntity<Map> exchange(String url, Object paramEntity) {
        HttpEntity<?> requestParamEntity = new HttpEntity<>(paramEntity);
        return restTemplate.exchange(url, HttpMethod.GET, requestParamEntity, Map.class);
    }

    /**
     *
     * @param url
     * @param param
     * @param typeRef
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpEntity param, ParameterizedTypeReference typeRef) {
        return restTemplate.exchange(url, HttpMethod.GET, param, typeRef);
    }
}
