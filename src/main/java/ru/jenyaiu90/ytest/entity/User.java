package ru.jenyaiu90.ytest.entity;

public class User
{
	protected int id;
	protected String login;
	protected String name;
	protected String surname;
	protected String email;
	protected String phone_number;
	protected String image;
	protected boolean isTeacher;
	protected String password;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getLogin()
	{
		return login;
	}
	public void setLogin(String login)
	{
		this.login = login;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getSurname()
	{
		return surname;
	}
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPhone_number()
	{
		return phone_number;
	}
	public void setPhone_number(String phone_number)
	{
		this.phone_number = phone_number;
	}
	public String getImage()
	{
		return image;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	public boolean getIsTeacher()
	{
		return isTeacher;
	}
	public void setTeacher(boolean teacher)
	{
		isTeacher = teacher;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
}