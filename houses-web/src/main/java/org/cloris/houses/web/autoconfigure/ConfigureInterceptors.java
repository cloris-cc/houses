package org.cloris.houses.web.autoconfigure;

import org.cloris.houses.web.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Jackson Fang
 * Date:   2018/11/8
 * Time:   19:32
 */
@Configuration
public class ConfigureInterceptors implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/accounts/signIn")
                .excludePathPatterns("/accounts/register")
                .excludePathPatterns("/static")
                .excludePathPatterns("/index")
                .excludePathPatterns("/")
                .addPathPatterns("/**");
    }
}
