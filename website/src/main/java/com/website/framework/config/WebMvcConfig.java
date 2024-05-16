package com.website.framework.config;

import com.website.framework.interceptor.GlobalHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    /*
     *url拦截
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalHandlerInterceptor()).addPathPatterns("/**");
    }

    /*
     *跨域访问配置
     * 出于安全考虑，浏览器禁止ajax跨域访问,W3C的CORS规范(cross-origin resource sharing)允许实现跨域访问，目前已被大多数浏览器支持
     * 跨域原理：发起跨域请求，浏览器对请求域返回的响应信息检查HTTP头，如果ACCESS-CONTROL-ALLOW-ORIGIN包含了自身域，则允许访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //允许跨域访问
        registry.addMapping("/**");
        //允许来自domain2.com的跨域访问，并且限定访问路径为/api，方法是POST或者GET
        registry.addMapping("/api/**").allowedOrigins("http://domain2.com").allowedMethods("POST", "GET");
    }

    /*
     *http请求映射到Controller方法参数上后，部分类型参数转化（日期字符串转为日期类型）
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    /*
     * 视图映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //index1.html请求，设置返回的视图为index2.html
        registry.addViewController("/index1").setViewName("/index2");
        //以.do结尾的请求重定向到/index请求
        registry.addRedirectViewController("/**/*.do", "/index1");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }
}
