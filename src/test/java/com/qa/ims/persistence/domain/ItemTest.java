package com.qa.ims.persistence.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ItemTest {

	@Test
	public void testEquals() {
		EqualsVerifier.simple().forClass(Item.class).verify();
	}
	
	@Test
	public void testToString() {
		
		Item item = Item.builder()
				.id(1L)
				.name("Bodyboard")
				.price(19.99)
				.build();
		
		String itemStr = "Item [\n\tID:\t1\n\tName:\tBodyboard\n\tPrice:\t£19.99\n]";
		
		assertEquals(itemStr, item.toString());
	}
	
}
