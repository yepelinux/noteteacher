package com.teracode.example.core.presenter.course;

import java.util.Map;

import com.teracode.example.core.presenter.course.ICoursePresenterView;

public interface ICoursePresenter {
	
	public void save();
	
	public void init();
	
	public void setView(ICoursePresenterView view);
	
	public Map<Integer, String> getTeachers();
	
	public void setTeachers(Map<Integer, String> teachers);

}
