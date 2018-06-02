package com.pnc.assignment.exception;

import org.springframework.beans.NotWritablePropertyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pnc.assignment.model.ExceptionData;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

   @ExceptionHandler({ UserNotFoundException.class })
   @Nullable
   public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
   }
   
   @ExceptionHandler({ NotWritablePropertyException.class })
   @Nullable
   public final ResponseEntity<Object> handleNotWritablePropertyException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
   }
   
   @ExceptionHandler({ Exception.class })
   @Nullable
   public final ResponseEntity<Object> handleUnhandledException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, this.getGenericBody(ex, HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
   }

   @Override
   protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
      
      if (body == null) {
         body = this.getGenericBody(ex, status);
      }
      return super.handleExceptionInternal(ex, body, headers, status, request);
   }
   
   protected ExceptionData getGenericBody(Exception ex, HttpStatus status) {
      ExceptionData data = new ExceptionData(status.value());
      data.addErrorData("error", status.getReasonPhrase());
      data.addErrorData("message", ex.getMessage());
      data.addErrorData("errorType", ex.getClass().getSimpleName());
      data.addErrorData("exceptionClass", ex.getClass().getName());
      return data;
   }
}
