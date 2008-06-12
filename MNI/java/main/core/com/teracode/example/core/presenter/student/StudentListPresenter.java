package com.teracode.example.core.presenter.student;

import java.util.List;

import com.teracode.example.core.domain.Student;
import com.teracode.example.core.repository.IStudentRepository;

public class StudentListPresenter implements IStudentListPresenter{

	private IStudentListPresenterView view;

	private IStudentRepository studentRepository;
	
	public int countAllStudent() {
		return this.studentRepository.getAll().size();
	}

	public void delete() {
		
		Student student = this.studentRepository.get(view.getSelectedStudent());
		student.delete();
		this.studentRepository.remove(student);
		
	}

	public List getAllStudent() {
		return this.studentRepository.getAll();
	}

	public void setView(IStudentListPresenterView view) {

		this.view = view;
	}

	public void setStudentRepository(IStudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
}
