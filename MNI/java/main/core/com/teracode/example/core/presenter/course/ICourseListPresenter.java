package com.teracode.example.core.presenter.course;

import java.util.List;

public interface ICourseListPresenter {
	
	public void delete();
	
	public void init();

	public List getAllCourses();

	public int countAllCourses();
	

}
