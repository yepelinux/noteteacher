package com.teracode.example.core.presenter.course;

import java.util.HashMap;
import java.util.Map;

import com.teracode.example.core.domain.*;
import com.teracode.example.core.repository.ICourseRepository;
import com.teracode.example.core.repository.IStudentRepository;
import com.teracode.example.core.repository.ITeacherRepository;



public class CoursePresenter implements ICoursePresenter{

	private ICoursePresenterView view;
	
	private ICourseRepository courseRepository;
	
	private ITeacherRepository teacherRepository;
	
	protected Map<Integer, String> teachers;
	
	
	public void init() {

		this.teachers = new HashMap<Integer, String>();
		for(Teacher teacher : this.teacherRepository.getAll())
			this.teachers.put(teacher.getId(), teacher.getFirstName());

		if(view.getCourseId()!=null)
		{
			Course course = this.courseRepository.get(view.getCourseId());
			view.setName(course.getName());
			view.setDescription(course.getDescription());
			view.setSelectedTeacher(course.getTeacher().getId());
		}
		else
		{
			view.setName(null);
			view.setDescription(null);
			view.setSelectedTeacher(null);
		}
		
	}

	public void save() {
		
		if(view.getCourseId()==null)
		{
			Teacher teacher = this.teacherRepository.get(view.getSelectedTeacher());
			Course course = new Course(view.getName(), view.getDescription(), teacher);
			this.courseRepository.add(course);
		}
		else
		{
			Teacher teacher = this.teacherRepository.get(view.getSelectedTeacher());
			Course course = this.courseRepository.get(view.getCourseId());
			course.modify(view.getDescription(), teacher);
		}
		
	}

	public void setView(ICoursePresenterView view) {
		this.view = view;
		
	}

	public ICourseRepository getCourseRepository() {
		return courseRepository;
	}

	public void setCourseRepository(ICourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public ITeacherRepository getTeacherRepository() {
		return teacherRepository;
	}

	public void setTeacherRepository(ITeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}

	public Map<Integer, String> getTeachers() {
		return teachers;
	}
	
	public void setTeachers(Map<Integer, String> teachers) {
		this.teachers = teachers;
	}

	public ICoursePresenterView getView() {
		return view;
	}
	
	
	
	
	

}
