package com.teracode.example.core.presenter.course;

public interface ICoursePresenterView {
	
	public Integer getCourseId();

	public void setCourseId(Integer id);

	public String getName();

	public void setName(String name);

	public Integer getSelectedTeacher();

	public void setSelectedTeacher(Integer selectedTeacher);
	
	public String getDescription();

	public void setDescription(String description);

}
