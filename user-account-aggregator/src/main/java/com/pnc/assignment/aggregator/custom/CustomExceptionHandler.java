package com.pnc.assignment.aggregator.custom;

import com.pnc.assignment.aggregator.exception.InvalidAmountWithdrawException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pnc.assignment.aggregator.exception.AccountNotFoundException;
import com.pnc.assignment.aggregator.exception.UserNotFoundException;
import com.pnc.assignment.aggregator.model.ExceptionData;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

   @ExceptionHandler({ AccountNotFoundException.class })
   @Nullable
   public final ResponseEntity<Object> handleAccountNotFoundException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
   }
   
   @ExceptionHandler({ UserNotFoundException.class })
   @Nullable
   public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
   }

   @ExceptionHandler({ InvalidAmountWithdrawException.class })
   @Nullable
   public final ResponseEntity<Object> handleInvalidAmountWithdrawException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
   }
   
   @ExceptionHandler({ NotWritablePropertyException.class })
   @Nullable
   public final ResponseEntity<Object> handleNotWritablePropertyException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
   }
   
   @ExceptionHandler({ Exception.class })
   @Nullable
   public final ResponseEntity<Object> handleUnhandledException(Exception ex, WebRequest request) {
      return handleExceptionInternal(ex, this.getGenericBody(ex, HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
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
