package com.resttemplate.config;

import com.resttemplate.controller.TestController;
import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RestTemplateConfig
 * @Description: spring的RestTemplate连接池相关配置
 * @Author dong
 * @Date 2019/11/27
 * @Version V1.0
 * 时序:
 * <p>
 * 详细设计:
 **/
//@RefreshScope
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     * #设置整个连接池最大连接数 根据自己的场景决定
     */
    @Value("${restTemplate.maxTotal:500}")
    private int maxTotal;
    /**
     * 路由是对maxTotal的细分
     */
    @Value("${restTemplate.maxPerRoute:100}")
    private int maxPerRoute;
    /**
     * 服务器返回数据(response)的时间，超过该时间抛出read timeout
     */
    @Value("${restTemplate.readTimeout:10000}")
    private int readTimeout;
    /**
     * 连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
     */
    @Value("${restTemplate.connectTimeout:10000}")
    private int connectTimeout;
    /**
     * 从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException
     */
    @Value("${restTemplate.timeToLive:30}")
    private Integer timeToLive;
    @Value("${platform.connectionPool.retryCount:2}")
    private Integer retryCount;
    @Bean
    public RestTemplate restTemplate(@Autowired ResponseErrorHandler responseErrorHandler) {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(timeToLive,
                TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(maxTotal);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(maxPerRoute);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(retryCount, false));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        httpClientBuilder.setDefaultHeaders(headers);
        // 安全认证
//        httpClientBuilder.setDefaultCredentialsProvider(credsProvider());
//        httpClientBuilder.setSSLSocketFactory(sslSocketFactory());
        HttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        if (responseErrorHandler != null) {
            restTemplate.setErrorHandler(responseErrorHandler);
        }
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
                break;
            }
        }
        return restTemplate;
    }

    @Bean
    public ResponseErrorHandler genErrorHandler() {
        return new DefaultResponseErrorHandler();
    }

    private CredentialsProvider credsProvider(){
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials("", ""));
        return credsProvider;
    }
    private SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("sslSocketFactory error",e);
        }
        return null;
    }
    private X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s){
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s){
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}