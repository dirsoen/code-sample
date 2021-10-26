package com.qygly.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.Arrays;

/**
 *  */
@Configuration
public class FastJsonConverter {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 设置默认日期格式：
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // 设置支持的媒体类型，否则Spring Cloud的Feign Client调用报错：
        // feign.codec.EncodeException: Content-Type cannot contain wildcard type '*'
        fastConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 不使用SerializerFeature.PrettyFormat，可以将网络包降低为80%，时间也节省为80%：
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 4.将converter赋值给HttpMessageConverter
        HttpMessageConverter<?> converter = fastConverter;
        // 5.返回HttpMessageConverters对象
        return new HttpMessageConverters(converter);
    }
}