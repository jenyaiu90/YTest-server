package ru.jenyaiu90.ytest.entity;

//Результат прохождения теста
public class Result
{
	protected int id;
	protected int user;
	protected int test;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getUser()
	{
		return user;
	}
	public void setUser(int user)
	{
		this.user = user;
	}
	public int getTest()
	{
		return test;
	}
	public void setTest(int test)
	{
		this.test = test;
	}
}
