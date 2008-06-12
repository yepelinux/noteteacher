package com.teracode.example.core.domain;

import junit.framework.TestCase;

public class CourseTest extends TestCase {

	public void testCreate()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		
		assertTrue(teacher.getCourses().contains(course));
		assertSame(course.getTeacher(), teacher);
	}

	public void testModify()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Teacher otherTeacher = new Teacher("otherFirstName", "otherLastName");
		
		Course course = new Course("name", "description", teacher);

		course.modify(course.getDescription(), otherTeacher);
		
		assertTrue(otherTeacher.getCourses().contains(course));
		assertSame(course.getTeacher(), otherTeacher);
		
		assertFalse(teacher.getCourses().contains(course));
		assertNotSame(course.getTeacher(), teacher);
		
	}

	public void testModifyWithSameTeacher()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		
		Course course = new Course("name", "description", teacher);

		course.modify("otherdescription", teacher);
		
		assertTrue(teacher.getCourses().contains(course));
		assertSame(course.getTeacher(), teacher);
	}

	
	public void testDelete()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		
		course.delete();
		assertFalse(teacher.getCourses().contains(course));
		assertNull(course.getTeacher());
		
		assertTrue(course.getShifts().isEmpty());
		assertNull(shift.getCourse());
	}
}