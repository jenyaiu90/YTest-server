package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupsMapper implements RowMapper<Group>
{
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Group result = new Group();
		result.setId(rs.getInt("ID"));
		result.setName(rs.getString("NAME"));
		result.setAdmin(rs.getInt("ADMIN"));
		return result;
	}
}
