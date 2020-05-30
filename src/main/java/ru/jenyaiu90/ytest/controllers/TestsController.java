package ru.jenyaiu90.ytest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.jenyaiu90.ytest.TaskComparator;
import ru.jenyaiu90.ytest.entity.*;
import ru.jenyaiu90.ytest.repositories.TasksRepository;
import ru.jenyaiu90.ytest.repositories.TestsRepository;
import ru.jenyaiu90.ytest.repositories.UsersRepository;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestsController
{
	@Autowired
	protected TestsRepository testsRep;
	@Autowired
	protected TasksRepository tasksRep;
	@Autowired
	protected UsersRepository usersRep;

	//Создание нового теста
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ServerAnswer createTest(@RequestParam("test_name") String test_name, @RequestParam("subject") String subject, @RequestBody List<Task> tasks, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> u = usersRep.getUser(login);
		Test test = new Test();
		test.setName(test_name);
		test.setSubject(subject);
		if (!u.isEmpty()) //Проверка пользователя на существование
		{
			if (password.equals(u.get(0).getPassword())) //Проверка правильности пароля
			{
				if (u.get(0).getIsTeacher()) //Проверка наличия статуса учителя
				{
					testsRep.createTest(test);
					test = testsRep.getLast().get(0);
					//Создание заданий
					for (Task i : tasks)
					{
						i.setTest(test.getId());
						i.setNum(i.getNum() + 1); //Нумерация начинается с нуля, но в базе должна начинаться с одного
						tasksRep.createTask(i);
					}
					testsRep.createTestUser(test, u.get(0));
					System.out.println("User " + login + " has created the test \"" + test.getName() + "\"");
					return new ServerAnswer(ServerAnswer.OK);
				}
				else
				{
					System.out.println("User " + login + " couldn`t create test because he isn`t a teacher");
					return new ServerAnswer(ServerAnswer.NO_ACCESS);
				}
			}
			else
			{
				System.out.println("User " + login + " couldn`t create test because of wrong password");
				return new ServerAnswer(ServerAnswer.PASSWORD);
			}
		}
		else
		{
			System.out.println("User " + login + " couldn`t create test because that login wasn`t found");
			return new ServerAnswer(ServerAnswer.NO_USER);
		}
	}

	//Получение теста по id
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Test getTest(@RequestParam("test_id") int test_id)
	{
		List<Test> test = testsRep.getTest(test_id);
		if (!test.isEmpty()) //Проверка теста на существование
		{
			System.out.println("Request for test with id " + test_id + ". Test was found");
			return test.get(0);
		}
		else
		{
			System.out.println("Request for test with id " + test_id + ". Test wasn`t found");
			return null;
		}
	}

	//Отметить ответ как проверенный учителем
	@RequestMapping(value = "/check", method = RequestMethod.PUT)
	public ServerAnswer checkAnswer(@RequestBody Answer answer, @RequestParam("points") int points, @RequestParam("login") String login, @RequestParam("password") String passord)
	{
		List<Task> task = tasksRep.getTask(answer.getTask());
		if (task.isEmpty()) //Проверка задания на существование
		{
			//Нарушение целостности базы данных!
			System.out.println("User " + login + " couldn`t check the answers because the task of test wasn`t found");
			System.out.println("\tWARNING! The answer with id " + answer.getId() + " hasn`t a task");
			return new ServerAnswer(ServerAnswer.NO_TASK);
		}
		List<Test> test = testsRep.getTest(task.get(0).getTest());
		if (test.isEmpty()) //Проверка теста на существование
		{
			//Нарушение целостности базы данных!
			System.out.println("User " + login + " couldn`t check the answers because the test of test wasn`t found");
			System.out.println("\tWARNING! The task with id " + task.get(0).getId() + " hasn`t a test");
			return new ServerAnswer(ServerAnswer.NO_TEST);
		}
		List<User> user = testsRep.getAuthorOfTest(test.get(0));
		if (user.isEmpty()) //Проверка пользователя на существование
		{
			//Нарушение целостности базы данных!
			System.out.println("User " + login + " couldn`t check the answers because the author of test wasn`t found");
			System.out.println("\tWARNING! The test with id " + test.get(0).getId() + " hasn`t an author");
			return new ServerAnswer(ServerAnswer.NO_AUTHOR);
		}
		if (user.get(0).getLogin().equals(login)) //Проверка на наличие необходимых прав
		{
			if (user.get(0).getPassword().equals(passord)) //Проверка правильности пароля
			{
				testsRep.checkAnswer(answer, points);
				System.out.println("One of answers was checked by " + login);
				return new ServerAnswer(ServerAnswer.OK);
			}
			else
			{
				System.out.println("User " + login + " couldn`t check the answers because of wrong password");
				return new ServerAnswer(ServerAnswer.PASSWORD);
			}
		}
		else
		{
			System.out.println("User " + login + " couldn`t check the answers because he isn`t an author of test");
			return new ServerAnswer(ServerAnswer.NO_ACCESS);
		}
	}

	//Добавление данных учеником ответов на тест
	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public ServerAnswer setAnswer(@RequestParam("answers") String[] answers, @RequestParam("test_id") int test_id, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			if (user.get(0).getPassword().equals(password)) //Проверка правильности пароля
			{
				List<Test> test = testsRep.getTest(test_id);
				if (!test.isEmpty()) //Проверка теста на существование
				{
					List<Result> result = testsRep.createResult(test.get(0), user.get(0));
					List<Task> tasks = tasksRep.getTasksOfTest(test.get(0));
					tasks.sort(new TaskComparator());
					for (int i = 0; i < answers.length && i < tasks.size(); i++)
					{
						//Создание ответов
						Answer answer = new Answer();
						answer.setResult(result.get(0).getId());
						answer.setTask(tasks.get(i).getId());
						answer.setAnswer(answers[i].equals("/@=/") ? null : answers[i]);
						answer.setImageAnswer(null);
						if (tasks.get(i).getType() == Task.TaskType.LONG)
						{
							//Ответы на задания с развёрнутым ответом проверяются учителем
							answer.setIsChecked(false);
							answer.setPoints(0);
						}
						else
						{
							//Ответы на все остальные задания проверяются автоматически
							answer.setIsChecked(true);
							boolean cor = false; //Верный ли ответ
							if (answer.getAnswer() != null)
							{
								if (tasks.get(i).getType() == Task.TaskType.MANY)
								{
									cor = Arrays.equals(tasks.get(i).getAnswer(), answer.getAnswer().split("/=@/"));
								}
								else
								{
									for (String j : tasks.get(i).getAnswer())
									{
										if (answer.getAnswer().equals(j))
										{
											cor = true;
											break;
										}
									}
								}
							}
							if (cor)
							{
								answer.setPoints(tasks.get(i).getCost());
							}
							else
							{
								answer.setPoints(0);
							}
						}
						testsRep.createAnswer(answer);
					}
					System.out.println("User " + login + " has solved the test with id " + test_id);
					return new ServerAnswer(ServerAnswer.OK);
				}
				else
				{
					System.out.println("User " + login + " has tried to solve the test with id " + test_id + ", but test wasn`t found");
					return new ServerAnswer(ServerAnswer.NO_TEST);
				}
			}
			else
			{
				System.out.println("User " + login + " has tried to solve the test with id " + test_id + ", but his password if wrong");
				return new ServerAnswer(ServerAnswer.PASSWORD);
			}
		}
		else
		{
			System.out.println("User " + login + "has tried to solve the test with id " + test_id + ", but that user wasn`t found");
			return new ServerAnswer(ServerAnswer.NO_USER);
		}
	}

	//Получить ответы ученика на задания теста
	@RequestMapping(value = "/get_result", method = RequestMethod.GET)
	public List<Answer> getAnswers(@RequestParam("login") String login, @RequestParam("test_id") int test_id)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			List<Test> test = testsRep.getTest(test_id);
			if (!test.isEmpty()) //Проверка теста на существование
			{
				System.out.println("A user has got results of solving the test with id " + test_id + " by " + login);
				return testsRep.getAnswers(user.get(0), test.get(0));
			}
			else
			{
				System.out.println("A user couldn`t get results of test because test with id " + test_id + "wasn`t found");
				return null;
			}
		}
		else
		{
			System.out.println("A user couldn`t get results of test because user " + login + " wasn`t found");
			return null;
		}
	}

	//Получить тесты, заданные всем группам, в которых состоит пользователь
	@RequestMapping(value = "/get_for_user", method = RequestMethod.GET)
	public List<Test> getTestsForUser(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			System.out.println("User has got the tests for " + login);
			return testsRep.getTestsFor(user.get(0));
		}
		else
		{
			System.out.println("User couldn`t get the tests for " + login + " because this user wasn`t found");
			return null;
		}
	}

	//Получить список тестов, созданных пользователем
	@RequestMapping(value = "/get_of_user", method = RequestMethod.GET)
	public List<Test> getTestsOfUser(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			System.out.println("User has got the tests of " + login);
			return testsRep.getTestsOf(user.get(0));
		}
		else
		{
			System.out.println("User couldn`t get the tests of \" + login + \" because this user wasn`t found");
			return null;
		}
	}

	//Получить автора теста
	@RequestMapping(value = "/get_author", method = RequestMethod.GET)
	public User getAuthorOfTest(@RequestParam("test_id") int test_id)
	{
		List<Test> test = testsRep.getTest(test_id);
		if (test.isEmpty()) //Проверка теста на существование
		{
			System.out.println("Attempt to get the author of the test with id " + test_id + ", but this test wasn`t found");
			return null;
		}
		else
		{
			List<User> user = testsRep.getAuthorOfTest(test.get(0));
			if (user.isEmpty()) //Проверка автора на существование
			{
				//Нарушение целостности базы данных!
				System.out.println("Attempt to get the author of the test with id " + test_id + ", but this author wasn`t found");
				System.out.println("\tWARNING! Reference to missing user!");
				return null;
			}
			else
			{
				System.out.println("The request for getting the author of test with id " + test_id + " is granted");
				return user.get(0);
			}
		}
	}

	//Получить сведения о том, решён ли тест учеником
	@RequestMapping(value = "/get_is_solved", method = RequestMethod.GET)
	public ServerAnswer getIsSolved(@RequestParam("login") String login, @RequestParam("test_id") int test_id)
	{
		List<User> user = usersRep.getUser(login);
		List<Test> test = testsRep.getTest(test_id);
		if (!user.isEmpty() && !test.isEmpty()) //Проверка пользователя и теста на существование
		{
			List<Result> result = testsRep.getResultOfTest(test.get(0), user.get(0));
			System.out.println("A user has got the information about whether the test with id " + test_id + " by " + login);
			if (result.isEmpty())
			{
				return new ServerAnswer("Not solved");
			}
			else
			{
				return new ServerAnswer("Solved");
			}
		}
		else
		{
			System.out.println("A user couldn`t get the information about whether the test with id " + test_id + " was solved by " + login);
			return new ServerAnswer("Error");
		}
	}
}
