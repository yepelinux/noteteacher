package com.teracode.example.core.presenter.student;

import java.util.List;

import com.teracode.example.core.presenter.student.IStudentListPresenterView;

public interface IStudentListPresenter {
	
	public void setView(IStudentListPresenterView view);
	
	public void delete();
	
	public List getAllStudent();

	public int countAllStudent();

}
