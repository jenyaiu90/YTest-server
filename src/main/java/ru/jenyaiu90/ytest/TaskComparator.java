package ru.jenyaiu90.ytest;

import java.util.Comparator;

import ru.jenyaiu90.ytest.entity.Task;

public class TaskComparator implements Comparator<Task>
{
	//Сравнение заданий по их номерам для сортировки
	@Override
	public int compare(Task o1, Task o2)
	{
		return o1.getNum() - o2.getNum();
	}
}
