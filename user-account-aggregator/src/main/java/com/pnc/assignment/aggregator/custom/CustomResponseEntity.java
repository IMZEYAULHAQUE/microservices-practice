package com.pnc.assignment.aggregator.custom;

public class CustomResponseEntity<T> {

   private CustomResponseStatus status;
   private T data;

   public CustomResponseEntity() {
      this.status = CustomResponseStatus.SUCCESS;
   }

   public CustomResponseEntity(CustomResponseStatus status, T data) {
      super();
      this.status = status;
      this.data = data;
   }

   public static final <S> CustomResponseEntity<S> SUCCESS() {
      return new CustomResponseEntity<S>(CustomResponseStatus.SUCCESS, null);
   }

   public static final <S> CustomResponseEntity<S> SUCCESS(S data) {
      return new CustomResponseEntity<S>(CustomResponseStatus.SUCCESS, data);
   }

   public static final <S> CustomResponseEntity<S> FAILURE() {
      return new CustomResponseEntity<S>(CustomResponseStatus.FAILURE, null);
   }

   public static final <S> CustomResponseEntity<S> FAILURE(S data) {
      return new CustomResponseEntity<S>(CustomResponseStatus.FAILURE, data);
   }

   public CustomResponseStatus getStatus() {
      return status;
   }

   public CustomResponseEntity<T> setStatus(CustomResponseStatus status) {
      this.status = status;
      return this;
   }

   public T getData() {
      return data;
   }

   public CustomResponseEntity<T> setData(T data) {
      this.data = data;
      return this;
   }

   public enum CustomResponseStatus {
      SUCCESS, PARTIAL_PASS, FAILURE;
   }
}
