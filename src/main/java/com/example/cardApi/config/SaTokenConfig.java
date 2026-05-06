package com.example.cardApi.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器
        registry.addInterceptor(new SaInterceptor(handler -> {
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());

                })).addPathPatterns("/**")
                .excludePathPatterns("/api/mock/**")
                .excludePathPatterns(
                        "/webjars/**",
                        "/favicon.ico",
                        "/error",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**"
                )
                .excludePathPatterns("/favicon.ico");
    }
}
