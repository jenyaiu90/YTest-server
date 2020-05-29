package ru.jenyaiu90.ytest.entity;

public class ServerAnswer
{
	public static final String OK = "OK";
	public static final String PASSWORD = "Password";
	public static final String NO_USER = "No_user";
	public static final String NO_GROUP = "No_group";
	public static final String NO_TEST = "No_test";
	public static final String NO_ACCESS = "No_access";
	public static final String NO_TASK = "No_task";
	public static final String NO_AUTHOR = "No_author";
	public static final String USER_ALREADY_EXISTS = "User_already_exists";

	protected String answer;

	public ServerAnswer()
	{
		answer = "";
	}
	public ServerAnswer(String str)
	{
		answer = str;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
}
