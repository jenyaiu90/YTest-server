package ru.jenyaiu90.ytest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jenyaiu90.ytest.entity.Task;
import ru.jenyaiu90.ytest.entity.Test;
import ru.jenyaiu90.ytest.repositories.TasksRepository;
import ru.jenyaiu90.ytest.repositories.TestsRepository;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TasksController
{
	@Autowired
	TasksRepository tasksRep;
	@Autowired
	TestsRepository testsRep;

	//Получить все задания теста
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public List<Task> getTasksOfTest(@RequestParam("test_id") int test_id)
	{
		List<Test> test = testsRep.getTest(test_id);
		if (!test.isEmpty()) //Проверка теста на существование
		{
			List<Task> task = tasksRep.getTasksOfTest(test.get(0));
			return task;
		}
		else
		{
			return null;
		}
	}
}
