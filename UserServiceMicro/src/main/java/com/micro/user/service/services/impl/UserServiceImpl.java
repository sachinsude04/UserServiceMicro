package com.micro.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.micro.user.service.entities.Hotel;
import com.micro.user.service.entities.Rating;
import com.micro.user.service.entities.User;
import com.micro.user.service.exceptions.ResourceNotFoundException;
import com.micro.user.service.external.services.HotelService;
import com.micro.user.service.repositories.UserRepository;
import com.micro.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;
	
	private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		System.out.println("in service "+userRepository.findAll());
		return userRepository.findAll();
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		//get user from database from user repository
		User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id is not found on server !!"+userId));
		//fetch rating from user from RATING SERVICE

		// http://localhost:8092/ratings/users/8da874ae-cfcd-4be1-87cd-e29cddb30acb
		//+user.getUserId()
		
		//non dyanamic
		//Rating[] forObject = restTemplate.getForObject("http://localhost:8092/ratings/users/8da874ae-cfcd-4be1-87cd-e29cddb30acb",Rating[].class);
		
		Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId() ,Rating[].class);
		System.out.println("list of rating objects");
		logger.info("{} ",ratingsOfUser);
		List<Rating> ratingOfUserList= Arrays.stream(ratingsOfUser).collect(Collectors.toList());
		List<Rating> ratings=ratingOfUserList.stream().map(rating->{
			
			//api call to hotel service to get the hotel
			//http://localhost:8091/hotels/0cec853d-02bf-47c6-90e5-a18025311a19
			
		//	System.out.println("hotel ratings ++++++++++"+rating.getHotelId());
			//Below two lines for rest template
			 //ResponseEntity<Hotel> responseHotel = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
			 //Hotel hotel=responseHotel.getBody();
			
			//by using feign client
			Hotel hotel = hotelService.getHotel(rating.getHotelId());
			
			// logger.info("Response status code: {}",responseHotel);
			//to set the hotel to rating
			rating.setHotel(hotel);
			//return the rating
			return rating;
			
		}).collect(Collectors.toList());
			 
		//
		
		user.setRatings(ratings);
	
		return user;
	}

}
