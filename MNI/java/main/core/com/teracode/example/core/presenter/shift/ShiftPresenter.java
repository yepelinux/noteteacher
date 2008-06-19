package com.teracode.example.core.presenter.shift;

import java.util.List;

import com.teracode.example.core.domain.Course;
import com.teracode.example.core.domain.Shift;
import com.teracode.example.core.repository.ICourseRepository;

public class ShiftPresenter implements IShiftPresenter{

	private IShiftPresenterView view;
	
	private ICourseRepository courseRepository;
	
	public void delete() {
		Shift shift = this.courseRepository.get(view.getCourseId()).getShift(view.getShiftId());
		shift.delete();
		view.setShiftId(null);
		
	}

	public void init() {
		
		view.setCourseName(courseRepository.get(view.getCourseId()).getName());
		
		if(view.getShiftId()!=null)
		{
			Shift shift = this.courseRepository.get(view.getCourseId()).getShift(view.getShiftId());
			view.setName(shift.getName());
			view.setDescription(shift.getDescription());
			view.setVacancy(shift.getVacancy());
		}
		else
		{
			view.setName(null);
			view.setDescription(null);
			view.setVacancy(null);
		}
		
	}

	public void save() {
		if(view.getShiftId()==null)
		{
			Course course = this.courseRepository.get(view.getCourseId());
			new Shift(view.getName(),view.getDescription(), view.getVacancy(), course);
		}
		else
		{
			Shift shift = this.courseRepository.get(view.getCourseId()).getShift(view.getShiftId());
			shift.modify(view.getDescription());
		}
		
		view.setShiftId(null);
	}
	
	

	public int countAllShift() {
		return courseRepository.get(view.getCourseId()).getShifts().size();
	}

	public List getAllShift() {
		return courseRepository.get(view.getCourseId()).getShifts();
	}

	public ICourseRepository getCourseRepository() {
		return courseRepository;
	}

	public void setCourseRepository(ICourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public IShiftPresenterView getView() {
		return view;
	}

	public void setView(IShiftPresenterView view) {
		this.view = view;
	}
	
	
	

}
