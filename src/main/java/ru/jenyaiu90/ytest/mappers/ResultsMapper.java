package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.Result;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultsMapper implements RowMapper<Result>
{
	public Result mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Result result = new Result();
		result.setId(rs.getInt("ID"));
		result.setUser(rs.getInt("USER"));
		result.setTest(rs.getInt("TEST"));
		return result;
	}
}
