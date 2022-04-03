package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAOTest {

	private final OrderDAO dao = new OrderDAO();

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
	 * Test creation with a valid order, expects success.
	 */
	@Test
	public void testCreate() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o1 = Order.builder().customer(c1).build();
		o1.addItem(i1, 5);
		o1.addItem(i2, 3);
		
		final Order expected = o1;
		expected.setId(3L);
		
		assertEquals(expected, dao.create(o1));
	}
	
	/**
	 * Test creation with an invalid customer, expects failure.
	 */
	@Test
	public void testCreateWithInvalidCustomer() {
		Customer c1 = new Customer(5L, "John", "Smith");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o1 = Order.builder().customer(c1).build();
		o1.addItem(i1, 5);
		o1.addItem(i2, 3);
		
		final Order expected = o1;
		expected.setId(3L);
		
		assertEquals(null, dao.create(o1));
	}
	
	/**
	 * Test creation with an invalid item, expects failure.
	 */
	@Test
	public void testCreateWithInvalidItem() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(5L, "Wooden board", 9.99);
		
		Order o1 = Order.builder().customer(c1).build();
		o1.addItem(i1, 5);
		o1.addItem(i2, 3);
		
		final Order expected = o1;
		expected.setId(3L);
		
		assertEquals(null, dao.create(o1));
	}
	
	/**
	 * Test creation with null, expects failure.
	 */
	@Test
	public void testCreateWithNull() {
		assertNull(dao.create(null));
	}

	/**
	 * Test read all with a populated database, expects success.
	 */
	@Test
	public void testReadAll() {
		List<Order> expected = new ArrayList<>();
		
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		Customer c2 = new Customer(3L, "Jane", "Smith");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o1 = Order.builder().id(1L).customer(c1).build();
		o1.addItem(i1, 5);
		o1.addItem(i2, 3);
		Order o2 = Order.builder().id(2L).customer(c2).build();
		o2.addItem(i2, 2);
		
		expected.add(o1);
		expected.add(o2);
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
	 * Test read latest with a valid order, expects success.
	 */
	@Test
	public void testReadLatestID() {
		
		Customer c2 = new Customer(3L, "Jane", "Smith");
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o2 = Order.builder().id(2L).customer(c2).build();
		o2.addItem(i2, 2);
		
		assertEquals(Long.valueOf(2), dao.readLatestID());
	}
	
	/**
	 * Test read latest with an empty database, expects failure.
	 */
	@Test
	public void testReadLatestWhenEmpty() {
		windDown();
		assertNull(dao.readLatestID());
	}

	/**
	 * Test read by ID with a valid ID, expects success.
	 */
	@Test
	public void testRead() {
		final long ID = 1L;
		
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o1 = Order.builder().id(ID).customer(c1).build();
		o1.addItem(i1, 5);
		o1.addItem(i2, 3);
		
		assertEquals(o1, dao.read(ID));
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
	 * Test update with a valid order, lowering item quantity, expects success.
	 */
	@Test
	public void testUpdateLowerQuantity() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o1 = Order.builder().id(1L).customer(c1).build();
		o1.addItem(i1, 1);
		o1.addItem(i2, 3);
		
		assertEquals(o1, dao.update(o1));
	}
	
	/**
	 * Test update with a valid order, raising item quantity, expects success.
	 */
	@Test
	public void testUpdateHigherQuantity() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		
		Order o1 = Order.builder().id(1L).customer(c1).build();
		o1.addItem(i1, 8);
		o1.addItem(i2, 4);
		
		assertEquals(o1, dao.update(o1));
	}
	
	/**
	 * Test update with a valid customer with new item, expects success.
	 */
	@Test
	public void testUpdateNewItem() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		Item i3 = new Item(1L, "Bodyboard", 19.99);
		
		Order o1 = Order.builder().id(1L).customer(c1).build();
		o1.addItem(i1, 8);
		o1.addItem(i2, 4);
		o1.addItem(i3, 4);
		
		assertEquals(o1, dao.update(o1));
	}
	
	/**
	 * Test update with a valid customer with nonexistent item, expects failure.
	 */
	@Test
	public void testUpdateInvalidItem() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(3L, "Skateboard", 16.99);
		Item i3 = new Item(4L, "Wooden board", 9.99);
		
		Order o1 = Order.builder().id(1L).customer(c1).build();
		o1.addItem(i1, 8);
		o1.addItem(i2, 4);
		o1.addItem(i3, 4);
		
		assertEquals(null, dao.update(o1));
	}
	
	/**
	 * Test update with a null order, expects failure.
	 */
	@Test
	public void testUpdateWithNull() {
		assertNull(dao.update(null));
	}

	/**
	 * Test deletion with a valid order, expects success.
	 */
	@Test
	public void testDelete() {
		assertEquals(1, dao.delete(1));
	}
	
	/**
	 * Test deletion order doesn't exist, expects failure.
	 */
	@Test
	public void testDeleteWhenNonExistant() {
		assertEquals(0, dao.delete(4));
	}
	
}
