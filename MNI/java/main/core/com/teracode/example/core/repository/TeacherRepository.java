package com.teracode.example.core.repository;

import com.teracode.example.core.common.AbstractReporsitory;
import com.teracode.example.core.domain.Teacher;

public class TeacherRepository extends AbstractReporsitory<Teacher> implements ITeacherRepository {

	public TeacherRepository()
	{
		Teacher teacher = new Teacher("teacher1", "teacher1");
		this.objects.put(teacher.getId(), teacher);

		teacher = new Teacher("teacher2", "teacher2");
		this.objects.put(teacher.getId(), teacher);

	}
	
}
