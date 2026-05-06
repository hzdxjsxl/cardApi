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
                    // 🌟 核心防线：拦截所有路由，并校验登录状态
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());

                })).addPathPatterns("/**")
                // 极少数纯静态资源（非业务接口）可以在这里写死排除，比如前端界面的静态文件
                .excludePathPatterns("/api/mock/**")
                .excludePathPatterns(
                        "/webjars/**",
                        "/favicon.ico",        // 必须放行！
                        "/error",              // 必须放行！
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        // 🌟 2. OpenAPI 3 数据源（防死角核心！）
                        "/v3/api-docs",        // 注意这里没有后面的斜杠和星号！
                        "/v3/api-docs/**"     // 放行子路径
                )
                .excludePathPatterns("/favicon.ico");
    }
}
