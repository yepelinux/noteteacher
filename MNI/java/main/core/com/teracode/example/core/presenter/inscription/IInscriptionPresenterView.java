package com.teracode.example.core.presenter.inscription;


public interface IInscriptionPresenterView {
	
	public Integer getStudentId();

	public void setStudentId(Integer studentId);

	public void setStudentName(String studentName);
	
	public String getStudentName();
	
	public Integer getSelectedCourse();

	public void setSelectedCourse(Integer selectedCourse);

	public Integer getSelectedShift();

	public void setSelectedShift(Integer selectedShift);
	

}
