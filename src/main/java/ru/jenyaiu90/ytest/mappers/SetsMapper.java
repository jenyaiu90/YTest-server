package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.Result;
import ru.jenyaiu90.ytest.entity.Set;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SetsMapper implements RowMapper<Set>
{
	public Set mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Set set = new Set();
		set.setId(rs.getInt("ID"));
		set.setGroup(rs.getInt("GROUP"));
		set.setTest(rs.getInt("TEST"));
		return set;
	}
}
