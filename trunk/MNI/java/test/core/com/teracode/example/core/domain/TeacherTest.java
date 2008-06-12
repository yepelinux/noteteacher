package com.teracode.example.core.domain;

import junit.framework.TestCase;

public class TeacherTest extends TestCase {


	public void testDeletee()
	{
		Teacher teacher = new Teacher("firstName", "lastName");

		teacher.delete();
		
	}
	
	public void testDeleteWithCouse()
	{
		Teacher teacher = new Teacher("firstName", "lastName");
		Course course = new Course("name", "description", teacher);

		try
		{
			teacher.delete();
			fail();
		}
		catch(TeacherInCourseException e)
		{
			
		}
	}
	
}
