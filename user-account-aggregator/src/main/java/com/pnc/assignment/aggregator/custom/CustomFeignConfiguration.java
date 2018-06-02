package com.pnc.assignment.aggregator.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignConfiguration {

   @Bean
   public FeignExceptionDecoder feignExceptionDecoder() {
     return new FeignExceptionDecoder();
   }
}
