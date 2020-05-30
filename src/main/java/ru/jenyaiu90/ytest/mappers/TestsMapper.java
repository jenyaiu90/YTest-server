package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestsMapper implements RowMapper<Test>
{
	public Test mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Test test = new Test();
		test.setId(rs.getInt("ID"));
		test.setName(rs.getString("NAME"));
		test.setSubject(rs.getString("SUBJECT"));
		return test;
	}
}
