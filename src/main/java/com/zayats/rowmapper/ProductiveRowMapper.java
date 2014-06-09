package com.zayats.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class ProductiveRowMapper implements RowMapper {
    @Override
    public Map.Entry<String, Integer> mapRow(ResultSet resultSet, int i) throws SQLException {
        TreeMap<String, Integer> result = new TreeMap<String, Integer>();
        result.put(resultSet.getString("first_name") + " " + resultSet.getString("last_name"), resultSet.getInt("amount"));
        return result.firstEntry();
    }
}
