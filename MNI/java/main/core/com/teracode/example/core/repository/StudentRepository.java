package com.teracode.example.core.repository;

import com.teracode.example.core.common.AbstractReporsitory;
import com.teracode.example.core.domain.Student;

public class StudentRepository extends AbstractReporsitory<Student> implements IStudentRepository {

	public StudentRepository()
	{
		Student student = new Student("student1", "student1");
		this.objects.put(student.getId(), student);
		student = new Student("student2", "student2");
		this.objects.put(student.getId(), student);
	}
}
