package com.teracode.example.web.teacher;

import java.util.Iterator;

import wicket.extensions.markup.html.repeater.data.DataView;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.common.Entity;
import com.teracode.example.core.domain.Teacher;
import com.teracode.example.core.domain.TeacherInCourseException;
import com.teracode.example.core.presenter.teacher.ITeacherListPresenter;
import com.teracode.example.core.presenter.teacher.ITeacherListPresenterView;
import com.teracode.example.web.BasePage;
import com.teracode.example.web.common.EntityDetachableModel;

public class TeacherList extends BasePage implements ITeacherListPresenterView {

	@SpringBean(name="teacherListPresenter")
	private ITeacherListPresenter presenter;
	private Integer selectedTeacher;
	
	public Integer getSelectedTeacher()
	{
		return selectedTeacher;
	}
	
	public TeacherList()
	{
		presenter.setView(this);
		
		add(new FeedbackPanel("feedback"));
		add(new PageLink("add", TeacherPage.class));
		add(new DataView("table", new TeacherDataProvider())
		{
			protected void populateItem(final Item item)
			{
				Teacher teacher = (Teacher)item.getModelObject();
				item.add(new Label("firstName", teacher.getFirstName()));
				item.add(new Label("lastName", teacher.getLastName()));

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
					selectedTeacher = ((EntityDetachableModel)getModel()).getId();
					setResponsePage(new TeacherPage(selectedTeacher));
				}
			});
			
			add(new Link("delete", model)
			{
				public void onClick()
				{
					selectedTeacher = ((EntityDetachableModel)getModel()).getId();
					try
					{
						presenter.delete();
					}
					catch(TeacherInCourseException e)
					{
						info("Can not delete teacher");
					}
				}
			});
		}
	}
	
	class TeacherDataProvider implements IDataProvider{
		
		public Iterator iterator(int from, int to) {
			return presenter.getAllTeachers().iterator();
		}

		public IModel model(Object o) {
			return new EntityDetachableModel((Entity)o);
		}

		public int size() {
			return presenter.countAllTeachers();
		}
	}
}
