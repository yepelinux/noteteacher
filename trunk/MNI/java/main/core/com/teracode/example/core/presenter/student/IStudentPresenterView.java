package com.teracode.example.core.presenter.student;

public interface IStudentPresenterView {

	Integer getSelectedStudent();
	
	public void setSelectedStudent(Integer idStudent);
	
	public String getFirstName();

	public String getLastName();
	
	public void setFirstName(String firstName);

	public void setLastName(String lastName);

}
