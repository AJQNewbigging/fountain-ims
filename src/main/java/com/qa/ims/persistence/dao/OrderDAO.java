package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAO implements Dao<Order> {
	
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public List<Order> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order read(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order create(Order order) {
		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO orders(customer_id) VALUES (?);");
			stmt.setLong(1, order.getCustomer().getId());
			stmt.executeUpdate();
			
			Long newId = this.readLatestID();
			
			order.setId(newId);
			
			if (!order.getItems().isEmpty()) {
				order = saveItemsToOrder(order);
			}
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return order;
	}
	
	public Long readLatestID() {
		Long orderId = null;
		
		try { 
			Connection con = DBUtils.getInstance().getConnection();
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(
					"SELECT id FROM orders ORDER BY id DESC LIMIT 1");
			
			rs.first();
			orderId = rs.getLong("id");
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return orderId;
	}
	
	public Order saveItemsToOrder(Order order) {
		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"REPLACE INTO order_items(order_id, item_id, quantity) "
					+ "VALUES (?, ?, ?);");
			
			for (Entry<Item, Integer> entry : order.getItems().entrySet()) {
				stmt.setLong(1, order.getId());
				stmt.setLong(2, entry.getKey().getId());
				stmt.setInt(3, entry.getValue());
				stmt.addBatch();
				stmt.clearParameters();
			}
			
			stmt.executeBatch();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return order;
	}

	@Override
	public Order update(Order t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Order modelFromResultSet(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
