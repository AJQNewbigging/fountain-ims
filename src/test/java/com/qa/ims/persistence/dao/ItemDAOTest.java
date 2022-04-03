package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;

public class ItemDAOTest {

	private final ItemDAO dao = new ItemDAO();

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
		String name = "Pipecleaner";
		Double price = 3.70;
		final Item create = Item.builder().name(name).price(price).build();
		final Item expected = Item.builder().id(4L).name(name).price(price).build();
		assertEquals(expected, dao.create(create));
	}
	
	/**
	 * Test creation with null, expects failure.
	 */
	@Test
	public void testCreateWithNull() {
		assertNull(dao.create(null));
	}

	/**
	 * Test read all with an populated database, expects success.
	 */
	@Test
	public void testReadAll() {
		List<Item> expected = new ArrayList<>();
		expected.add(new Item(1L, "Bodyboard", 19.99));
		expected.add(new Item(2L, "Surfboard", 24.99));
		expected.add(new Item(3L, "Skateboard", 16.99));
		assertEquals(expected, dao.readAll());
	}
	
	/**
	 * Test read all with an empty database, expects failure.
	 */
	@Test
	public void testReadAllWhenEmpty() {
		windDown();
		assertEquals(new ArrayList<>(), dao.readAll());
	}

	/**
	 * Test read latest with a valid user, expects success.
	 */
	@Test
	public void testReadLatest() {
		assertEquals(new Item(3L, "Skateboard", 16.99), dao.readLatest());
	}
	
	/**
	 * Test read latest with an empty database, expects failure.
	 */
	@Test
	public void testReadLatestWhenEmpty() {
		windDown();
		assertNull(dao.readLatest());
	}

	/**
	 * Test read by ID with a valid ID, expects success.
	 */
	@Test
	public void testRead() {
		final long ID = 1L;
		assertEquals(new Item(ID, "Bodyboard", 19.99), dao.read(ID));
	}
	
	/**
	 * Test read by ID with an empty database, expects failure.
	 */
	@Test
	public void testReadWhenEmpty() {
		windDown();
		assertNull(dao.read(1L));
	}

	/**
	 * Test update with a valid customer, expects success.
	 */
	@Test
	public void testUpdate() {
		final Item updated = 
				Item.builder().id(1L).name("Dishwasher").price(0.99).build();
		assertEquals(updated, dao.update(updated));
	}
	
	/**
	 * Test update with a null customer, expects failure.
	 */
	@Test
	public void testUpdateWithNull() {
		assertNull(dao.update(null));
	}

	/**
	 * Test deletion when an item is in no orders, expects success.
	 */
	@Test
	public void testDelete() {
		assertEquals(1, dao.delete(1));
	}
	
	/**
	 * Test deletion when a item is in an order, expects failure.
	 */
	@Test
	public void testDeleteWhenInOrder() {
		assertEquals(0, dao.delete(2));
	}
	
}
