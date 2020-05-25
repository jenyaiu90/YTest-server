package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.Answer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswersMapper implements RowMapper<Answer>
{
	public Answer mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Answer result = new Answer();
		result.setId(rs.getInt("ID"));
		result.setResult(rs.getInt("RESULT"));
		result.setTask(rs.getInt("TASK"));
		result.setAnswer(rs.getString("ANSWER"));
		result.setImageAnswer(rs.getString("IMAGE_ANSWER"));
		result.setIsChecked(rs.getBoolean("IS_CHECKED"));
		result.setPoints(rs.getInt("POINTS"));
		return result;
	}
}
