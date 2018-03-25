package com.sarva.restaurantservice.restaurantnew;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface RestaurantNewRepository extends ReactiveCrudRepository<RestaurantNew, String>{

	Flux<RestaurantNew> findByName(String name);

}
