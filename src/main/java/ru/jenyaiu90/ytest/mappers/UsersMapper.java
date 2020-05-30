package ru.jenyaiu90.ytest.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.jenyaiu90.ytest.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersMapper implements RowMapper<User>
{
	public User mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		User user = new User();
		user.setId(rs.getInt("ID"));
		user.setLogin(rs.getString("LOGIN"));
		user.setName(rs.getString("NAME"));
		user.setSurname(rs.getString("SURNAME"));
		user.setEmail(rs.getString("EMAIL"));
		user.setPhone_number("+" + rs.getInt("PHONE_NUMBER"));
		user.setImage(rs.getString("IMAGE"));
		user.setTeacher(rs.getBoolean("IS_TEACHER"));
		user.setPassword(rs.getString("PASSWORD"));
		return user;
	}
}
