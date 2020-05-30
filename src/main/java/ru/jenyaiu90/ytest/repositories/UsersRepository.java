package ru.jenyaiu90.ytest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.jenyaiu90.ytest.entity.User;
import ru.jenyaiu90.ytest.mappers.UsersMapper;

import java.util.List;

@Component
public class UsersRepository
{
	@Autowired
	protected JdbcTemplate jdbc;

	//Создание пользователя
	public int createUser(User u)
	{
		return jdbc.update("INSERT INTO \"USERS\" (\"LOGIN\", \"NAME\", \"SURNAME\", \"PASSWORD\", \"PHONE_NUMBER\", \"IS_TEACHER\") VALUES (?, ?, ?, ?, ?, ?)",
				u.getLogin(), u.getName(), u.getSurname(), u.getPassword(), "+0", u.getIsTeacher());
	}

	//Проверка пользователя на существование
	public boolean existsUser(String login)
	{
		return jdbc.update("SELECT EXISTS (SELECT * FROM \"USERS\" WHERE \"LOGIN\" = ?)",
				login) != 0;
	}

	//Получить пользователя
	public List<User> getUser(String login)
	{
		return jdbc.query(
				"SELECT * FROM \"USERS\" WHERE \"LOGIN\" = ?",
				new UsersMapper(),
				login);
	}

	//Обновить пользователя
	public int updateUser(User user)
	{
		return jdbc.update("UPDATE \"USERS\" SET" +
				"\"NAME\" = ?, \"SURNAME\" = ?, \"EMAIL\" = ?, \"PHONE_NUMBER\" = ?, \"IMAGE\" = ?, \"PASSWORD\" = ?" +
				"WHERE \"LOGIN\" = ?",
				user.getName(), user.getSurname(), user.getEmail(), user.getPhone_number(), user.getImage(), user.getPassword(), user.getLogin());
	}
}
