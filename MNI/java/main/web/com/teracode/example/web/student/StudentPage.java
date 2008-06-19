package com.teracode.example.web.student;

import wicket.markup.html.form.Form;
import wicket.markup.html.form.RequiredTextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.PropertyModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.domain.StudentInCurseException;
import com.teracode.example.core.presenter.student.IStudentPresenter;
import com.teracode.example.core.presenter.student.IStudentPresenterView;
import com.teracode.example.web.BasePage;

@SuppressWarnings("serial")
public class StudentPage extends BasePage implements IStudentPresenterView{
	
	/*	
 	* @SpringBean
 	* protected IStudentRepository studentRepository;
	*/
	
	@SpringBean(name="studentPresenter")
	private IStudentPresenter studentPresenter;
	
	protected Integer studentId;
	protected String firstName;
	protected String lastName;
	
	
	public Integer getSelectedStudent() {
		return studentId;
	}

	public void setSelectedStudent(Integer idStudent) {
		this.studentId=idStudent;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public StudentPage()
	{
		this(null);
	}
	
	public StudentPage(Integer id)
	{
		this.studentId = id;
		this.studentPresenter.setView(this);
		this.studentPresenter.init();

		add(new FeedbackPanel("feedback"));
		add(new StudentPageForm("form"));
	}

	@SuppressWarnings("unchecked")
	class StudentPageForm extends Form
	{
		public StudentPageForm(String id) {
			super(id);
			add(new RequiredTextField("firstName", new PropertyModel(StudentPage.this, "firstName")));
			add(new RequiredTextField("lastName", new PropertyModel(StudentPage.this, "lastName")));
					
		}
		
		@Override
		protected void onSubmit() {
			
			try{
				studentPresenter.save();
			}
			catch (StudentInCurseException e) {
				info("student is already in curse");
				// TODO: handle exception
			}
			setResponsePage(StudentList.class);
		}
	}
}
