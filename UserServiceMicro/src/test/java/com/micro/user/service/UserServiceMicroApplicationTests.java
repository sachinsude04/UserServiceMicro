package com.micro.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.micro.user.service.entities.Rating;
import com.micro.user.service.external.services.RatingService;

@SpringBootTest
class UserServiceMicroApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private RatingService ratingService;
	
	/*
	 * @Test public void createRatings() { Rating
	 * rating=Rating.builder().rating(10).userId("").hotelId("").
	 * feedback("This is created by using feign client").build();
	 * ratingService.createRating(rating);
	 * 
	 * System.out.println("New rating created"); }
	 */

}
