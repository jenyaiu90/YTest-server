package ru.jenyaiu90.ytest.entity;

//Группа пользователей
public class Group
{
	protected int id;
	protected String name;
	protected int admin;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getAdmin()
	{
		return admin;
	}
	public void setAdmin(int admin)
	{
		this.admin = admin;
	}
}
