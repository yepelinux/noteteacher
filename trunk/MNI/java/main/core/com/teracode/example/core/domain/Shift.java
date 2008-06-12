package com.teracode.example.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.teracode.example.core.common.Entity;
import com.teracode.example.core.common.ObjectAlreadyExistsException;

@SuppressWarnings("serial")
public class Shift extends Entity {

	private String name;
	private String description;
	private int vacancy;
	private List<Student> students;
	private Course course;
	
	public Course getCourse()
	{
		return this.course;
	}
	
	public Shift(String name, String description, int vacancy, Course course)
	{
		this.name = name;
		this.description = description;
		this.vacancy = vacancy;
		this.students = new ArrayList<Student>();
		
		boolean exists = false;
		for(Shift s : course.getShifts())
			exists |= this.getName().equals(s.getName());
		
		if(exists)
			throw new ObjectAlreadyExistsException();

		course.getShifts().add(this);
		this.course = course;
	}
	
	public void modify(String description)
	{
		this.description = description;
	}

	public boolean hasVacancy()
	{
		return vacancy > this.students.size();
	}
	
	public void inscribe(Student student)
	{
		if(course.isInscribed(student))
			throw new StudentAlreadyIscribedException();
			
		if(!hasVacancy())
			throw new NoVacancyException();
			
		students.add(student);
		student.getShifts().add(this);
	}
	
	public void uninscribe(Student student) {
		students.remove(student);
		student.getShifts().remove(this);
	}
	
	public boolean isInscribed(Student student)
	{
		return this.students.contains(student);
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public int getVacancy() {
		return vacancy;
	}

	public List<Student> getStudents()
	{
		return new ArrayList<Student>(this.students);
	}

	public void delete() 
	{
		for(Student student : new ArrayList<Student>(this.students))
			this.uninscribe(student);
		
		this.students.clear();
		this.course.getShifts().remove(this);
		this.course = null;
	}
}
