package com.zayats.rowmapper;

import com.zayats.model.Event;
import com.zayats.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsRowMapper implements RowMapper {
    public Object mapRow(ResultSet result, int arg1) throws SQLException {

        Event temp = new Event();
        temp.setId(result.getInt("id"));
        temp.setName(result.getString("name"));
        temp.setDescription(result.getString("description"));
        temp.setAddress(result.getString("address"));
        temp.setDate(result.getTimestamp("date"));
        User user = new User();
        user.setUserId(result.getInt("owner_id"));
        user.setFirstName(result.getString("first_name"));
        user.setLastName(result.getString("last_name"));
        user.setUsername(result.getString("username"));
        temp.setOwner(user);
        return temp;
    }
}