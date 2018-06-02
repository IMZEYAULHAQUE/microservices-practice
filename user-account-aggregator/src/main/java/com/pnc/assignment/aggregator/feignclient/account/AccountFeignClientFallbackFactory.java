package com.pnc.assignment.aggregator.feignclient.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class AccountFeignClientFallbackFactory implements FallbackFactory<AccountFeignClient> {

   @Autowired
   private AccountFeignClientFallback fallback;
   
   @Override
   public AccountFeignClient create(Throwable cause) {
      return fallback.setCause(cause);
   }
}
