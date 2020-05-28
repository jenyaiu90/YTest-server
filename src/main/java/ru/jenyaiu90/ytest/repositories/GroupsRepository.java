package ru.jenyaiu90.ytest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.jenyaiu90.ytest.entity.Group;
import ru.jenyaiu90.ytest.entity.Set;
import ru.jenyaiu90.ytest.entity.Test;
import ru.jenyaiu90.ytest.entity.User;
import ru.jenyaiu90.ytest.mappers.GroupsMapper;
import ru.jenyaiu90.ytest.mappers.SetsMapper;
import ru.jenyaiu90.ytest.mappers.TestsMapper;
import ru.jenyaiu90.ytest.mappers.UsersMapper;

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

	public List<User> getUsers(Group group)
	{
		return jdbc.query("SELECT * FROM \"USERS\" WHERE \"ID\" IN (SELECT \"USER\" FROM \"GROUPS_USERS\" WHERE \"GROUP\" = ?)",
				new UsersMapper(),
				group.getId());
	}

	public int setTest(Test test, Group group)
	{
		List<Set> set = jdbc.query("SELECT * FROM \"SETS\" WHERE \"TEST\" = ? AND \"GROUP\" = ?",
				new SetsMapper(),
				test.getId(), group.getId());
		if (set.isEmpty())
		{
			return jdbc.update("INSERT INTO \"SETS\" (\"GROUP\", \"TEST\") VALUES (?, ?)",
					group.getId(), test.getId());
		}
		else
		{
			return -1;
		}
	}

	public List<Test> getTestsForSet(Group group)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" IN (SELECT \"TEST\" FROM \"TESTS_USERS\" WHERE \"USER\" = ?) AND \"ID\" NOT IN (SELECT \"TEST\" FROM \"SETS\" WHERE \"GROUP\" = ?)",
				new TestsMapper(),
				group.getAdmin(), group.getId());
	}

	public List<Test> getTestsForGroup(Group group)
	{
		return jdbc.query("SELECT * FROM \"TESTS\" WHERE \"ID\" IN (SELECT \"TEST\" FROM \"SETS\" WHERE \"GROUP\" = ?)",
				new TestsMapper(),
				group.getId());
	}
}
