package com.qa.ims.persistence.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	
	private Long id;
	
	private Customer customer;
	
	@Builder.Default
	private Map<Item, Integer> items = new HashMap<>();
	
	public void addItem(Item item, Integer quantity) {
		this.items.put(item, quantity);
	}
	
	public void removeItem(Item item) {
		this.items.remove(item);
	}
	
	public Double getOrderTotal() {
		Double total = 0d;
		
		for (Entry<Item, Integer> entry : this.items.entrySet()) {
			total += entry.getKey().getPrice() * entry.getValue();
		}
		
		return total;
	}
	
	public Entry<Item, Integer> getItemEntryById(Long id) {
		for (Entry<Item, Integer> entry : this.items.entrySet()) {
			if (entry.getKey().getId() == id) {
				return entry;
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Order [\n\t"
				+ "ID:\t%d\n\t"
				+ "Customer ID:\t%d\n\t"
				+ "Customer Name:\t%s %s\n\t"
				+ "Total Price:\t£%.2f\n\t"
				+ "Number of items: %d\n]",
				this.id,
				this.customer.getId(),
				this.customer.getFirstName(),
				this.customer.getSurname(),
				this.getOrderTotal(),
				items.values().stream().mapToInt(Integer::intValue).sum());
	}

}
