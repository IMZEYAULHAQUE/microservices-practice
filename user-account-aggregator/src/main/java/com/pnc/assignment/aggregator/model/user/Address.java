package com.pnc.assignment.aggregator.model.user;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {

   private static final long serialVersionUID = 8175621106786227660L;

   public Address() {
   }

   public Address(@Size(max = 100) @NotEmpty String addressOne, @Size(max = 100) String addressTwo, @Size(max = 30) @NotEmpty String city, @Size(max = 100) @NotEmpty String state, @Size(max = 10) @NotEmpty String zip, User user) {
      super();
      this.addressOne = addressOne;
      this.addressTwo = addressTwo;
      this.city = city;
      this.state = state;
      this.zip = zip;
      this.user = user;
   }

   private Long id;

   @Size(max = 100)
   @NotEmpty
   private String addressOne;

   @Size(max = 100)
   private String addressTwo;

   @Size(max = 30)
   @NotEmpty
   private String city;

   @Size(max = 100)
   @NotEmpty
   private String state;

   @Size(max = 10)
   @NotEmpty
   private String zip;

   @JsonIgnore
   private User user;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public String getAddressOne() {
      return addressOne;
   }

   public void setAddressOne(String addressOne) {
      this.addressOne = addressOne;
   }

   public String getAddressTwo() {
      return addressTwo;
   }

   public void setAddressTwo(String addressTwo) {
      this.addressTwo = addressTwo;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getZip() {
      return zip;
   }

   public void setZip(String zip) {
      this.zip = zip;
   }

   @Override
   public String toString() {
      return "Address [id=" + id + ", addressOne=" + addressOne + ", addressTwo=" + addressTwo + ", city=" + city + ", state=" + state + ", zip=" + zip + "]";
   }
}
