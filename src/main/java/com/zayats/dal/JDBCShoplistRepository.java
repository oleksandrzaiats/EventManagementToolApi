package com.zayats.dal;

import com.zayats.controller.UserController;
import com.zayats.model.Task;
import com.zayats.rowmapper.ShopitemsRowMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCShoplistRepository implements ShoplistRepository {

	private final String CREATE_SHOPLIST_STRING = "INSERT INTO shoplists(name, family_id) VALUES(:name, :familyId)";
	private final String DELETE_SHOPLIST_STRING = "DELETE FROM shoplists WHERE shoplist_id=:shoplistId";
	private final String DELETE_SHOPITEM_STRING = "DELETE FROM shopitems WHERE shopitem_id=:shopitemId";
	private final String GET_SHOPLIST_FOR_FAMILY_STRING = "SELECT * FROM shoplists WHERE family_id=:familyId";
	private final String GET_SHOPITEMS_STRING = "SELECT * FROM shopitems WHERE shoplist_id=:shoplistId ORDER BY bought";
	private final String BUY_SHOPITEM_STRING = "UPDATE shopitems SET bought='true' WHERE shopitem_id=:shopitemId";
	private final String ADD_SHOPITEM_STRING = "INSERT INTO shopitems (name, quantity, shoplist_id) VALUES (:name, :quantity, :shoplistId)";

	private static final Logger logger = Logger.getLogger(UserController.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public boolean createShoplist(String name, int familyId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("familyId", familyId);
		parameters.put("name", name);

		try {
			jdbcTemplate.update(CREATE_SHOPLIST_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean deleteShoplist(int shoplistId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shoplistId", shoplistId);

		try {
			jdbcTemplate.update(DELETE_SHOPLIST_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

/*	public List<Shoplist> getShoplistsForFamily(int familyId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("familyId", familyId);

		logger.info("Getting shoplists for family.");
		List<Shoplist> shoplists = jdbcTemplate.query(
				GET_SHOPLIST_FOR_FAMILY_STRING, parameters,
				new ShoplistsRowMapper());
		return shoplists;
	}*/

	public List<Task> getItemsForShoplist(int shoplistId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shoplistId", shoplistId);

		logger.info("Getting tasks for shoplist.");
		List<Task> tasks = jdbcTemplate.query(GET_SHOPITEMS_STRING,
				parameters, new ShopitemsRowMapper());
		return tasks;
	}

	public boolean addShopitem(String name, String quantity, int shoplistId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shoplistId", shoplistId);
		parameters.put("name", name);
		parameters.put("quantity", quantity);

		try {
			jdbcTemplate.update(ADD_SHOPITEM_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean deleteShopitem(int shopitemId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shopitemId", shopitemId);

		try {
			jdbcTemplate.update(DELETE_SHOPITEM_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean buyShopitem(int shopitemId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shopitemId", shopitemId);

		try {
			jdbcTemplate.update(BUY_SHOPITEM_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
