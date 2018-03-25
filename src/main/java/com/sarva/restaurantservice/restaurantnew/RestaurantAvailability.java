package com.sarva.restaurantservice.restaurantnew;

import org.springframework.data.annotation.Id;

public class RestaurantAvailability {

	public RestaurantAvailability() {
		
	}
	
	public RestaurantAvailability(String name, boolean isAvailable) {
		this.name = name;
		this.isAvailable = isAvailable;
	}
	
	@Id
	private String id;
	
	private String name;
	
	private boolean isAvailable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
}
