package ru.jenyaiu90.ytest.entity;

//Задание теста
public class Task
{
	//Тип задания
	public enum TaskType
	{
		ONE,	//Один из нескольких
		MANY,	//Несколько из нескольких
		SHORT,	//Краткий ответ
		LONG	//Развёрнутый ответ
	}

	protected int id;
	protected TaskType type;
	protected int num;
	protected String image;
	protected String text;
	protected int cost;
	protected String[] choice; //Варианты ответа
	protected String[] answer;
	protected int test;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public TaskType getType()
	{
		return type;
	}
	public void setType(TaskType type)
	{
		this.type = type;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public String getImage()
	{
		return image;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public int getCost()
	{
		return cost;
	}
	public void setCost(int cost)
	{
		this.cost = cost;
	}
	public String[] getChoice()
	{
		return choice;
	}
	public void setChoice(String[] choice)
	{
		this.choice = choice;
	}
	public String[] getAnswer()
	{
		return answer;
	}
	public void setAnswer(String[] answer)
	{
		this.answer = answer;
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