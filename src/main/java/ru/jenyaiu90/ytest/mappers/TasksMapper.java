package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TasksMapper implements RowMapper<Task>
{
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Task task = new Task();
		task.setId(rs.getInt("ID"));
		switch (rs.getString("TYPE"))
		{
			case "One":
				task.setType(Task.TaskType.ONE);
				break;
			case "Many":
				task.setType(Task.TaskType.MANY);
				break;
			case "Short":
				task.setType(Task.TaskType.SHORT);
				break;
			case "Long":
				task.setType(Task.TaskType.LONG);
				break;
		}
		task.setNum(rs.getInt("NUM"));
		task.setImage(rs.getString("IMAGE"));
		task.setText(rs.getString("TEXT"));
		task.setCost(rs.getInt("COST"));
		if (task.getType() == Task.TaskType.ONE || task.getType() == Task.TaskType.MANY)
		{
			task.setChoice(rs.getString("CHOICE").split("/=@/"));
		}
		if (task.getType() != Task.TaskType.LONG)
		{
			task.setAnswer(rs.getString("ANSWER").split("/=@/"));
		}
		task.setTest(rs.getInt("TEST"));
		return task;
	}
}
