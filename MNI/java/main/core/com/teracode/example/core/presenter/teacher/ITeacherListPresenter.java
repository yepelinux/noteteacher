package com.teracode.example.core.presenter.teacher;

import java.util.List;

public interface ITeacherListPresenter {

	public void setView(ITeacherListPresenterView view);
	
	public void delete();
	
	public List getAllTeachers();

	public int countAllTeachers();

}
