package com.springoauth.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ServletComponentScan
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.druid")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}