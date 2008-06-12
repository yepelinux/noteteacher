package com.teracode.example.core.presenter.shift;

public interface IShiftPresenterView {
	
	public String getName();

	public void setName(String name);

	public Integer getVacancy();

	public void setVacancy(Integer vacancy);

	public Integer getShiftId();

	public void setShiftId(Integer shiftId);
	
	public Integer getCourseId();

	public String getCourseName();
	
	public void setCourseName(String courseName);

	public String getDescription();

	public void setDescription(String description);

}
