package com.teracode.example.web;

import com.teracode.example.web.course.CourseList;
import com.teracode.example.web.student.StudentList;
import com.teracode.example.web.teacher.TeacherList;

import pages.homePage.HomePage;
import wicket.markup.html.link.PageLink;

public abstract class BasePage extends ExamplePage {

	public BasePage()
	{
//		add(new PageLink("courses", CourseList.class));
//		add(new PageLink("students", StudentList.class));
//		add(new PageLink("teachers", TeacherList.class));
		add(new PageLink("homePage", HomePage.class));

	}
	
}
