package org.cloris.houses.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterBeanConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new LogFilter());

        List<String> urlList = new ArrayList<String>();
        urlList.add("*");

        bean.setUrlPatterns(urlList);
        return bean;



    }
}
