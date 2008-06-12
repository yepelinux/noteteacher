package com.teracode.example.core.presenter.course;

import java.util.List;

import com.teracode.example.core.repository.ICourseRepository;
import com.teracode.example.core.domain.Course;

public class CourseListPresenter implements ICourseListPresenter{

	private ICourseListPresenterView view;
	
	private ICourseRepository courseRepository;
	
	
	public void init() {
		// do nothing
		
	}

	public void delete() {
		Course course = this.courseRepository.get(view.getSelectedCourse());
		course.delete();
		this.courseRepository.remove(course);
		view.setSelectedCourse(null);
				
	}
	
	public int countAllCourses() {
		return this.courseRepository.getAll().size();
	}

	public List getAllCourses() {
		return this.courseRepository.getAll();
	}

	public ICourseRepository getCourseRepository() {
		return courseRepository;
	}

	public void setCourseRepository(ICourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public ICourseListPresenterView getView() {
		return view;
	}

	public void setView(ICourseListPresenterView view) {
		this.view = view;
	}
	
}
