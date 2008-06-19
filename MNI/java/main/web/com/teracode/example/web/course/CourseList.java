package com.teracode.example.web.course;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.ResourceModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.common.Entity;
import com.teracode.example.core.domain.Course;
import com.teracode.example.core.presenter.course.ICourseListPresenter;
import com.teracode.example.core.presenter.course.ICourseListPresenterView;
import com.teracode.example.web.BasePage;
import com.teracode.example.web.common.EntityDetachableModel;
import com.teracode.example.web.shift.ShiftPage;

@SuppressWarnings("serial")
public class CourseList extends BasePage implements ICourseListPresenterView{

	/*
	@SpringBean
	protected ICourseRepository courseRepository;
	*/
	
	@SpringBean(name="courseListPresenter")
	private ICourseListPresenter courseListPresenter;
	
	private Integer selectedCourse;
	
	public CourseList()
	{
		add(new PageLink("add", CoursePage.class));
		
		
		int rowsPerPage = 20;
		List<AbstractColumn> list = new ArrayList<AbstractColumn>();
		
		
		
		list.add(new PropertyColumn(new ResourceModel("name", "Name"), "name"));
		list.add(new PropertyColumn(new ResourceModel("description", "Description"), "description"));
		list.add(new PropertyColumn(new ResourceModel("teacher", "Teacher"), "teacher"){
			@Override
			public void populateItem(Item item, String componentId, IModel model) {
				
				Course course = (Course)model.getObject(null);
				
				item.add(new Label(componentId, course.getTeacher().getFirstName()));
			}
		});
				
		list.add(new AbstractColumn(new Model("Actions"))
		{
			public void populateItem(Item cellItem, String componentId, IModel model)
			{
				cellItem.add(new ActionPanel(componentId, model));
			}
		});
		
		final IColumn[] columns = (IColumn[])list.toArray(new IColumn[list.size()]);
		
		DefaultDataTable table = new DefaultDataTable("table", columns, new CourseDataProvider() , rowsPerPage);
		
//		add(new DataView("table", new CourseDataProvider())
//		{
//			protected void populateItem(final Item item)
//			{
//				Course course = (Course)item.getModelObject();
//				item.add(new Label("name", course.getName()));
//				item.add(new Label("description", course.getDescription()));
//				item.add(new Label("teacher", course.getTeacher().getFirstName()));
//
//				item.add(new ActionPanel("action", item.getModel()));
//			}
//		});
		
		add(table);
		
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
					selectedCourse = ((EntityDetachableModel)getModel()).getId();
					setResponsePage(new CoursePage(selectedCourse));
				}
			});
			
			add(new Link("shifts", model)
			{
				public void onClick()
				{
					selectedCourse = ((EntityDetachableModel)getModel()).getId();
					setResponsePage(new ShiftPage(selectedCourse));
				}
			});
			
			add(new Link("delete", model)
			{
				public void onClick()
				{
					selectedCourse = ((EntityDetachableModel)getModel()).getId();
					CourseList.this.courseListPresenter.delete();
					CourseList.this.courseListPresenter.init();
				}
			});
		}
		
	}
	
	class CourseDataProvider extends SortableDataProvider
	{
		public CourseDataProvider(){
			super();
			setSort("name", true);
		}
		
		public Iterator iterator(int from, int to) {
			// TODO Auto-generated method stub
			return CourseList.this.courseListPresenter.getAllCourses().iterator();
		}

		public IModel model(Object o) {
			// TODO Auto-generated method stub
			return new EntityDetachableModel((Entity)o);
		}

		public int size() {
			// TODO Auto-generated method stub
			return CourseList.this.courseListPresenter.countAllCourses();
		}
	}

	public Integer getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Integer selectedCourse) {
		this.selectedCourse = selectedCourse;
	}
	
	/*
	private void delete()
	{
		Course course = this.courseRepository.get(selectedCourse);
		course.delete();
		this.courseRepository.remove(course);
		selectedCourse = null;
	}
	*/
/*
	private void init()
	{
	}
	
	*/
	
	
	
}
