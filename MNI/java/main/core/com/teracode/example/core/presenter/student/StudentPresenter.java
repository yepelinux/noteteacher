package com.teracode.example.core.presenter.student;

import java.util.List;

import com.teracode.example.core.domain.Student;
import com.teracode.example.core.repository.IStudentRepository;

public class StudentPresenter implements IStudentPresenter{
	
	private IStudentPresenterView view;

	private IStudentRepository studentRepository;
	
	
	
	public void init() {
		if(view.getSelectedStudent() != null)
		{
			Student student = studentRepository.get(view.getSelectedStudent());
			 
			view.setFirstName(student.getFirstName());
			view.setLastName(student.getLastName());
		}
		else
		{
			view.setFirstName(null);
			view.setLastName(null);
		}
		
	}

	public void save(){
		
		if(view.getSelectedStudent()==null)
		{
			//create student
			
			Student student = new Student(view.getFirstName(), view.getLastName());
			this.studentRepository.add(student);
		}
		else
		{
			//modify student
			
			Student student = (Student)this.studentRepository.get(view.getSelectedStudent()); 
			student.modify(view.getFirstName(), view.getLastName());
		}
		
		view.setSelectedStudent(null);
	
	};

	public List getAllStudent() {
		return this.studentRepository.getAll();
	}

	public int countAllStudent() {
		return this.studentRepository.getAll().size();
	}

	public void setView(IStudentPresenterView view) {
		this.view = view;
	}
	
	public void setStudentRepository(IStudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
	
	public void addStudent(Student student){
		this.studentRepository.add(student);
		
	}
}
