package org.wmy.mall.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wmy.mall.interceptor.UserLoginInterceptor;

/**
 * @author wmy
 * @create 2020-11-07 20:46
 */

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * /error是因为在处理/carts时，如果
     *
     * @NotNull private Integer productId; 为空，会报错的，此时spring-boot会重定向到/error
     * 所以这里不拦截/error，为的就是报错
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error","/user/register", "/user/login", "/categories", "/products", "/products/*");
    }
}
