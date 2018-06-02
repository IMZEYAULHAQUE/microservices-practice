package com.pnc.assignment.aggregator.feignclient.user;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pnc.assignment.aggregator.custom.CustomFeignConfiguration;
import com.pnc.assignment.aggregator.custom.CustomResponseEntity;
import com.pnc.assignment.aggregator.model.user.User;

//if the 'user-service' micro service is registered to the same discovery service as the current micro service, there is no need for an URL as it will be retrieved for you based on the name.
@FeignClient(name = "${feign.client.user.url}", configuration = CustomFeignConfiguration.class)
public interface UserFeignClient {
   
   @GetMapping("/users")
   public CustomResponseEntity<List<User>> findAll();
   
   @GetMapping(value="/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<User> findUser(@PathVariable("userId") @NotNull Long userId);
   
   @GetMapping(value="/users/lastname/{lastName}")
   public CustomResponseEntity<List<User>> findUserByLastName(@PathVariable("lastName") @NotBlank String lastName);
   
   @PutMapping(value="/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String> update(@RequestBody @Valid User user, @PathVariable("userId") @NotBlank Long userId);
   
   @PatchMapping(value="/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String> partialUpdate(@RequestBody Map<String, String> updateRequestedMap, @PathVariable("userId") Long userId);
   
   @DeleteMapping(value="/users/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
   public CustomResponseEntity<String> delete(@PathVariable("userId") Long userId);

}
