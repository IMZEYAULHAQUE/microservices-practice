package com.pnc.assignment.aggregator.model.user;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pnc.assignment.aggregator.model.account.Account;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

   private static final long serialVersionUID = -7276753885312066914L;

   public User() {
   }

   public User(@Size(max = 25) String firstName, @Size(max = 25) @NotEmpty String lastName, Address address) {
      super();
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
   }

   @JsonProperty(value = "id") // since this property is named as "id" in user-service module, name has to be changed here to json mapping. 
   private Long userId;

   @Size(max = 25)
   private String firstName;

   @Size(max = 25)
   @NotEmpty
   private String lastName;

   private Address address;

   private List<Account> accounts;

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long id) {
      this.userId = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public Address getAddress() {
      return address;
   }

   public void setAddress(Address address) {
      this.address = address;
   }

   public List<Account> getAccounts() {
      return accounts;
   }

   public void setAccounts(List<Account> accounts) {
      this.accounts = accounts;
   }

   @Override
   public String toString() {
      return "User [id=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", accounts=" + accounts + "]";
   }
}
