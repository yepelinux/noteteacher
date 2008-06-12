package com.teracode.example.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.teracode.example.core.common.Entity;

@SuppressWarnings("serial")
public class Student extends Entity {

	private String firstName;
	private String lastName;
	private List<Shift> shifts;
	
	public Student(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.shifts = new ArrayList<Shift>();
	}

	public void modify(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void delete()
	{
		for(Shift shift : new ArrayList<Shift>(this.shifts))
			shift.uninscribe(this);
		
		this.shifts.clear();
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public List<Shift> getShifts()
	{
		return shifts;
	}
}
