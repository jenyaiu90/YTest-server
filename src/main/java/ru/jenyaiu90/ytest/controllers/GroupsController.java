package ru.jenyaiu90.ytest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.jenyaiu90.ytest.entity.Group;
import ru.jenyaiu90.ytest.entity.ServerAnswer;
import ru.jenyaiu90.ytest.entity.Test;
import ru.jenyaiu90.ytest.entity.User;
import ru.jenyaiu90.ytest.repositories.GroupsRepository;
import ru.jenyaiu90.ytest.repositories.TestsRepository;
import ru.jenyaiu90.ytest.repositories.UsersRepository;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupsController
{
	@Autowired
	protected GroupsRepository groupsRep;

	@Autowired
	protected UsersRepository usersRep;

	@Autowired
	protected TestsRepository testsRep;

	//Создание новой группы
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ServerAnswer createGroup(@RequestBody Group group, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка на существование пользователя
		{
			group.setAdmin(user.get(0).getId());
			if (password.equals(user.get(0).getPassword())) //Проверка пароля
			{
				groupsRep.createGroup(group);
				System.out.println("User " + login + " has successfully created a new group with name " + group.getName());
				return new ServerAnswer(ServerAnswer.OK);
			}
			else
			{
				System.out.println("User " + login + " couldn`t create a group because of wrong password");
				return new ServerAnswer(ServerAnswer.PASSWORD);
			}
		}
		else
		{
			System.out.println("Couldn`t create group because user " + login + " wasn`t found");
			return new ServerAnswer(ServerAnswer.NO_USER);
		}
	}

	//Присоединение к группе
	@RequestMapping(value = "/join", method = RequestMethod.PUT)
	public ServerAnswer joinGroup(@RequestParam("group_id") int group_id, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			if (user.get(0).getPassword().equals(password)) //Проверка правильности пароля
			{
				List<Group> group = groupsRep.getGroup(group_id);
				if (!group.isEmpty()) //Проверка группы на существование
				{
					System.out.println("A user " + login + " has joined the group with id " + group_id);
					List<Group> groups = groupsRep.getGroupsWith(user.get(0)); //Получение списка групп, в которых есть пользователь
					boolean contains = false;
					for (Group i : groups)
					{
						if (group.get(0).getId() == i.getId())
						{
							contains = true;
							break;
						}
					}
					if (!contains)
					{
						//Добавление пользователя в группу только в том случае, если его в этой группе ещё нет
						groupsRep.joinGroup(user.get(0), group.get(0));
					}
					return new ServerAnswer(ServerAnswer.OK);
				}
				else
				{
					System.out.println("User " + login + " couldn`t join the group with id " + group_id + " because that group wasn`t found");
					return new ServerAnswer(ServerAnswer.NO_GROUP);
				}
			}
			else
			{
				System.out.println("User " + login + " couldn`t join the group with id " + group_id + " because of wrong password");
				return new ServerAnswer(ServerAnswer.PASSWORD);
			}
		}
		else
		{
			System.out.println("User " + login + " couldn`t join the group because that user wasn`t found");
			return new ServerAnswer(ServerAnswer.NO_USER);
		}
	}

	//Получение списка групп, созданных пользователем
	@RequestMapping(value = "/get_groups_of", method = RequestMethod.GET)
	public List<Group> getGroupsOf(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			System.out.println("A user has got the groups of user " + login);
			return groupsRep.getGroupsOf(user.get(0));
		}
		else
		{
			System.out.println("Couldn`t find groups of user " + login + " because that user wasn`t found");
			return null;
		}
	}

	//Получение списка групп, в которых состоит пользователь
	@RequestMapping(value = "/get_groups_with", method = RequestMethod.GET)
	public List<Group> getGroupsWith(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			System.out.println("A user has got the groups with user " + login);
			return groupsRep.getGroupsWith(user.get(0));
		}
		else
		{
			System.out.println("Couldn`t find groups with user " + login + " because that user wasn`t found");
			return null;
		}
	}

	//Получить всех пользователей в группе
	@RequestMapping(value = "/get_users", method = RequestMethod.GET)
	public List<User> getUsers(@RequestParam("group_id") int group_id)
	{
		List<Group> group = groupsRep.getGroup(group_id);
		if (!group.isEmpty()) //Проверка группы на существование
		{
			System.out.println("The list of users of group with id " + group_id + " was returned");
			List<User> users = groupsRep.getUsers(group.get(0));
			for (User i : users)
			{
				//Сокрытие настоящего пароля
				i.setPassword("password");
			}
			return users;
		}
		else
		{
			System.out.println("Couldn`t return users of group with id " + group_id + " because that group wasn`t found");
			return null;
		}
	}

	//Задать тест группе
	@RequestMapping(value = "/set", method = RequestMethod.POST)
	public ServerAnswer setTest(@RequestParam("group_id") int group_id, @RequestParam("test_id") int test_id, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty()) //Проверка пользователя на существование
		{
			if (user.get(0).getPassword().equals(password)) //Проверка правильности пароля
			{
				List<Group> group = groupsRep.getGroup(group_id);
				if (!group.isEmpty()) //Проверка группы на существование
				{
					if (group.get(0).getAdmin() == user.get(0).getId()) //Проверка прав администратора
					{
						List<Test> test = testsRep.getTest(test_id);
						if (!test.isEmpty()) //Проверка теста на существование
						{
							groupsRep.setTest(test.get(0), group.get(0));
							System.out.println("The user " + login + " has set the test with id " + test_id + " for the group with id " + group_id);
							return new ServerAnswer(ServerAnswer.OK);
						}
						else
						{
							System.out.println("Couldn`t set the test with id " + test_id + " for the group because that test wasn`t found");
							return new ServerAnswer(ServerAnswer.NO_TEST);
						}
					}
					else
					{
						System.out.println("User " + login + " is not an admin of the group with id " + group_id + " and can`t set tests for it");
						return new ServerAnswer(ServerAnswer.NO_ACCESS);
					}
				}
				else
				{
					System.out.println("Couldn`t set the test for the group with id " + group_id + " because that group wasn`t found");
					return new ServerAnswer(ServerAnswer.NO_GROUP);
				}
			}
			else
			{
				System.out.println("User " + login + " couldn`t set the test for the group because of wrong password");
				return new ServerAnswer(ServerAnswer.PASSWORD);
			}
		}
		else
		{
			System.out.println("User " + login + " couldn`t set the test for the group because that user wasn`t found");
			return new ServerAnswer(ServerAnswer.NO_USER);
		}
	}

	//Получить список всех тестов, которые можно задать группе
	@RequestMapping(value = "/get_tests_for_set", method = RequestMethod.GET)
	public List<Test> getTestsForSet(@RequestParam("group_id") int group_id)
	{
		List<Group> group = groupsRep.getGroup(group_id);
		if (!group.isEmpty()) //Проверка группы на существование
		{
			System.out.println("A user has got the list of the tests that can be set for the group with id " + group_id);
			return groupsRep.getTestsForSet(group.get(0));
		}
		else
		{
			System.out.println("Couldn`t get tests for set for the group with id " + group_id + " because that group wasn`t found");
			return null;
		}
	}

	//Получить список всех тестов, заданных группе
	@RequestMapping(value = "/get_tests_for_group", method = RequestMethod.GET)
	public List<Test> getTestsForGroup(@RequestParam("group_id") int group_id)
	{
		List<Group> group = groupsRep.getGroup(group_id);
		if (!group.isEmpty())
		{
			System.out.println("A user has got the list of the tests for the group with id " + group_id);
			return groupsRep.getTestsForGroup(group.get(0));
		}
		else
		{
			System.out.println("Couldn`t get tests for the group with id " + group_id + " because that group wasn`t found");
			return null;
		}
	}
}
