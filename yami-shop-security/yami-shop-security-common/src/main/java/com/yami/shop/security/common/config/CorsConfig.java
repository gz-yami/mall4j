package com.yami.shop.security.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author yami
 */
@Configuration
public class CorsConfig {

    /**
     *  修改为添加而不是设置，* 最好生产环境改为实际的需要， 这里可以用多个add配置多个域名
     *  configuration.addAllowedOrigin("http://localhost:8080");
     *  configuration.addAllowedOrigin("http://192.168.1.6:8080");
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        //修改为添加而不是设置
        configuration.addAllowedMethod("*");
        //这里很重要，起码需要允许 Access-Control-Allow-Origin
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600 * 24L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
