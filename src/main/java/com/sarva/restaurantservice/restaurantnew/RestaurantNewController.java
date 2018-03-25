package com.sarva.restaurantservice.restaurantnew;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class RestaurantNewController {

	private RestaurantNewRepository restaurantNewRepository;
	
	private WebClient webClient;

	public RestaurantNewController(RestaurantNewRepository restaurantRepository, WebClient.Builder webClientBuilder) {
		this.restaurantNewRepository = restaurantRepository;
		this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
	}

	@PostMapping("/restaurants-new")
	public Mono<RestaurantNew> restaurants(@RequestBody RestaurantNew restaurantNew) {
		return restaurantNewRepository.save(restaurantNew);
	}

	@PostMapping("/restaurants-new/insert/{count}")
	public String restaurantsRandomPost(@PathVariable int count, @RequestBody RestaurantNew restaurantNew) {
		List<RestaurantNew> restaurants = new ArrayList<RestaurantNew>();
		IntStream.rangeClosed(1, count).forEach(i -> restaurants
				.add(new RestaurantNew(restaurantNew.getName() + "-" + i, new Random().nextDouble(), "vegan")));

		long startTime = System.currentTimeMillis();
		restaurantNewRepository.saveAll(restaurants).subscribe();
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime));

		return "done and took -> " + (endTime - startTime);
	}

	@GetMapping("/restaurants-new")
	public Flux<RestaurantNew> allRestaurants() {
		return restaurantNewRepository.findAll();
	}

	@GetMapping("/restaurants-new/search")
	public Flux<RestaurantNew> restaurantsByCategoryAndPrice(@RequestParam String name) {
		return restaurantNewRepository.findByName(name);
	}
	
	@GetMapping("/restaurants-new/availability")
	public String getAvailableRestaurants() {
		long startTime = System.currentTimeMillis();
		Flux<RestaurantAvailability> availableRestaurants = this.restaurantNewRepository.findAll()
				.flatMap(this::getAvailability)
				// .filter(RestaurantAvailability::isAvailable)
				.take(500);
		availableRestaurants.subscribe();
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		return "done and took -> " + totalTime;

	}
	
	private Mono<RestaurantAvailability> getAvailability(RestaurantNew restaurantNew) {
		String uri = "/restaurants-availability/search/findByName?name=" + restaurantNew.getName();
		return webClient.get().uri(uri)
				.retrieve().bodyToMono(RestaurantAvailability.class);
	}

}
