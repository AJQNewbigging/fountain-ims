package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAO implements Dao<Order> {
	
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public List<Order> readAll() {
		Map<Long, Order> orderMap = new HashMap<>();
				
		try {
			Connection con = DBUtils.getInstance().getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT o.id AS order_id, "
					+ "c.id AS customer_id, c.first_name, c.surname, "
					+ "i.id AS item_id, i.name, i.price, ot.quantity "
					+ "FROM orders o "
					+ "JOIN customers c ON o.customer_id = c.id "
					+ "JOIN order_items ot ON o.id = ot.order_id "
					+ "JOIN items i ON ot.item_id = i.id "
					+ "ORDER BY order_id;");
			
			while (rs.next()) {
				Long orderId = rs.getLong("order_id");
				
				if (!orderMap.containsKey(orderId)) {
					orderMap.put(orderId, modelFromResultSet(rs));
				}
				
				Item item = Item.builder()
						.id(rs.getLong("item_id"))
						.name(rs.getString("name"))
						.price(rs.getDouble("price"))
						.build();
				
				orderMap.get(orderId).addItem(item, rs.getInt("quantity"));
			}
			
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return orderMap.values().stream().collect(Collectors.toList());
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

		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement otDelete = con.prepareStatement("DELETE FROM order_items WHERE order_id = ?");
			otDelete.setLong(1, id);
			
			PreparedStatement oDelete = con.prepareStatement("DELETE FROM orders WHERE id = ?");
			oDelete.setLong(1, id);
			
			return otDelete.executeUpdate() + oDelete.executeUpdate();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return 0;
	}

	@Override
	public Order modelFromResultSet(ResultSet rs) throws SQLException {
		Customer customer = Customer.builder()
				.id(rs.getLong("customer_id"))
				.firstName(rs.getString("first_name"))
				.surname(rs.getString("surname"))
				.build();
		Order order = Order.builder()
				.id(rs.getLong("order_id"))
				.customer(customer)
				.build();
		
		return order;
	}

}
