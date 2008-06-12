package com.teracode.example.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.teracode.example.core.common.Entity;
import com.teracode.example.core.common.ObjectAlreadyExistsException;
import com.teracode.example.core.common.ObjectNotFoundException;

@SuppressWarnings("serial")
public class Course extends Entity {

	private String name;
	private String description;
	private Teacher teacher;
	private List<Shift> shifts;
	
	public Course(String name, String description, Teacher teacher)
	{
		this.name = name;
		this.description = description;
		this.teacher = teacher;
		this.teacher.getCourses().add(this);
		
		this.shifts = new ArrayList<Shift>();
	}
	
	public void modify(String description, Teacher teacher)
	{
		this.description = description;
		this.teacher.getCourses().remove(this);
		this.teacher = teacher;
		this.teacher.getCourses().add(this);
	}
	
	public Shift getShift(int id)
	{
		Shift ret = null;
		for (Shift shift : shifts)
		{
			if(shift.getId()==id)
				ret = shift;
		}
		if(ret==null)
			throw new ObjectNotFoundException();
		
		return ret;
	}
	
	public boolean isInscribed(Student student)
	{
		boolean isInscribed = false;
		for(Shift shift : this.shifts)
			isInscribed |= shift.isInscribed(student);
		
		return isInscribed;
		
	}
	
	public void delete()
	{
		for(Shift shift : new ArrayList<Shift>(this.shifts))
			shift.delete();
	
		this.teacher.getCourses().remove(this);
		this.teacher = null;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}
	
	public List<Shift> getShifts()
	{
		return this.shifts;
	}
}
