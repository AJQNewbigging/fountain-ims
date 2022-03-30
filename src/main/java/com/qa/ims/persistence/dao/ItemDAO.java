package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;

public class ItemDAO implements Dao<Item> {
	
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public List<Item> readAll() {
		List<Item> items = new ArrayList<>();
		
		try {
			Connection con = DBUtils.getInstance().getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM items");
			
			while(rs.next()) {
				items.add(modelFromResultSet(rs));
			}
			
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return items;
	}

	@Override
	public Item read(Long id) {
		Item item = null;
		
		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM items WHERE id = ?");
			stmt.setLong(1, id);
			
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				item = modelFromResultSet(rs);
			}
			
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return item;
	}

	@Override
	public Item create(Item item) {
		
		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO items(name, price) VALUES (?,?);");
			stmt.setString(1, item.getName());
			stmt.setDouble(2, item.getPrice());
			stmt.executeUpdate();
			
			item = this.readLatest();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return item;
	}
	
	public Item readLatest() {
		Item item = null;
		
		try { 
			Connection con = DBUtils.getInstance().getConnection();
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM items ORDER BY id DESC LIMIT 1");
			
			rs.next();
			item = modelFromResultSet(rs);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return item;
	}

	@Override
	public Item update(Item item) {
		
		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"UPDATE items SET name = ?, price = ? WHERE id = ?");
			stmt.setString(1, item.getName());
			stmt.setDouble(2, item.getPrice());
			stmt.setLong(3, item.getId());
			stmt.executeUpdate();
			
			item = this.read(item.getId());
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return item;
	}

	@Override
	public int delete(long id) {
		
		try {
			Connection con = DBUtils.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM items WHERE id = ?");
			stmt.setLong(1, id);
			
			return stmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		
		return 0;
	}

	@Override
	public Item modelFromResultSet(ResultSet rs) throws SQLException {
		return Item.builder()
				.id(rs.getLong("id"))
				.name(rs.getString("name"))
				.price(rs.getDouble("price"))
				.build();
	}

}
