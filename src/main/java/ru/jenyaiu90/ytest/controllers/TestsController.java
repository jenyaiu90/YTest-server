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

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Test createTest(@RequestParam("test") Test test, @RequestParam("tasks") List<Task> tasks, @RequestParam("user") User user)
	{
		List<User> u = usersRep.getUser(user.getLogin());
		if (!u.isEmpty() && user.getPassword().equals(u.get(0).getPassword()) && u.get(0).getIsTeacher())
		{
			testsRep.createTest(test);
			for (Task i : tasks)
			{
				tasksRep.createTask(i);
			}
			testsRep.createTestUser(test, u.get(0));
			System.out.println("User " + user.getLogin() + " has created the test \"" + test.getName() + "\"");
			return test;
		}
		else
		{
			return null;
		}
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Test getTest(@RequestParam("test_id") int test_id)
	{
		List<Test> test = testsRep.getTest(test_id);
		if (!test.isEmpty())
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

	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public ServerAnswer setAnswer(@RequestParam("answers") String[] answers, @RequestParam("test_id") int test_id, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
		{
			if (user.get(0).getPassword().equals(password))
			{
				List<Test> test = testsRep.getTest(test_id);
				if (!test.isEmpty())
				{
					List<Result> result = testsRep.createResult(test.get(0), user.get(0));
					List<Task> tasks = tasksRep.getTasksOfTest(test.get(0));
					tasks.sort(new TaskComparator());
					for (int i = 0; i < answers.length && i < tasks.size(); i++)
					{
						Answer answer = new Answer();
						answer.setResult(result.get(0).getId());
						answer.setTask(tasks.get(i).getId());
						answer.setAnswer(answers[i].equals("/@=/") ? null : answers[i]);
						answer.setImageAnswer(null);
						if (tasks.get(i).getType() == Task.TaskType.LONG)
						{
							answer.setIsChecked(answer.getAnswer() != null);
							answer.setPoints(0);
						}
						else
						{
							answer.setIsChecked(true);
							boolean cor = false;
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
					return new ServerAnswer("OK");
				}
				else
				{
					System.out.println("User " + login + " has tried to solve the test with id " + test_id + ", but test wasn`t found");
					return new ServerAnswer("No test");
				}
			}
			else
			{
				System.out.println("User " + login + " has tried to solve the test with id " + test_id + ", but his password if wrong");
				return new ServerAnswer("Password");
			}
		}
		else
		{
			System.out.println("User " + login + "has tried to solve the test with id " + test_id + ", but that user wasn`t found");
			return new ServerAnswer("No user");
		}
	}

	@RequestMapping(value = "/get_for_user", method = RequestMethod.GET)
	public List<Test> getTestsForUser(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
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

	@RequestMapping(value = "/get_of_user", method = RequestMethod.GET)
	public List<Test> getTestsOfUser(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
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

	@RequestMapping(value = "/get_author", method = RequestMethod.GET)
	public User getAuthorOfTest(@RequestParam("test_id") int test_id)
	{
		List<Test> test = testsRep.getTest(test_id);
		if (test.isEmpty())
		{
			System.out.println("Attempt to get the author of the test with id " + test_id + ", but this test wasn`t found");
			return null;
		}
		else
		{
			List<User> user = testsRep.getAuthorOfTest(test.get(0));
			if (user.isEmpty())
			{
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

	@RequestMapping(value = "/get_is_solved", method = RequestMethod.GET)
	public ServerAnswer getIsSolved(@RequestParam("login") String login, @RequestParam("test_id") int test_id)
	{
		List<User> user = usersRep.getUser(login);
		List<Test> test = testsRep.getTest(test_id);
		if (!user.isEmpty() && !test.isEmpty())
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