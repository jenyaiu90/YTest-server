package ru.jenyaiu90.ytest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.jenyaiu90.ytest.entity.ServerAnswer;
import ru.jenyaiu90.ytest.entity.User;
import ru.jenyaiu90.ytest.repositories.UsersRepository;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController
{
	@Autowired
	protected UsersRepository usersRep;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ServerAnswer createUser(@RequestBody User user)
	{
		if (usersRep.getUser(user.getLogin()).isEmpty())
		{
			usersRep.createUser(user);
			System.out.println("User " + user.getLogin() + " was created.");
			return new ServerAnswer(ServerAnswer.OK);
		}
		else
		{
			System.out.println("Attempt to create user " + user.getLogin() + " was failed.");
			return new ServerAnswer(ServerAnswer.USER_ALREADY_EXISTS);
		}
	}

	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public User signIn(@RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty() && user.get(0).getPassword().equals(password))
		{
			System.out.println("User " + login + " signed in");
			return user.get(0);
		}
		else
		{
			System.out.println("User " + login + " couldn`t sign in");
			User empty = new User();
			empty.setId(0);
			return empty;
		}
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public User getUser(@RequestParam("login") String login)
	{
		List<User> users = usersRep.getUser(login);
		if (users.isEmpty())
		{
			System.out.println("A user couldn`t get an information about user " + login + " because this user wasn`t found");
			return null;
		}
		else
		{
			System.out.println("A user has got an information about " + login);
			users.get(0).setPassword("password");
			return users.get(0);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ServerAnswer update(@RequestParam("login") String login, @RequestParam("name") String name,
							   @RequestParam("surname") String surname, @RequestParam("email") String email,
							   @RequestParam("phone_number") String phone_number,
							   @RequestParam("old_password") String old_password, @RequestParam("new_password") String new_password)
	{
		List<User> users = usersRep.getUser(login);
		if (users.isEmpty())
		{
			System.out.println("Couldn`t update user " + login + " because this user wasn`t found");
			return new ServerAnswer(ServerAnswer.NO_USER);
		}
		if (users.get(0).getPassword().equals(old_password))
		{
			User user = new User();
			user.setLogin(login);
			user.setName(name);
			user.setSurname(surname);
			user.setEmail(email);
			user.setPhone_number(phone_number);
			user.setImage(null);
			user.setPassword(new_password);
			usersRep.updateUser(user);
			System.out.println("User " + user.getLogin() + " was updated");
			return new ServerAnswer(ServerAnswer.OK);
		}
		else
		{
			System.out.println("User " + login + " couldn`t be updated because of wrong password");
			return new ServerAnswer(ServerAnswer.PASSWORD);
		}
	}
}
