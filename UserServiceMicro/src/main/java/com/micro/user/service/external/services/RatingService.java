package com.micro.user.service.external.services;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.micro.user.service.entities.Rating;

@Service

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

	// get method
	
	
	// post method
	
	@PostMapping("/ratings")
	public Rating createRating( Rating rating);
	
	
	//put method
	@PutMapping("/ratings/{ratingId}")
	public Rating updateRatings(Rating rating,@PathVariable("ratingId") String ratingId);
	
	//delete
	@DeleteMapping("/ratings/{ratingId}")
	public void deleteRating(@PathVariable("ratingId") String ratingId);
}
