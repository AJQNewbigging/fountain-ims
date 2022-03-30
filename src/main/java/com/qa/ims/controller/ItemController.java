package com.qa.ims.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.dao.Dao;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemController implements CrudController<Item> {
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	private Dao<Item> itemDAO;
	
	private Utils util;

	@Override
	public List<Item> readAll() {
		List<Item> items = itemDAO.readAll();
		
		items.forEach(i -> {
			LOGGER.info(i);
		});
		
		return items;
	}

	@Override
	public Item create() {
		Item item = itemDAO.create(createForm(new Item()));
		
		LOGGER.info(String.format(
				"New item '%s' created successfully!", item.getName()));
		return item;
	}

	@Override
	public Item update() {
		LOGGER.info("What is the ID of the item you wish to modify?");
		Item item = createForm(Item.builder().id(util.getLong()).build());
		
		item = itemDAO.update(item);
		LOGGER.info(String.format("The update has been appled to item of id %d",
				item.getId()));
		
		return item;
	}
	
	private Item createForm(Item item) {
		
		LOGGER.info("Please enter a name for the item:");
		item.setName(util.getString());
		
		LOGGER.info("And the price? (£)");
		item.setPrice(util.getDouble());
		
		return item;
	}

	@Override
	public int delete() {
		// TODO Auto-generated method stub
		return 0;
	}

}
