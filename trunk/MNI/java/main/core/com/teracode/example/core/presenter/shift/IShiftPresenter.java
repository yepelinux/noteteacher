package com.teracode.example.core.presenter.shift;

import java.util.List;

public interface IShiftPresenter {
	
	public void setView(IShiftPresenterView view);
	
	public void init();
	
	public void delete();
	
	public void save();
	
	public List getAllShift();

	public int countAllShift();

}
