package com.qa.ims.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// Establishes default getters and setters for private properties
@Data
// Establishes builder functionality for lombok (eg: .builder().id(x).build();)
@Builder				
// Establishes all arg constructor
@AllArgsConstructor
public class Item {
	
	private Long id;
	
	private String name;
	
	private Float price;
	
	public Item(String name, Float price) {
		this.name = name;
		this.price = price;
	}
	
	/**
	 * 
	 * Should return string like:
	 * 
	 *	Item [
	 *		ID:		1
	 *		Name:	Bodyboard
	 *		Price:	£19.99
	 *	]
	 * 
	 */
	@Override
	public String toString() {
		return String.format(
				"Item [\n\tID:\t\t%l\n\tName:\t%s\n\tPrice:\t£%f\n]\n",
				this.id, this.name, this.price);
	}
	
	/**
	 *
	 * Compares incoming obj class with current class in order to establish
	 * equality.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Item)) {
			return false;
		} else {
			Item i2 = (Item) obj;
			return i2.toString().equals(this.toString());
		}
	}

}