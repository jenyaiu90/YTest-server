package ru.jenyaiu90.ytest.entity;

public class ServerAnswer
{
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
