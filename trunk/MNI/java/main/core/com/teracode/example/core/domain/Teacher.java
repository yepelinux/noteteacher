package com.teracode.example.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.teracode.example.core.common.Entity;

@SuppressWarnings("serial")
public class Teacher extends Entity {

	private String firstName;
	private String lastName;
	private List<Course> courses;
	
	public Teacher(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.courses = new ArrayList<Course>();
	}
	
	public void modify(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void delete()
	{
		if(!this.courses.isEmpty())
			throw new TeacherInCourseException();
	}
	
	public List<Course> getCourses() {
		return this.courses;
	}
	
	public boolean teachesIn(Course course)
	{
		return this.courses.contains(course);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
