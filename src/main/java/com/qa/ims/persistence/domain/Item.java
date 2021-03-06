package com.qa.ims.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Establishes default getters and setters for private properties
@Data
// Establishes builder functionality for lombok (eg: .builder().id(x).build();)
@Builder				
// Establishes all arg constructor
@AllArgsConstructor
// Establishes no arg constructor
@NoArgsConstructor
public class Item {
	
	private Long id;
	
	private String name;
	
	private Double price;
	
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
				"Item [\n\tID:\t%d\n\tName:\t%s\n\tPrice:\t£%.2f\n]",
				this.id, this.name, this.price);
	}

}
