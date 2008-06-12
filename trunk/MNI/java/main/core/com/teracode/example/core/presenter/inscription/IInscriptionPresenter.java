package com.teracode.example.core.presenter.inscription;

import java.util.List;
import java.util.Map;

public interface IInscriptionPresenter {

	public void setView(IInscriptionPresenterView view);
	
	public void init();
	
	public List getAllShift();

	public int countAllShift();
	
	public void selectCourse();
	
	public void inscribe();
	
	public void uninscribe();
	
	public Map<Integer, String> getCourses();

	public void setCourses(Map<Integer, String> courses);

	public Map<Integer, String> getShifts();

	public void setShifts(Map<Integer, String> shifts);

}
