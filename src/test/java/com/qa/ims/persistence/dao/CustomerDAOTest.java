package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.utils.DBUtils;

public class CustomerDAOTest {

	private final CustomerDAO DAO = new CustomerDAO();

	@Before
	public void setup() {
		DBUtils.connect();
		DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
	}
	
	@After
	public void windDown() {
		DBUtils.getInstance().init("src/test/resources/sql-winddown.sql");
	}

	/**
	 * Test creation with a valid user, expects success.
	 */
	@Test
	public void testCreate() {
		String firstName = "chris";
		String lastName = "perrins";
		final Customer create = new Customer(firstName, lastName);
		final Customer created = new Customer(4L, firstName, lastName);
		assertEquals(created, DAO.create(create));
	}
	
	/**
	 * Test creation with null, expects failure.
	 */
	@Test
	public void testCreateWithNull() {
		assertNull(DAO.create(null));
	}

	/**
	 * Test read all with an populated database, expects success.
	 */
	@Test
	public void testReadAll() {
		List<Customer> expected = new ArrayList<>();
		expected.add(new Customer(1L, "Jordan", "Harrison"));
		expected.add(new Customer(2L, "Asher", "Newbigging"));
		expected.add(new Customer(3L, "Jane", "Smith"));
		assertEquals(expected, DAO.readAll());
	}
	
	/**
	 * Test read all with an empty database, expects failure.
	 */
	@Test
	public void testReadAllWhenEmpty() {
		windDown();
		assertEquals(new ArrayList<>(), DAO.readAll());
	}

	/**
	 * Test read latest with a valid user, expects success.
	 */
	@Test
	public void testReadLatest() {
		assertEquals(new Customer(3L, "Jane", "Smith"), DAO.readLatest());
	}
	
	/**
	 * Test read latest with an empty database, expects failure.
	 */
	@Test
	public void testReadLatestWhenEmpty() {
		windDown();
		assertNull(DAO.readLatest());
	}

	/**
	 * Test read by ID with a valid ID, expects success.
	 */
	@Test
	public void testRead() {
		final long ID = 1L;
		assertEquals(new Customer(ID, "Jordan", "Harrison"), DAO.read(ID));
	}
	
	/**
	 * Test read by ID with an empty database, expects failure.
	 */
	@Test
	public void testReadWhenEmpty() {
		windDown();
		assertNull(DAO.read(1L));
	}

	/**
	 * Test update with a valid customer, expects success.
	 */
	@Test
	public void testUpdate() {
		final Customer updated = new Customer(1L, "chris", "perrins");
		assertEquals(updated, DAO.update(updated));
	}
	
	/**
	 * Test update with a null customer, expects failure.
	 */
	@Test
	public void testUpdateWithNull() {
		assertNull(DAO.update(null));
	}

	/**
	 * Test deletion when a customer has no orders, expects success.
	 */
	@Test
	public void testDelete() {
		assertEquals(1, DAO.delete(1));
	}
	
	/**
	 * Test deletion when a customer has an order, expects failure.
	 */
	@Test
	public void testDeleteWhenHasOrder() {
		assertEquals(0, DAO.delete(2));
	}
}
