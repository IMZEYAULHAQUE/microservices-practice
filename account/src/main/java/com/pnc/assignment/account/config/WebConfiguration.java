package com.pnc.assignment.account.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class WebConfiguration  implements WebMvcConfigurer  {

   /**
    * This setting required to convert String to CustomResponseEntity, else will get ClassCastException. 
    */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
       MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
       
       SimpleModule module = new SimpleModule();
       module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").toFormatter()));
       //module.addSerializer(LocalDateTime.class, new ZonedDateTimeSerializer(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").toFormatter()));
       module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").toFormatter()));
       jsonConverter.getObjectMapper().registerModule(module);
       
       converters.add(0, jsonConverter);
    }
}
