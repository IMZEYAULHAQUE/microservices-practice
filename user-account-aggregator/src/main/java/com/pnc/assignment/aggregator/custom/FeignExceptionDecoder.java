package com.pnc.assignment.aggregator.custom;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.pnc.assignment.aggregator.model.ExceptionData;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignExceptionDecoder implements ErrorDecoder {

   Logger logger = LoggerFactory.getLogger(FeignExceptionDecoder.class);
   private ErrorDecoder delegate = new ErrorDecoder.Default();
   
   /**
    * @ TODO Need to optimize to incorporate all possibility in better way. 
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   @Override
   public Exception decode(String methodKey, Response response) {
      
      try {
         byte[] responseBody = ByteStreams.toByteArray(response.body().asInputStream());
          ObjectMapper mapper = new ObjectMapper();
          CustomResponseEntity exception = mapper.readValue(new String(responseBody), CustomResponseEntity.class);
          
          if (exception.getData() instanceof ExceptionData || exception.getData() instanceof Map) {
             
             Map<String, Object> map = (exception.getData() instanceof Map) ? (Map<String, Object>)((Map<String, Object>)exception.getData()).get("errorData") : ((ExceptionData)exception.getData()).getErrorData();
             String exceptionClassName = (String)map.get("errorType");
             String errorMessage = (String)map.get("message");
             
             if (Exception.class.isAssignableFrom(Class.forName("com.pnc.assignment.aggregator.exception." + exceptionClassName))) {
                Class clazz = Class.forName("com.pnc.assignment.aggregator.exception." + exceptionClassName);
                Constructor con = clazz.getConstructor(String.class);
                throw (Exception)con.newInstance(errorMessage);
                //throw ((Exception)Class.forName("com.pnc.assignment.aggregator.exception." + exceptionClassName).newInstance());
             }
             else {
                throw new RuntimeException(exceptionClassName + " exception occured.");
             }
          }
          else if (exception.getData() instanceof String) {
             throw new RuntimeException((String)exception.getData());
          }
      } catch (RuntimeException ex) {
         throw ex;
      } catch (Exception ex) {
         ex.printStackTrace();
         logger.error("Exception occured while decoding error in FeignExceptionDecoder.decode(...). Returning default implementation.");
      }
      return delegate.decode(methodKey, response);
   }

}
