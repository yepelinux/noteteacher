package com.teracode.example.core.presenter.teacher;

import java.util.List;

import com.teracode.example.core.domain.Teacher;
import com.teracode.example.core.repository.ITeacherRepository;

public class TeacherListPresenter implements ITeacherListPresenter {

	private ITeacherListPresenterView view;

	private ITeacherRepository teacherRepository;

	
	public void setView(ITeacherListPresenterView view)
	{
		this.view = view; 
	}
	
	public void delete()
	{
		Teacher teacher = this.teacherRepository.get(view.getSelectedTeacher());
		teacher.delete();
		this.teacherRepository.remove(teacher);
	}

	public List getAllTeachers()
	{
		return teacherRepository.getAll();
	}

	public int countAllTeachers()
	{
		return teacherRepository.getAll().size();
	}

	public void setTeacherRepository(ITeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}

}
