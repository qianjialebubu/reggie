package com.bjpowernode.reggie.config;

import com.bjpowernode.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author qjl
 * @create 2022-09-21 20:03
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override

    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        消息转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =  new MappingJackson2HttpMessageConverter();
//        设置对象转换器将java转换到json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
//        消息转换器追加到mvc框架中,放到最前的位置
        converters.add(0,mappingJackson2HttpMessageConverter);

    }
}
