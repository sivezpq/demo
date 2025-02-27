package com.springoauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 这个注解必须加，开启Security
@EnableGlobalMethodSecurity(prePostEnabled = true) //保证post之前的注解可以使用
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    SecurityUserDetailsService securityUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoderBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //todo 允许表单登录
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .csrf()
                .disable();
        //
        // http.authorizeRequests()//开启登录配置.antMatchers("/login").permitAll()
        //     //.antMatchers("/login").permitAll()
        //     .antMatchers("/hello/haha").permitAll()
        //      .antMatchers("/oauth/authorize").permitAll()
        //      .antMatchers("/oauth/confirm_access").permitAll()
        //      .antMatchers("/oauth/error").permitAll()
        //     .antMatchers("/hello").hasRole("admin")//表示访问 /hello 这个接口，需要具备 admin 这个角色
        //     .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
        //     .anyRequest().authenticated()//表示剩余的其他接口，登录之后就能访问
        //     .and()
        //     // 禁用Spring Security自带的跨域处理
        //     .csrf().disable();
        //     // 定制我们自己的session策略(即JWT)：调整为让Spring Security不创建和使用session
        //     //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /**
         * 配置异常处理器
         * 登陆认证失败jwtAuthenticationEntryPoint
         * 鉴权失败accessDeniedHandlerImpl
         */
        // http.exceptionHandling()
        //     //配置认证失败处理器，请求参数中没有token凭证，则进入jwtAuthenticationEntryPoint处理
        //     .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        //     //配置权限不足处理器，如果访问接口请求时，请求参数中有token凭证，但权限不足，则进入accessDeniedHandlerImpl处理
        //     .accessDeniedHandler(accessDeniedHandlerImpl);

        /**
         * 跨域配置
         */
        http.cors();

        /**
         * 下面配置是Spring Security自动处理登录、登出请求
         * 如果要自定义/login登录逻辑请求接口，则需要把下面配置注释掉
         */
        // http.formLogin().permitAll();
            // //定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
            // .loginPage("/login")
            // //登录处理接口
            // .loginProcessingUrl("/doLogin")
            // //定义登录时，用户名的 key，默认为 username
            // .usernameParameter("uname")
            // //定义登录时，用户密码的 key，默认为 password
            // .passwordParameter("passwd")
            //登录成功的处理器
            // .successHandler(new AuthenticationSuccessHandler() {
            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
            //         resp.setContentType("application/json;charset=utf-8");
            //         PrintWriter out = resp.getWriter();
            //         out.write("success");
            //         out.flush();
            //         out.close();
            //     }
            // })
            // .failureHandler(new AuthenticationFailureHandler() {
            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
            //         resp.setContentType("application/json;charset=utf-8");
            //         PrintWriter out = resp.getWriter();
            //         out.write("fail");
            //         out.flush();
            //         out.close();
            //
            //         // // 设置响应内容类型为JSON，并且编码为UTF-8
            //         // resp.setContentType("application/json;charset=UTF-8");
            //         // ServletOutputStream outputStream = resp.getOutputStream();
            //         // // 获取认证异常的错误信息
            //         // String message=exception.getMessage();
            //         // // 如果是BadCredentialsException异常，则将错误信息设置为"用户名或者密码错误！"
            //         // if(exception instanceof BadCredentialsException){
            //         //     message="用户名或者密码错误！";
            //         // }
            //         // // 将错误信息以JSON格式写入响应输出流中
            //         // outputStream.write(JSONUtil.toJsonStr(R.error(message)).getBytes("UTF8"));
            //         // outputStream.flush();
            //         // outputStream.close();
            //     }
            // })
            // .permitAll()//和表单登录相关的接口统统都直接通过
            // .and()
            // .logout()
            // .logoutUrl("/logout")
            // .logoutSuccessHandler(new LogoutSuccessHandler() {
            //     @Override
            //     public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
            //         resp.setContentType("application/json;charset=utf-8");
            //         PrintWriter out = resp.getWriter();
            //         out.write("logout success");
            //         out.flush();
            //     }
            // })
            // .permitAll()
            // .and()
    }
}
