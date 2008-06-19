package com.teracode.example.web.teacher;

import wicket.markup.html.form.Form;
import wicket.markup.html.form.RequiredTextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.PropertyModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.presenter.teacher.ITeacherPresenter;
import com.teracode.example.core.presenter.teacher.ITeacherPresenterView;
import com.teracode.example.web.BasePage;

public class TeacherPage extends BasePage implements ITeacherPresenterView {

	protected Integer teacherId;
	protected String firstName;
	protected String lastName;
	
	@SpringBean(name="teacherPresenter")
	protected ITeacherPresenter presenter;
	
	
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

	public Integer getTeacherId() {
		return teacherId;
	}

	public TeacherPage()
	{
		this(null);
	}
	
	public TeacherPage(Integer id)
	{
		this.teacherId = id;
		presenter.setView(this);
		presenter.init();
		
		add(new FeedbackPanel("feedback"));
		add(new TeacherPageForm("form"));
	}

	@SuppressWarnings("unchecked")
	class TeacherPageForm extends Form
	{
		public TeacherPageForm(String id) {
			super(id);
			add(new RequiredTextField("firstName", new PropertyModel(TeacherPage.this, "firstName")));
			add(new RequiredTextField("lastName", new PropertyModel(TeacherPage.this, "lastName")));
					
		}
		
		@Override
		protected void onSubmit() {
			presenter.save();
			setResponsePage(TeacherList.class);
		}
	}
}
