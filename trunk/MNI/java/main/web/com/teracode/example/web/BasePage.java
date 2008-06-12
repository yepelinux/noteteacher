package com.teracode.example.web;

import wicket.markup.html.link.PageLink;

import com.teracode.example.web.course.CourseList;
import com.teracode.example.web.student.StudentList;
import com.teracode.example.web.teacher.TeacherList;

public abstract class BasePage extends ExamplePage {

	public BasePage()
	{
		add(new PageLink("courses", CourseList.class));
		add(new PageLink("students", StudentList.class));
		add(new PageLink("teachers", TeacherList.class));

	}
	
}
