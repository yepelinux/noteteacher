package com.teracode.example.core.presenter.teacher;

import com.teracode.example.core.domain.Teacher;
import com.teracode.example.core.repository.ITeacherRepository;

public class TeacherPresenter implements ITeacherPresenter {

	
	private ITeacherPresenterView view;

	private ITeacherRepository teacherRepository;

	
	public void setView(ITeacherPresenterView view)
	{
		this.view = view;
	}
	
	public void init()
	{
		if(view.getTeacherId()!=null)
		{
			Teacher teacher = teacherRepository.get(view.getTeacherId());
			view.setFirstName(teacher.getFirstName());
			view.setLastName(teacher.getLastName());
		}
		else
		{
			view.setFirstName(null);
			view.setLastName(null);
		}
	}

	public void save()
	{
		if(view.getTeacherId()==null)
		{
			Teacher teacher = new Teacher(view.getFirstName(), view.getLastName());
			this.teacherRepository.add(teacher);
		}
		else
		{
			Teacher teacher = this.teacherRepository.get(view.getTeacherId()); 
			teacher.modify(view.getFirstName(), view.getLastName());
		}
	}

	public void setTeacherRepository(ITeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}
}
