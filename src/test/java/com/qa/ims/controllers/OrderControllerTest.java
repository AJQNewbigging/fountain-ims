package com.qa.ims.controllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.qa.ims.controller.ItemController;
import com.qa.ims.controller.OrderController;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	@Mock
	private Utils utils;

	@Mock
	private OrderDAO dao;
	
	@Mock
	private ItemController itemController;

	@InjectMocks
	private OrderController controller;
	
	/**
	 * Test order creation without items
	 */
	@Test
	public void testCreateNoItems() {
		Order order = Order.builder()
				.customer(Customer.builder().id(1L).build())
				.build();

		Mockito.when(utils.getLong()).thenReturn(1L);
		Mockito.when(utils.getString()).thenReturn("no");
		Mockito.when(dao.create(order)).thenReturn(order);

		assertEquals(order, controller.create());

		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(1)).create(order);
	}
	
	/**
	 * Test order creation with items 
	 */
	@Test
	public void testCreateWithItems() {
		Mockito.when(utils.getLong()).thenReturn(1L, 2L, 5L);
		Mockito.when(utils.getString()).thenReturn("yes", "yes", "no");
		Mockito.when(utils.getInteger()).thenReturn(5, 3);
		
		Order order = Order.builder()
				.customer(Customer.builder().id(1L).build()).build();
		order.addItem(Item.builder().id(2L).build(), 5);
		order.addItem(Item.builder().id(5L).build(), 3);
		
		Order created = orderObj();
		created.setId(1L);
		Mockito.when(dao.create(order)).thenReturn(created);

		assertEquals(created, controller.create());

		Mockito.verify(utils, Mockito.times(3)).getString();
		Mockito.verify(utils, Mockito.times(3)).getLong();
		Mockito.verify(utils, Mockito.times(2)).getInteger();
		Mockito.verify(dao, Mockito.times(1)).create(order);
	}

	/**
	 * Test read all and show item list
	 */
	@Test
	public void testReadAllShowItems() {
		List<Order> orders = new ArrayList<>();
		
		Order o1 = orderObj();
		o1.setId(1L);
		orders.add(o1);

		Mockito.when(utils.getString()).thenReturn("YeS", "no");
		Mockito.when(utils.getLong()).thenReturn(1L);
		Mockito.when(dao.readAll()).thenReturn(orders);

		assertEquals(orders, controller.readAll());

		Mockito.verify(utils, Mockito.times(2)).getString();
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(1)).readAll();
	}
	
	/**
	 * Test read all with no item list shown
	 */
	@Test
	public void testReadAll() {
		List<Order> orders = new ArrayList<>();
		
		orders.add(orderObj());

		Mockito.when(utils.getString()).thenReturn("no");
		Mockito.when(dao.readAll()).thenReturn(orders);

		assertEquals(orders, controller.readAll());

		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(dao, Mockito.times(1)).readAll();
	}

	/**
	 * Test order update with adding of items, expect success.
	 */
	@Test
	public void testUpdateAdd() {
		Order order = orderObj();
		Long orderId = 1L;
		order.setId(orderId);

		Mockito.when(utils.getLong()).thenReturn(orderId, 2L, 1L);
		Mockito.when(utils.getString()).thenReturn("AdD", "yeS", "no");
		Mockito.when(utils.getInteger()).thenReturn(3, 4);
		Mockito.when(dao.read(orderId)).thenReturn(order);
		
		List<Item> items = order
				.getItems().keySet().stream().collect(Collectors.toList());
		
		items.add(new Item(1L, "Bodyboard", 19.99));
		
		Mockito.when(itemController.readAll()).thenReturn(items);
		
		Map<Item, Integer> updatedItems = new HashMap<>();
		updatedItems.put(items.get(0), 8);
		updatedItems.put(items.get(1), 3);
		updatedItems.put(items.get(2), 4);
		order.setItems(updatedItems);
		
		Mockito.when(dao.update(order)).thenReturn(order);

		assertEquals(order, controller.update());

		Mockito.verify(utils, Mockito.times(3)).getString();
		Mockito.verify(utils, Mockito.times(3)).getLong();
		Mockito.verify(utils, Mockito.times(2)).getInteger();
		Mockito.verify(dao, Mockito.times(1)).update(order);
	}
	
	/**
	 * Test order update with removal of items, expect success.
	 */
	@Test
	public void testUpdateRemove() {
		Order order = orderObj();
		Long orderId = 1L;
		order.setId(orderId);

		Mockito.when(utils.getLong()).thenReturn(orderId, 3L, 2L, 5L);
		Mockito.when(utils.getString()).thenReturn("reMOve", "yeS", "no");
		Mockito.when(utils.getInteger()).thenReturn(1, 5, 2);
		Mockito.when(dao.read(orderId)).thenReturn(order);
		
		List<Item> items = order
				.getItems().keySet().stream().collect(Collectors.toList());
		
		Map<Item, Integer> updatedItems = new HashMap<>();
		updatedItems.put(items.get(0), 4);
		updatedItems.put(items.get(1), 1);
		order.setItems(updatedItems);
		
		Mockito.when(dao.update(order)).thenReturn(order);

		assertEquals(order, controller.update());

		Mockito.verify(utils, Mockito.times(3)).getString();
		Mockito.verify(utils, Mockito.times(4)).getLong();
		Mockito.verify(utils, Mockito.times(3)).getInteger();
		Mockito.verify(dao, Mockito.times(1)).update(order);
	}
	
	/**
	 * Test order update with a 'return' user selection, expect nothing.
	 */
	@Test
	public void testUpdateReturn() {
		Order order = orderObj();
		Long orderId = 1L;
		order.setId(orderId);

		Mockito.when(utils.getLong()).thenReturn(orderId);
		Mockito.when(utils.getString()).thenReturn("return");
		Mockito.when(dao.read(orderId)).thenReturn(order);

		assertEquals(null, controller.update());

		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(0)).update(order);
	}
	
	/**
	 * Test order update with an invalid order ID, expect failure.
	 */
	@Test
	public void testUpdateInvalidID() {
		Order order = orderObj();
		Long orderId = 6L;
		order.setId(orderId);
		Mockito.when(utils.getLong()).thenReturn(orderId);
		Mockito.when(dao.read(orderId)).thenReturn(null);

		assertEquals(null, controller.update());

		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(0)).update(order);
	}

	/**
	 * Tests successful deletion with valid ID
	 */
	@Test
	public void testDelete() {
		final long ID = 1L;

		Mockito.when(utils.getString()).thenReturn("YeS");
		Mockito.when(utils.getLong()).thenReturn(ID);
		Mockito.when(dao.delete(ID)).thenReturn(1);

		assertEquals(1L, this.controller.delete());

		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(1)).delete(ID);
	}
	
	/**
	 * Tests deletion with denied check, so no deletion should be made.
	 */
	@Test
	public void testDeleteWithRejection() {
		Mockito.when(utils.getString()).thenReturn("no");

		assertEquals(0L, this.controller.delete());

		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(0)).getLong();
		Mockito.verifyNoInteractions(dao);
	}
	
	/**
	 * Tests deletion with failed result
	 */
	@Test
	public void testDeleteWithFailure() {
		final long ID = 1L;

		Mockito.when(utils.getString()).thenReturn("YeS");
		Mockito.when(utils.getLong()).thenReturn(ID);
		Mockito.when(dao.delete(ID)).thenReturn(0);

		assertEquals(0L, this.controller.delete());

		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(1)).delete(ID);
	}
	
	private Order orderObj() {
		Customer c1 = new Customer(2L, "Asher", "Newbigging");
		
		Item i1 = new Item(2L, "Surfboard", 24.99);
		Item i2 = new Item(5L, "Wooden board", 9.99);
		
		Order o1 = Order.builder().customer(c1).build();
		o1.addItem(i1, 5);
		o1.addItem(i2, 3);
		
		return o1;
	}
}
