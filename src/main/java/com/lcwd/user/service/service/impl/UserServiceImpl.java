package com.lcwd.user.service.service.impl;


import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    //to save data into data base we need repository
    @Autowired
    private UserRepository userRepository;

   @Autowired
   private HotelService hotelService;



    @Autowired
    private RestTemplate restTemplate;
    private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);




    @Override
    public User saveUser(User user) {
        //generate unique userid which is in string formate it will auto generate
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        //implimenting rating service call: using rest templet

        return userRepository.findAll();
    }

    //get single user
    @Override
    public User getUser(String userId) {
        //get user from database with the help repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !! : " + userId));
        //fetch rating of the above user from rating service
        //http://localhost:8083/ratings/users/14c889a6-dd50-4c8c-af47-41cf854e9d08

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(),Rating[] .class);
        logger.info("{} ",ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

        List<Rating> ratingsList = ratings.stream().map(rating -> {


            //api call to the hotel service get hotel
            //http://localhost:8082/hotels/f2178f11-b185-457b-ad10-c6e5244a90d1
//            ResponseEntity<Hotel> responseEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());

//            logger.info(" response of code {} ",responseEntity.getStatusCode());

            //set the hotel to rating
            rating.setHotel(hotel);

            //return to rating
            return rating;

        }).collect(Collectors.toList());

        user.setRatings(ratingsList);

        return user;
    }

}
