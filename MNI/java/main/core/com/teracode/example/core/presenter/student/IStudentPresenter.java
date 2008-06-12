package com.teracode.example.core.presenter.student;

import java.util.List;

import com.teracode.example.core.domain.Student;

public interface IStudentPresenter {
	
	public void save();
	
	public void init();
	
	public void setView(IStudentPresenterView view);
	
	public void addStudent(Student student);
	
	public List getAllStudent();

	public int countAllStudent();

}
