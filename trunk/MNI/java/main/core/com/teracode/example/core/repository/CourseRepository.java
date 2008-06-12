package com.teracode.example.core.repository;

import com.teracode.example.core.common.AbstractReporsitory;
import com.teracode.example.core.common.ObjectAlreadyExistsException;
import com.teracode.example.core.domain.Course;

public class CourseRepository extends AbstractReporsitory<Course> implements ICourseRepository {

	@Override
	public void add(Course cource) {
		// TODO Auto-generated method stub
		boolean exists = false;
		for(Course c : this.objects.values())
			exists |= cource.getName().equals(c.getName());
		
		if(exists)
			throw new ObjectAlreadyExistsException();
		
		super.add(cource);
	}
	
}
