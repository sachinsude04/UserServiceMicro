package com.micro.user.service.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.micro.user.service.entities.User;


public interface UserService {
	
	// user operations 
	
	//crete 
	User saveUser(User user);

	//get all user
	public List<User> getAllUser();
	
	//get single user of given userId

	public User getUser(String userId);
	
	//TODO: delete
	//TODO: update
}
