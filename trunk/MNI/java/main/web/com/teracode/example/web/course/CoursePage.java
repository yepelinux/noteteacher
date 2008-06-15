package com.teracode.example.web.course;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import wicket.Component;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.RequiredTextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.PropertyModel;
import wicket.spring.injection.annot.SpringBean;

//import buscadores.BuscarPorNombre;

import com.aetrion.flickr.FlickrException;
import com.teracode.example.core.common.ObjectAlreadyExistsException;
import com.teracode.example.core.domain.Course;
import com.teracode.example.core.domain.Teacher;
import com.teracode.example.core.presenter.course.ICoursePresenter;
import com.teracode.example.core.presenter.course.ICoursePresenterView;
import com.teracode.example.core.presenter.student.IStudentPresenter;
import com.teracode.example.core.repository.ICourseRepository;
import com.teracode.example.core.repository.ITeacherRepository;
import com.teracode.example.web.BasePage;

@SuppressWarnings("serial")
public class CoursePage extends BasePage implements ICoursePresenterView{
	
	
	@SpringBean(name="coursePresenter")
	private ICoursePresenter coursePresenter;
	
	protected Integer courseId;
	protected String name;
	protected String description;
	protected Integer selectedTeacher;
//	private BuscarPorNombre buscadorPorNombre;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer id) {
		this.courseId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSelectedTeacher() {
		return selectedTeacher;
	}

	public void setSelectedTeacher(Integer selectedTeacher) {
		this.selectedTeacher = selectedTeacher;
	}

	public Map<Integer, String> getTeachers() {
		return this.coursePresenter.getTeachers();
		
	}

	public void setTeachers(Map<Integer, String> teachers) {
		this.coursePresenter.setTeachers(teachers);
	}

	public CoursePage()
	{
		this(null);
	}
	
	public CoursePage(Integer id)
	{
		
		this.courseId = id;  
		this.coursePresenter.setView(this);
		this.coursePresenter.init();
		
		
		
		add(new FeedbackPanel("feedback"));
		add(new CoursePageForm("form"));
	}

	class CoursePageForm extends Form
	{
		@SuppressWarnings("unchecked")
		public CoursePageForm(String id) {
			super(id);
			add(new RequiredTextField("name", new PropertyModel(CoursePage.this, "name"))
			{
				@Override
				public boolean isEnabled() {
					return courseId==null;
				}
			});
			
			add(new RequiredTextField("description", new PropertyModel(CoursePage.this, "description")));

			AbstractReadOnlyModel teachersModel = new AbstractReadOnlyModel()
			{
				@Override
				public Object getObject(Component component) {
					
					return new ArrayList<Integer>(CoursePage.this.coursePresenter.getTeachers().keySet());
				}
			};
			
			IChoiceRenderer teachersChoiseRenderer = new IChoiceRenderer()
			{
				public Object getDisplayValue(Object object) {
					return CoursePage.this.coursePresenter.getTeachers().get(object);
				}

				public String getIdValue(Object object, int index) {
					return object.toString();
				}
			};
			
			DropDownChoice ddc = new DropDownChoice("teacher", new PropertyModel(CoursePage.this, "selectedTeacher"), teachersModel, teachersChoiseRenderer);
			ddc.setRequired(true);
			add(ddc);
		}
		
		@Override
		protected void onSubmit() {
			
			try
			{
				CoursePage.this.coursePresenter.save();
				setResponsePage(CourseList.class);
			}
			catch(ObjectAlreadyExistsException e)
			{
				info("Course already exists");
			}
		}
	}
	}
