package com.resttemplate.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @ClassName RestTemplateService
 * @Description: RestTemplate接口
 * @Author dong
 * @Date 2019/11/19
 * @Version V1.0
 **/
public interface RestTemplateService {

    /**
     * GET请求调用方式
     *
     * @param url 请求URL
     * @param responseType 返回对象类型
     * @return ResponseEntity 响应对象封装类
     */
     <T> ResponseEntity<T> get(String url, Class<T> responseType);

    /**
     * GET请求调用方式
     *
     * @param url 请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables);

    /**
     * GET请求调用方式
     *
     * @param url 请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> uriVariables);

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url 请求URL
     * @param headers 请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables);

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url 请求URL
     * @param headers 请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables);

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url 请求URL
     * @param headers 请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables);

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url 请求URL
     * @param headers 请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables);

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
    <T> ResponseEntity<T> post(String url, Object request, Map<String, String> headersMap, Class<T> responseType, Object... uriVariables);

    <T> ResponseEntity<T> post(String url, Object request, HttpHeaders headers, Class<T> responseType, Object... uriVariables);

    /**
     * @param url
     * @param httpEntity
     * @param responseType
     * @param <T>
     * @return
     */
    <T> ResponseEntity<T> post(String url, HttpEntity httpEntity, Class<T> responseType);

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url 请求URL
     * @param paramEntity URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    ResponseEntity<Map> exchange(String url, Object paramEntity);


    /**
     * GET请求调用方式
     * @param url
     * @param param
     * @param typeRef
     * @param <T>
     * @return
     */
    <T> ResponseEntity<T> exchange(String url, HttpEntity param, ParameterizedTypeReference typeRef);

}
