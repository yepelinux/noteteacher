package com.teracode.example.core.domain;

import com.teracode.example.core.common.ObjectAlreadyExistsException;

import junit.framework.TestCase;

public class ShiftTest extends TestCase {

	public void testCreate()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);

		assertTrue(course.getShifts().contains(shift));
		assertSame(course, shift.getCourse());

		Shift otherShift = new Shift("otherName", "description", 1, course);

		assertTrue(course.getShifts().contains(shift));
		assertSame(course, otherShift.getCourse());
		assertEquals(2, course.getShifts().size());

	}

	public void testCreateWithSameName()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);

		try
		{
			new Shift("name", "description", 1, course);
			fail();
		}
		catch(ObjectAlreadyExistsException e)
		{
			
		}
		assertEquals(1, course.getShifts().size());
	}

	
	public void testModifiy()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);

		shift.modify("otherDescription");
		
		assertTrue(course.getShifts().contains(shift));
		assertSame(course, shift.getCourse());
	}

	public void testInscribe()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		Student student = new Student("firstName", "lastName");
		
		shift.inscribe(student);
		
		assertTrue(shift.getStudents().contains(student));
		assertTrue(student.getShifts().contains(shift));
	}

	public void testInscribeWithNoVacancy()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		Student student = new Student("firstName", "lastName");
		Student otherStudent = new Student("otherFirstName", "otherLastName");

		shift.inscribe(student);
		
		try
		{
			shift.inscribe(otherStudent);
			fail();
		}
		catch(NoVacancyException e)
		{
			
		}
		
		assertFalse(otherStudent.getShifts().contains(shift));
		assertFalse(shift.getStudents().contains(otherStudent));
		
	}

	public void testInscribeWithAlreadyInscribedStudent()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		Shift otherShift = new Shift("otherName", "otherDescription", 1, course);
		Student student = new Student("firstName", "lastName");

		shift.inscribe(student);
		
		try
		{
			otherShift.inscribe(student);
			fail();
		}
		catch(StudentAlreadyIscribedException e)
		{
			
		}

		assertFalse(student.getShifts().contains(otherShift));
		assertFalse(otherShift.getStudents().contains(student));

	}
	
	public void testUninscribe()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		Student student = new Student("firstName", "lastName");
		Student otherStudent = new Student("otherFirstName", "otherLastName");

		shift.inscribe(student);

		shift.uninscribe(student);

		assertFalse(student.getShifts().contains(shift));
		assertFalse(shift.getStudents().contains(student));
		
		shift.inscribe(otherStudent);
	}

	public void testDelete()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);
		Shift shift = new Shift("name", "description", 1, course);
		Student student = new Student("firstName", "lastName");
		
		shift.delete();
		
		assertFalse(student.getShifts().contains(shift));
		assertFalse(shift.getStudents().contains(student));

		assertFalse(course.getShifts().contains(shift));
		assertNull(shift.getCourse());
	}
}
