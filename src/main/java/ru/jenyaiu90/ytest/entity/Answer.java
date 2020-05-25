package ru.jenyaiu90.ytest.entity;

public class Answer
{
	protected int id;
	protected int result;
	protected int task;
	protected String answer;
	protected String imageAnswer;
	protected boolean isChecked;
	protected int points;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getResult()
	{
		return result;
	}
	public void setResult(int result)
	{
		this.result = result;
	}
	public int getTask()
	{
		return task;
	}
	public void setTask(int task)
	{
		this.task = task;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
	public String getImageAnswer()
	{
		return imageAnswer;
	}
	public void setImageAnswer(String image)
	{
		imageAnswer = image;
	}
	public boolean getIsChecked()
	{
		return isChecked;
	}
	public void setIsChecked(boolean checked)
	{
		isChecked = checked;
	}
	public int getPoints()
	{
		return points;
	}
	public void setPoints(int points)
	{
		this.points = points;
	}
}
