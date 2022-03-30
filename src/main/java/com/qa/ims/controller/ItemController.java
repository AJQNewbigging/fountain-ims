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
		LOGGER.info("Please enter a name for the item:");
		String name = util.getString();
		LOGGER.info("And the price? (£)");
		Double price = util.getDouble();
		
		Item item = itemDAO.create(
				Item.builder().name(name).price(price).build());
		
		LOGGER.info(String.format(
				"New item '%s' created successfully!", item.getName()));
		return item;
	}

	@Override
	public Item update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete() {
		// TODO Auto-generated method stub
		return 0;
	}

}
