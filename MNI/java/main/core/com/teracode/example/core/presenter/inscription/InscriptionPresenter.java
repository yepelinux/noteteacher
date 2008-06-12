package com.teracode.example.core.presenter.inscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teracode.example.core.presenter.course.ICoursePresenterView;
import com.teracode.example.core.repository.ICourseRepository;
import com.teracode.example.core.repository.IStudentRepository;
import com.teracode.example.core.domain.*;

public class InscriptionPresenter implements IInscriptionPresenter{

	private IInscriptionPresenterView view;
	
	private ICourseRepository courseRepository;
	
	private IStudentRepository studentRepository;
	
	protected Map<Integer, String> courses;
	protected Map<Integer, String> shifts;
	
	public void init() {
		view.setSelectedCourse(null);
		view.setSelectedShift(null);
		this.courses = new HashMap<Integer, String>();
		this.shifts = new HashMap<Integer, String>();
		
		Student student = studentRepository.get(view.getStudentId());
		view.setStudentName(student.getFirstName());
		
		
		for (Course course : this.courseRepository.getAll())
		{
			if(!course.isInscribed(student))
				this.courses.put(course.getId(), course.getName());
			
		}
		
	}

	public void inscribe() {
		Course course = this.courseRepository.get(view.getSelectedCourse());
		Shift shift = course.getShift(view.getSelectedShift());
		Student student = this.studentRepository.get(view.getStudentId());
		shift.inscribe(student);
		
	}

	public void selectCourse() {
		if(view.getSelectedCourse()!=null)
		{
			shifts = new HashMap<Integer, String>();
			Course course = courseRepository.get(view.getSelectedCourse());
			for(Shift shift : course.getShifts())
			{
				if(shift.hasVacancy())
					shifts.put(shift.getId(), shift.getDescription());
			}
			
		}
		
	}

	public void uninscribe() {
		Student student = studentRepository.get(view.getStudentId());
		Course course = courseRepository.get(view.getSelectedCourse());
		Shift shift = course.getShift(view.getSelectedShift());
		shift.uninscribe(student);
		
	}
	
	public int countAllShift() {
		return studentRepository.get(view.getStudentId()).getShifts().size();
		
	}

	public List getAllShift() {
		return studentRepository.get(view.getStudentId()).getShifts();
	}

	public ICourseRepository getCourseRepository() {
		return courseRepository;
	}

	public void setCourseRepository(ICourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public IStudentRepository getStudentRepository() {
		return studentRepository;
	}

	public void setStudentRepository(IStudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public void setView(IInscriptionPresenterView view) {
		this.view = view;
		
	}

	public Map<Integer, String> getCourses() {
		return courses;
	}

	public void setCourses(Map<Integer, String> courses) {
		this.courses = courses;
	}

	public Map<Integer, String> getShifts() {
		return shifts;
	}

	public void setShifts(Map<Integer, String> shifts) {
		this.shifts = shifts;
	}
	
}
