package com.website.framework.config;

import com.website.framework.common.ApplicationProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义身份验证类（用于重写WebSecurityConfigurerAdapter默认配置）
 * @Configuration     表示这是一个配置类
 * @EnableWebSecurity    允许security
 * configure()     该方法重写了父类的方法，用于添加用户与角色
 * */
@Configuration
@EnableWebSecurity(debug = true)
@Order(3)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService securityUserDetailService;
    @Autowired
    private AuthenticationProvider securityProvider;

    @Override
    protected UserDetailsService userDetailsService() {
        //自定义用户信息类
        return this.securityUserDetailService;
    }

    /**
     * 重写该方法，添加自定义用户
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义AuthenticationProvider
        auth.authenticationProvider(securityProvider);
        //inMemoryAuthentication 从内存中获取
        // auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        //         .withUser("user1").password(new BCryptPasswordEncoder().encode("123456")).roles("ADMIN", "USER")
        //         .and()
        //         .withUser("user2").password(new BCryptPasswordEncoder().encode("123456")).roles("USER")
        //         .and()
        //         .withUser("user3").password(new BCryptPasswordEncoder().encode("123456")).roles("USER");
    }

    /**
     * 重写该方法，设定用户访问权限
     * 用户身份可以访问 订单相关API
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApplicationProperty property = new ApplicationProperty().readFromClassPath("com/framework/security/security.properties");
        String value = property.getValue("permitall-matchers-url-0");
        http.authorizeRequests()
//                .antMatchers("/orders/**").hasAnyRole("USER", "ADMIN")    //用户权限
//                .antMatchers("/user/**").hasRole("ADMIN")    //管理员权限
                .antMatchers("/errorlogin/**").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/css/**", "/js/**", "/image/**").permitAll()
                .anyRequest().authenticated() //除上面外的所有请求 其余全部需要鉴权认证,登录后可以访问
                .and()
                .formLogin()
                .loginProcessingUrl("/security_check")
                .loginPage("/login")    //跳转登录页面的控制器，该地址要保证和表单提交的地址一致！
                //成功处理
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
                            throws IOException, ServletException {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal != null && principal instanceof UserDetails) {
                            UserDetails user = (UserDetails) principal;
                            System.out.println("loginUser:"+user.getUsername());
                            //维护在session中
                            request.getSession().setAttribute("userinfo", user);

                            response.sendRedirect("index");
                        }
                    }
                })
                //失败处理
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
                            throws IOException, ServletException {
                        System.out.println("error"+authenticationException.getMessage());
                        response.sendRedirect("errorlogin");
                    }
                })
                .permitAll()
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(10)
//                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .and()
                .logout()
                .logoutUrl("/exitlogin")   //自定义退出登录页面
//                .logoutSuccessHandler(new CoreqiLogoutSuccessHandler()) //退出成功后要做的操作（如记录日志），和logoutSuccessUrl互斥
                .logoutSuccessUrl("/login") //退出成功后跳转的页面
//                .deleteCookies("JSESSIONID")    //退出时要删除的Cookies的名字
                .permitAll()
                .and()
                .csrf().disable();        //暂时禁用CSRF，否则无法提交表单

        //super.configure(http);
    }
}
