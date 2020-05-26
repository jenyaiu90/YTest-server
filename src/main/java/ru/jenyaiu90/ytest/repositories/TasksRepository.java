package ru.jenyaiu90.ytest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.jenyaiu90.ytest.entity.Task;
import ru.jenyaiu90.ytest.entity.Test;
import ru.jenyaiu90.ytest.mappers.TasksMapper;

import java.util.List;

@Component
public class TasksRepository
{
	@Autowired
	protected JdbcTemplate jdbc;

	public List<Task> getTask(int id)
	{
		return jdbc.query("SELECT * FROM \"TASKS\" WHERE \"ID\" = ?",
				new TasksMapper(),
				id);
	}

	public List<Task> getLast()
	{
		return jdbc.query("SELECT * FROM \"TASKS\" ORDER BY \"ID\" DESC LIMIT 1",
				new TasksMapper());
	}

	public List<Task> getTasksOfTest(Test test)
	{
		return jdbc.query("SELECT * FROM \"TASKS\" WHERE \"TEST\" = ?",
				new TasksMapper(),
				test.getId());
	}

	public int createTask(Task task)
	{
		String type, choice, answer;
		switch (task.getType())
		{
			case ONE:
				type = "One";
				break;
			case MANY:
				type = "Many";
				break;
			case SHORT:
				type = "Short";
				break;
			default:
				type = "Long";
				break;
		}
		if (task.getChoice() == null || task.getChoice().length == 0)
		{
			choice = null;
		}
		else
		{
			choice = "";
			for (String i : task.getChoice())
			{
				choice += i + "/=@/";
			}
			choice = choice.substring(0, choice.length() - 4);
		}
		if (task.getAnswer() == null || task.getAnswer().length == 0)
		{
			answer = null;
		}
		else
		{
			answer = "";
			for (String i : task.getAnswer())
			{
				answer += i + "/=@/";
			}
			answer = answer.substring(0, answer.length() - 4);
		}

		return jdbc.update("INSERT INTO \"TASKS\" (\"TYPE\", \"NUM\", \"IMAGE\", \"TEXT\", \"COST\", \"CHOICE\", \"ANSWER\", \"TEST\") " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
				type, task.getNum(), task.getImage(), task.getText(), task.getCost(), choice, answer, task.getTest());
	}
}
