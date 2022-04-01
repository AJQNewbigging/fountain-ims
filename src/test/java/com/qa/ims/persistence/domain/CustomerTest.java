package com.qa.ims.persistence.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class CustomerTest {

	@Test
	public void testEquals() {
		EqualsVerifier.simple().forClass(Customer.class).verify();
	}
	
	@Test
	public void testToString() {
		
		Customer customer = Customer.builder()
				.id(1L)
				.firstName("Greg")
				.surname("Davies")
				.build();
		
		String customerStr = "Customer [\n\tID:\t1\n\tName:\tGreg Davies\n]";
		
		assertEquals(customerStr, customer.toString());
	}

}
