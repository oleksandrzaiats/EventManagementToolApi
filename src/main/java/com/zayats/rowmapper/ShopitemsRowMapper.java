package com.zayats.rowmapper;

import com.zayats.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopitemsRowMapper implements RowMapper {
	public Object mapRow(ResultSet result, int arg1) throws SQLException {

		Task temp = new Task();
		temp.setId(result.getInt("shopitem_id"));
		temp.setName(result.getString("name"));
		temp.setQuantity(result.getString("quantity"));
//		temp.setName(result.getString("bought"));

		return temp;
	}
}