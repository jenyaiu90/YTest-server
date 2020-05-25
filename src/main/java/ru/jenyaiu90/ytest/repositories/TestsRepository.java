package ru.jenyaiu90.ytest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.jenyaiu90.ytest.entity.*;
import ru.jenyaiu90.ytest.mappers.ResultsMapper;
import ru.jenyaiu90.ytest.mappers.TestsMapper;
import ru.jenyaiu90.ytest.mappers.UsersMapper;

import java.util.List;

@Component
public class TestsRepository
{
	@Autowired
	protected JdbcTemplate jdbc;

	public int createTest(Test test)
	{
		return jdbc.update("INSERT INTO \"TESTS\" (\"NAME\", \"SUBJECT\") VALUES (?, ?)",
				test.getName(), test.getSubject());
	}

	public int createTestUser(Test test, User user)
	{
		return jdbc.update("INSERT INTO \"TESTS_USERS\" (\"USER\", \"TEST\") VALUES (?, ?)",
				user.getId(), test.getId());
	}

	public List<Test> getTest(int id)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" = ?",
				new TestsMapper(),
				id);
	}

	public List<Result> createResult(Test test, User user)
	{
		jdbc.update("INSERT INTO \"RESULTS\" (\"USER\", \"TEST\") VALUES (?, ?)",
				user.getId(), test.getId());
		return jdbc.query("SELECT * FROM \"RESULTS\" WHERE \"USER\" = ? AND \"TEST\" = ?",
				new ResultsMapper(),
				user.getId(), test.getId());
	}

	public int createAnswer(Answer answer)
	{
		return jdbc.update("INSERT INTO \"ANSWERS\" (\"RESULT\", \"TASK\", \"ANSWER\", \"IMAGE_ANSWER\", \"IS_CHECKED\", \"POINTS\") VALUES (?, ?, ?, ?, ?, ?)",
				answer.getResult(), answer.getTask(), answer.getAnswer(), answer.getImageAnswer(), answer.getIsChecked(), answer.getPoints());
	}

	public List<Test> getTestsFor(User user)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" IN (SELECT \"TEST\" FROM \"SETS\" WHERE \"GROUP\" IN (SELECT \"GROUP\" FROM \"GROUPS_USERS\" WHERE \"USER\" = ?))",
				new TestsMapper(),
				user.getId());
	}

	public List<Test> getTestsOf(User user)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" IN (SELECT \"TEST\" FROM \"TESTS_USERS\" WHERE \"USER\" = ?",
				new TestsMapper(),
				user.getId());
	}

	public List<User> getAuthorOfTest(Test test)
	{
		return jdbc.query("SELECT * FROM \"USERS\" WHERE \"ID\" IN (SELECT \"USER\" FROM \"TESTS_USERS\" WHERE \"TEST\" = ?)",
				new UsersMapper(),
				test.getId());
	}

	public List<Result> getResultOfTest(Test test, User user)
	{
		return jdbc.query("SELECT * FROM \"RESULTS\" WHERE \"TEST\" = ? AND \"USER\" = ?",
				new ResultsMapper(),
				test.getId(), user.getId());
	}
}
