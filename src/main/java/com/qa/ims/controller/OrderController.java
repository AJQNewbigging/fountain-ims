package com.qa.ims.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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
	
	private ItemController itemController;

	@Override
	public List<Order> readAll() {
		List<Order> orders = orderDAO.readAll();
		
		orders.forEach(i -> {
			LOGGER.info(i);
		});
		
		boolean listItems = false;
		
		do {
			LOGGER.info("Would you like to see the items of a specific order? "
					+ "Type 'yes' to do so, or anything else to continue.");
			if (util.getString().toLowerCase().equals("yes")) {
				listItems = true;
				LOGGER.info("What is the ID of the order whose items you'd "
						+ "like to see?");
				Long orderId = util.getLong();
				
				Optional<Order> optOrder = 
						orders.stream().filter(o -> o.getId() == orderId)
						.findFirst();
				
				listItemsForOrder(optOrder);
			} else {
				listItems = false;
			}
		} while (listItems);
		
		return orders;
	}
	
	private void listItemsForOrder(Optional<Order> optOrder) {
		
		if (optOrder.isPresent()) {
			LOGGER.info("Items for order with ID " + optOrder.get().getId());
			optOrder.get().getItems().forEach((i, q) -> {
				LOGGER.info(i + " x" + q);
			});
		} else {
			LOGGER.info("No items found for the given order ID");
		}
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
		LOGGER.info("What is the ID of the order you'd like to modify?");
		Order order = orderDAO.read(util.getLong());
		if (order == null) {
			LOGGER.info("There is no order with that ID");
			return null;
		}
		LOGGER.info("Please choose one of the following:\n"
				+ "- Add to order (type 'add')\n"
				+ "- Remove from order (type 'remove')\n"
				+ "- Return to menu (type 'return')");
		String option = util.getString();
		
		switch (option.toLowerCase()) {
		case "add":
			order = addItemsToOrder(order);
			break;
		case "remove":
			order = removeItemsFromOrder(order);
			break;
		default:
			return null;
		}
		
		order = orderDAO.update(order);
		
		LOGGER.info("Order successfully updated:");
		LOGGER.info(order);
		
		return order;
	}
	
	private Order addItemsToOrder(Order order) {
		LOGGER.info("Here are the Items available to add to this order:");
		itemController.readAll();
		boolean addAnother = false;
		do {
			LOGGER.info("What is the ID of the item you would like to add?");
			Long itemId = util.getLong();
			Integer quantity = 0;
			while (quantity < 1) {
				LOGGER.info("How many of this item? Minimum 1.");
				quantity = util.getInteger();
			}
			
			Entry<Item, Integer> curItem = order.getItemEntryById(itemId);
			
			if (curItem != null) {
				order.addItem(curItem.getKey(), quantity + curItem.getValue());
			} else {
				order.addItem(Item.builder().id(itemId).build(), quantity);
			}
			
			LOGGER.info("Would you like to add another? "
					+ "Type 'yes' to add, or anything else to continue.");
			addAnother = util.getString().toLowerCase().equals("yes");
		} while (addAnother);
		
		LOGGER.info("Adding to order...");
		return order;
	}
	
	private Order removeItemsFromOrder(Order order) {
		LOGGER.info("Here are the Items in this order:");
		listItemsForOrder(Optional.of(order));
		boolean rmvAnother = false;
		do {
			
			Optional<Entry<Item, Integer>> optEntry = Optional.empty();
			
			while (!optEntry.isPresent()) {
				LOGGER.info("What is the ID of the item you would like to remove?");
				Long itemId = util.getLong();
				optEntry = order.getItems().entrySet()
						.stream()
						.filter(e -> e.getKey().getId() == itemId)
						.findFirst();
				
				if (!optEntry.isPresent()) {
					LOGGER.info("This order doesn't have any items by that ID");
				}
			}
			
			Integer quantity = 0;
			Item item = optEntry.get().getKey();
			Integer curQuan = optEntry.get().getValue();
			
			while (quantity < 1 || quantity > curQuan) {
				LOGGER.info("How many of this item? Minimum 1, maximum " + curQuan);
				quantity = util.getInteger();
			}
			
			if (curQuan == quantity) {
				order.removeItem(item);
			} else {
				order.addItem(item, curQuan - quantity);
			}
			
			LOGGER.info("Would you like to remove another? "
					+ "Type 'yes' to remove, or anything else to continue.");
			rmvAnother = util.getString().toLowerCase().equals("yes");
		} while (rmvAnother);
		
		LOGGER.info("Removing from order...");
		return order;
	}


	@Override
	public int delete() {
		LOGGER.info("You cannot undo this action, type 'yes' to continue, "
				+ "or anything else to return.");
		String confirm = util.getString();
		if (!confirm.toLowerCase().equals("yes")) {
			LOGGER.info("Cancelling request...");
			return 0;
		}
		LOGGER.info("What is the ID of the order you would like removed?");
		Long id = util.getLong();
		int o = orderDAO.delete(id);
		LOGGER.info("Order deleted successfully");
		return o;
	}

}
