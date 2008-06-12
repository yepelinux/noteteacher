package com.teracode.example.core.domain;

import junit.framework.TestCase;

public class StudentTest extends TestCase {

	
	public void testDelete()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		Student student = new Student("firstName", "lastName");

		student.delete();
		
		assertFalse(student.getShifts().contains(shift));
		assertFalse(shift.getStudents().contains(student));
	}
	
}
