package com.micro.user.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.micro.user.service.entities.User;
import com.micro.user.service.entities.User.UserBuilder;
import com.micro.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
@RestController
@RequestMapping("/users")
public class UserServiceController {
	
	@Autowired
	private UserService userServiceImpl;
	
	Logger logger=LoggerFactory.getLogger(UserServiceController.class);
			
	//create
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user)
	{
		User saveUser = userServiceImpl.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
	}
	
	int retryCount=1;
	//single user
	@GetMapping("/{userId}")
	//@CircuitBreaker(name="ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
	//@Retry(name="ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
	@RateLimiter(name="ratingHotelRateLimiter",fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		
		System.out.println("singleuser "+userId);
		logger.info("Get Single User Handler: UserController");
		logger.info("RetryCount is : {}"+retryCount);
		retryCount++;
		User user = userServiceImpl.getUser(userId);
		return ResponseEntity.ok(user);
	}
	
	//creating fallback method for circuitbreaker
	
	public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
		logger.info("Fallback is executed because Service is down : ",ex.getMessage());
		User user = User.builder()
		.email("DummyUser@gmail.com")
		.name("DummyUser")
		.about("This User is created dummy becausesome services are down").
		userId("12345").build();
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	//all user
	@GetMapping
	public ResponseEntity<List<User>> getAllUser(){
		
		System.out.println("Inside Get all users");
		List<User> allUser = userServiceImpl.getAllUser();
		System.out.println("Records "+allUser);
		return ResponseEntity.ok(allUser);
	}
	
	
}
