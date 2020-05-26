package ru.jenyaiu90.ytest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.jenyaiu90.ytest.entity.Group;
import ru.jenyaiu90.ytest.entity.User;
import ru.jenyaiu90.ytest.repositories.GroupsRepository;
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

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Group createGroup(@RequestBody Group group, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
		{
			group.setAdmin(user.get(0).getId());
			if (password.equals(user.get(0).getPassword()))
			{
				groupsRep.createGroup(group);
				System.out.println("User " + login + " has successfully created a new group with name " + group.getName());
				return group;
			}
			else
			{
				System.out.println("User " + login + " couldn`t create a group because of wrong password");
			}
		}
		else
		{
			System.out.println("Couldn`t create group because user " + login + " wasn`t found");
		}
		return null;
	}

	@RequestMapping(value = "/join", method = RequestMethod.PUT)
	public Group joinGroup(@RequestParam("group_id") int group_id, @RequestParam("login") String login, @RequestParam("password") String password)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
		{
			if (user.get(0).getPassword().equals(password))
			{
				List<Group> group = groupsRep.getGroup(group_id);
				if (!group.isEmpty())
				{
					System.out.println("A user " + login + " has joined the group with id " + group_id);
					List<Group> groups = groupsRep.getGroupsWith(user.get(0));
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
						groupsRep.joinGroup(user.get(0), group.get(0));
					}
					return group.get(0);
				}
				else
				{
					System.out.println("User " + login + " couldn`t join the group with id " + group_id + " because that group wasn`t found");
				}
			}
			else
			{
				System.out.println("User " + login + " couldn`t join the group with id " + group_id + " because of wrong password");
			}
		}
		else
		{
			System.out.println("User " + login + " couldn`t join the group because that user wasn`t found");
		}
		return null;
	}

	@RequestMapping(value = "/get_groups_of", method = RequestMethod.GET)
	public List<Group> getGroupsOf(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
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

	@RequestMapping(value = "/get_groups_with", method = RequestMethod.GET)
	public List<Group> getGroupsWith(@RequestParam("login") String login)
	{
		List<User> user = usersRep.getUser(login);
		if (!user.isEmpty())
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
}
