package ru.jenyaiu90.ytest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.jenyaiu90.ytest.entity.Group;
import ru.jenyaiu90.ytest.entity.Test;
import ru.jenyaiu90.ytest.entity.User;
import ru.jenyaiu90.ytest.mappers.GroupsMapper;

import java.util.List;

@Component
public class GroupsRepository
{
	@Autowired
	protected JdbcTemplate jdbc;

	public int createGroup(Group group)
	{
		return jdbc.update("INSERT INTO \"GROUPS\" (\"NAME\", \"ADMIN\") VALUES (?, ?)",
				group.getName(), group.getAdmin());
	}

	public List<Group> getGroup(int id)
	{
		return jdbc.query("SELECT * FROM \"GROUPS\" WHERE \"ID\" = ?",
				new GroupsMapper(),
				id);
	}

	public int joinGroup(User user, Group group)
	{
		return jdbc.update("INSERT INTO \"GROUPS_USERS\" (\"GROUP\", \"USER\") VALUES (?, ?)",
				group.getId(), user.getId());
	}

	public List<Group> getGroupsWith(User user)
	{
		return jdbc.query("SELECT * FROM \"GROUPS\" WHERE \"ID\" IN (SELECT \"GROUP\" FROM \"GROUPS_USERS\" WHERE \"USER\" = ?)",
				new GroupsMapper(),
				user.getId());
	}

	public List<Group> getGroupsOf(User user)
	{
		return jdbc.query("SELECT * FROM \"GROUPS\" WHERE \"ADMIN\" = ?",
				new GroupsMapper(),
				user.getId());
	}

	public int setTest(Test test, Group group)
	{
		return jdbc.update("INSERT INTO \"SETS\" (\"GROUP\", \"TEST\") VALUES (?, ?)",
				group.getId(), test.getId());
	}
}
