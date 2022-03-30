package com.qa.ims.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderController implements CrudController<Order> {
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	private OrderDAO orderDAO;
	
	private Utils util;

	@Override
	public List<Order> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order create() {
		Order order = new Order();
		LOGGER.info("What is ID for the customer owning this order?");
		order.setCustomer(Customer.builder().id(util.getLong()).build());
		Map<Item, Integer> items = new HashMap<>();
		
		boolean addItem = false;
		
		do {
			LOGGER.info("Do you wish to add an item to the order?\n"
					+ "Type yes to add an item, or anything else to continue.");
			if (!util.getString().toLowerCase().equals("yes")) {
				addItem = false;
			} else {
				LOGGER.info("What is ID of the item you would like to add?");
				Item item = Item.builder().id(util.getLong()).build();
				LOGGER.info("How many would you like to add?");
				items.put(item, util.getInteger());
				addItem = true;
			}
			
		} while (addItem);
		
		order.setItems(items);
		orderDAO.create(order);
		LOGGER.info("This order has been successfully created!");
		
		return order;
	}

	@Override
	public Order update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete() {
		// TODO Auto-generated method stub
		return 0;
	}

}
