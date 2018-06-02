package com.pnc.assignment.account.config;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.Formatter;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware")
public class CustomConfig {

   /*@Bean
   public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
      return new Jackson2ObjectMapperBuilderCustomizer() {

         @Override
         public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
            jacksonObjectMapperBuilder.timeZone("UTC");
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").toFormatter()));
            jacksonObjectMapperBuilder.serializers(new ZonedDateTimeSerializer(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").toFormatter()));
         }

      };
   }*/
   
   @Bean
   public Formatter<LocalDateTime> localDateFormatter() {
      
     return new Formatter<LocalDateTime>() {
       @Override
       public LocalDateTime parse(String text, Locale locale) throws ParseException {
         return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE);
       }
    
       @Override
       public String print(LocalDateTime object, Locale locale) {
         return DateTimeFormatter.ISO_DATE.format(object);
       }
     };
   }

   @Bean
   public AuditorAware<String> jpaAuditorAware() {
      return () -> java.util.Optional.of("Haque");
   }
}
