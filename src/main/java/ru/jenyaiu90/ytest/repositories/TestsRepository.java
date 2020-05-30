package ru.jenyaiu90.ytest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.jenyaiu90.ytest.entity.*;
import ru.jenyaiu90.ytest.mappers.*;

import java.util.List;

@Component
public class TestsRepository
{
	@Autowired
	protected JdbcTemplate jdbc;

	//Создание теста
	public int createTest(Test test)
	{
		return jdbc.update("INSERT INTO \"TESTS\" (\"NAME\", \"SUBJECT\") VALUES (?, ?)",
				test.getName(), test.getSubject());
	}

	//Получить последний созданный тест
	public List<Test> getLast()
	{
		return jdbc.query("SELECT * FROM \"TESTS\" ORDER BY \"ID\" DESC LIMIT 1",
				new TestsMapper());
	}

	//Задать автора теста
	public int createTestUser(Test test, User user)
	{
		return jdbc.update("INSERT INTO \"TESTS_USERS\" (\"USER\", \"TEST\") VALUES (?, ?)",
				user.getId(), test.getId());
	}

	//Получить тест по id
	public List<Test> getTest(int id)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" = ?",
				new TestsMapper(),
				id);
	}

	//Отметить тест как проверенный учителем
	public int checkAnswer(Answer answer, int points)
	{
		return jdbc.update("UPDATE \"ANSWERS\" SET \"POINTS\" = ?, \"IS_CHECKED\" = TRUE WHERE \"ID\" = ?",
				points, answer.getId());
	}

	//Добавить результат прохождения теста
	public List<Result> createResult(Test test, User user)
	{
		jdbc.update("INSERT INTO \"RESULTS\" (\"USER\", \"TEST\") VALUES (?, ?)",
				user.getId(), test.getId());
		return jdbc.query("SELECT * FROM \"RESULTS\" WHERE \"USER\" = ? AND \"TEST\" = ?",
				new ResultsMapper(),
				user.getId(), test.getId());
	}

	//Добавить ответ на одно из заданий теста
	public int createAnswer(Answer answer)
	{
		return jdbc.update("INSERT INTO \"ANSWERS\" (\"RESULT\", \"TASK\", \"ANSWER\", \"IMAGE_ANSWER\", \"IS_CHECKED\", \"POINTS\") VALUES (?, ?, ?, ?, ?, ?)",
				answer.getResult(), answer.getTask(), answer.getAnswer(), answer.getImageAnswer(), answer.getIsChecked(), answer.getPoints());
	}

	//Получить результаты прохождения теста пользователем
	public List<Answer> getAnswers(User user, Test test)
	{
		return jdbc.query("SELECT * FROM \"ANSWERS\" WHERE \"RESULT\" IN (SELECT \"ID\" FROM \"RESULTS\" WHERE \"USER\" = ? AND \"TEST\" = ?)",
				new AnswersMapper(),
				user.getId(), test.getId());
	}

	//Получить список тестов, заданных пользователю
	public List<Test> getTestsFor(User user)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" IN (SELECT \"TEST\" FROM \"SETS\" WHERE \"GROUP\" IN (SELECT \"GROUP\" FROM \"GROUPS_USERS\" WHERE \"USER\" = ?))", //Обожаю SQL за трёхэтажные запросы
				new TestsMapper(),
				user.getId());
	}

	//Получить список тестов, созданных пользователем
	public List<Test> getTestsOf(User user)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" IN (SELECT \"TEST\" FROM \"TESTS_USERS\" WHERE \"USER\" = ?)",
				new TestsMapper(),
				user.getId());
	}

	//Получить автора теста
	public List<User> getAuthorOfTest(Test test)
	{
		return jdbc.query("SELECT * FROM \"USERS\" WHERE \"ID\" IN (SELECT \"USER\" FROM \"TESTS_USERS\" WHERE \"TEST\" = ?)",
				new UsersMapper(),
				test.getId());
	}

	//Получить результат прохождение теста пользователем
	public List<Result> getResultOfTest(Test test, User user)
	{
		return jdbc.query("SELECT * FROM \"RESULTS\" WHERE \"TEST\" = ? AND \"USER\" = ?",
				new ResultsMapper(),
				test.getId(), user.getId());
	}
}
