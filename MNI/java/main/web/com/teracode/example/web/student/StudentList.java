package com.teracode.example.web.student;

import java.util.Iterator;

import wicket.extensions.markup.html.repeater.data.DataView;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.common.Entity;
import com.teracode.example.core.domain.Student;
import com.teracode.example.core.domain.StudentInCurseException;
import com.teracode.example.core.presenter.student.IStudentListPresenter;
import com.teracode.example.core.presenter.student.IStudentListPresenterView;
import com.teracode.example.core.repository.IStudentRepository;
import com.teracode.example.web.BasePage;
import com.teracode.example.web.common.EntityDetachableModel;
import com.teracode.example.web.inscription.InscriptionPage;

@SuppressWarnings("serial")
public class StudentList extends BasePage implements IStudentListPresenterView{

	/*
	 * La vista no conoce el modelo (objetos de dominio y repositorios)
	 */
	
	/*@SpringBean
	protected IStudentRepository studentRepository;
	*/
	protected Integer selectedStudent;
	
	
	@SpringBean(name="studentListPresenter")
	private IStudentListPresenter studentListPresenter;
	
	
	public Integer getSelectedStudent()
	{
		return selectedStudent;
	}
	
	public StudentList()
	{
		
		studentListPresenter.setView(this);
		
		add(new PageLink("add", StudentPage.class));
		add(new DataView("table", new StudentDataProvider())
		{
			protected void populateItem(final Item item)
			{
				Student student = (Student)item.getModelObject();
				item.add(new Label("firstName", student.getFirstName()));
				item.add(new Label("lastName", student.getLastName()));

				item.add(new ActionPanel("action", item.getModel()));
			}
		});
		
	}

	class ActionPanel extends Panel
	{
		public ActionPanel(String id, IModel model)
		{
			super(id, model);
			add(new Link("select", model)
			{
				public void onClick()
				{
					selectedStudent = ((EntityDetachableModel)getModel()).getId();
					setResponsePage(new StudentPage(selectedStudent));
				}
			});
			
			add(new Link("inscribe", model)
			{
				public void onClick()
				{
					selectedStudent = ((EntityDetachableModel)getModel()).getId();
					setResponsePage(new InscriptionPage(selectedStudent));
				}
			});
			
			add(new Link("delete", model)
			{
				public void onClick()
				{
					selectedStudent = ((EntityDetachableModel)getModel()).getId();
					
					try{
					studentListPresenter.delete();
					}
					catch(StudentInCurseException e){
						
						info("Can not delete student");
						
					}
					
				}
			});
		}
		
	}
	
	
	class StudentDataProvider implements IDataProvider{
		
		public Iterator iterator(int from, int to) {
			return studentListPresenter.getAllStudent().iterator();
		}

		public IModel model(Object o) {
			return new EntityDetachableModel((Entity)o);
		}

		public int size() {
			return studentListPresenter.countAllStudent();
		}
	}
}