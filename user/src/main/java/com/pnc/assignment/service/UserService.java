package com.pnc.assignment.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pnc.assignment.exception.UserNotFoundException;
import com.pnc.assignment.model.Address;
import com.pnc.assignment.model.User;
import com.pnc.assignment.repository.UserRepository;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
	}

	@Override
	public List<User> findByLastName(String lastName) {
		return userRepository.findByLastName(lastName);
	}
	
	/**
	 * @ TODO Need to find optimized way to update in cascade way (without manual manipulation). May be by using Entity Manager in Repository class.
	 */
	@Override
	public User update(Long userId, User user) {
	   
	   User existingUser       = this.findUser(userId);
	   Address address         = user.getAddress();
      Address existingAddress = existingUser.getAddress();
      
	   existingUser.setFirstName(user.getFirstName());
	   existingUser.setLastName(user.getLastName());
	   
	   if (existingAddress != null && address != null) {
	      existingAddress.setAddressOne(address.getAddressOne());
	      existingAddress.setAddressTwo(address.getAddressTwo());
	      existingAddress.setCity(address.getCity());
	      existingAddress.setState(address.getState());
	      existingAddress.setZip(address.getZip());
	   }
	   else if (address != null) {
	      address.setId(null);
	      address.setUser(existingUser);
	      existingUser.setAddress(address);
	   }
      return userRepository.save(existingUser);
	}

	@Override
	public User partialUpdate(Long userId, Map<String, String> userFieldsUpdateMap) {
		User user = this.findUser(userId);
		
		userFieldsUpdateMap.forEach((k, v) -> {
			PropertyAccessor propertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(user);
			propertyAccessor.setPropertyValue(k, v);
		});
		user.setId(userId);  // required because map might have id as input with different value
		return userRepository.save(user);
	}

	@Override
	public void delete(Long userId) {
		
		if (this.isUserExist(userId)) {
			userRepository.deleteById(userId);
		}
		else {
			throw new UserNotFoundException(userId);
		}
	}

	@Override
	public boolean isUserExist(Long userId) {
      User user = new User();
      user.setId(userId);
      return userRepository.exists(Example.of(user));
   }
}
