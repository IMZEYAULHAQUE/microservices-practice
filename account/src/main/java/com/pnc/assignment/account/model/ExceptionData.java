package com.pnc.assignment.account.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionData {

   private Integer errorCode;
   private Map<String, Object> errorData;
   private LocalDateTime date;
   
   public ExceptionData() {
      this(-1);
   }
   
   public ExceptionData(Integer errorCode) {
      this.errorCode = errorCode;
      this.errorData = new LinkedHashMap<>(7);
      this.date      = LocalDateTime.now();
   }
   
   public ExceptionData(Integer errorCode, Map<String, Object> errorData) {
      super();
      this.errorCode = errorCode;
      this.errorData = errorData;
      this.date      = LocalDateTime.now();
   }
   
   public LocalDateTime getDate() {
      return date;
   }

   public void setDate(LocalDateTime date) {
      this.date = date;
   }

   public Integer getErrorCode() {
      return errorCode;
   }

   public void setErrorCode(Integer errorCode) {
      this.errorCode = errorCode;
   }

   public Map<String, Object> getErrorData() {
      return errorData;
   }

   public void setErrorData(Map<String, Object> errorData) {
      this.errorData = errorData;
   }
   
   public void addErrorData(String key, Object data) {
      errorData.put(key, data);
   }
}
