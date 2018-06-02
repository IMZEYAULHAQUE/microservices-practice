package com.pnc.assignment.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="user_address")
public class Address implements Serializable {

	private static final long serialVersionUID = -7871650211955719481L;

	public Address() {}
	
	public Address(@Size(max = 100) @NotEmpty String addressOne, @Size(max = 100) String addressTwo,
			@Size(max = 30) @NotEmpty String city, @Size(max = 100) @NotEmpty String state,
			@Size(max = 10) @NotEmpty String zip, User user) {
		super();
		this.addressOne = addressOne;
		this.addressTwo = addressTwo;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "address_one")
	@Size(max=100)
	@NotEmpty
	private String addressOne;
	
	@Column(name = "address_two")
	@Size(max=100)
	private String addressTwo;
	
	@Size(max=30)
	@NotEmpty
	private String city;
	
	@Size(max=100)
	@NotEmpty
	private String state;
	
	@Size(max=10)
	@NotEmpty
	private String zip;
	
	@OneToOne(orphanRemoval = true)
	@JoinColumn(name = "user_id", nullable = false, unique=true)
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
