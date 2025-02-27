package com.springoauth.oauth;

import com.springoauth.security.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author pcc
 * @version 1.0.0
 * @description oauth2的配置类
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    /**
     * 用户认证 Manager
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    SecurityUserDetailsService securityUserDetailsService;

    /**
     *
     * @param endpoints
     * @throws Exception
     * 注： 认证端点的配置，注入springsecurity的认证管理器，
     *      并指定自定义的用户信息服务接口
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 自定义 加载用户信息的接口
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                //jwt模式
                .tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(securityUserDetailsService);
    }

    /**
     *
     * @param oauthServer
     * @throws Exception
     * 注： 安全相关的配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                // 允许表单认证
                .allowFormAuthenticationForClients()
                // 无论客户端是否登录，都允许访问令牌验证端点 /oauth/check_token
                .checkTokenAccess("permitAll()");
        // 只有已经认证通过的用户才能访问 令牌验证端点：/oauth/check_token
        // .checkTokenAccess("isAuthenticated()");
    }

    /**
     *
     * @param clients
     * @throws Exception
     * 注： 客户端相关的配置，这里使用的是内存客户端配置，正常应该从数据库读取
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用内存中的客户端信息
                .inMemory()
                //  客户端账号、密码。
                .withClient("ebbing-web").secret("$2a$10$JlsT1rU3UC8U/nPU4ZEiTev7sLs9Awu3S0b3TBDFghHtsJxUd6quG")
                // 授权模式,refresh_token 唯一的作用是启用刷新token
                .authorizedGrantTypes("refresh_token","authorization_code","implicit","password","","client_credentials")
                // 授权范围-不同的服务
                .scopes("customer", "space")
                // 访问令牌的有效期，单位秒
                .accessTokenValiditySeconds(3600)
                // 刷新令牌的有效期，单位秒
                .refreshTokenValiditySeconds(3600*24*7)
                // 这个地址，获取授权码后会使用这个地址进行传送授权码，获取令牌（授权码模式）也需要传递这个地址
                .redirectUris("https://www.baidu.com")
                // 这个配置和授权码模式相关：false会跳转授权页面
                .autoApprove(false);
    }
}
