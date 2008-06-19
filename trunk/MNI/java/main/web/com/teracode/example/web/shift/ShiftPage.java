package com.teracode.example.web.shift;

import java.util.Iterator;

import wicket.extensions.markup.html.repeater.data.DataView;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.RequiredTextField;
import wicket.markup.html.form.validation.NumberValidator;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.PropertyModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.common.Entity;
import com.teracode.example.core.common.ObjectAlreadyExistsException;
import com.teracode.example.core.domain.Shift;
import com.teracode.example.core.presenter.shift.IShiftPresenter;
import com.teracode.example.core.presenter.shift.IShiftPresenterView;
import com.teracode.example.web.BasePage;
import com.teracode.example.web.common.EntityDetachableModel;

@SuppressWarnings("serial")
public class ShiftPage extends BasePage implements IShiftPresenterView{

	/*
	@SpringBean
	protected ICourseRepository courseRepository;
	*/
	
	@SpringBean(name="shiftPresenter")
	private IShiftPresenter shiftPresenter;
	
	
	final private Integer courseId;
	private String courseName;
	private Integer shiftId;
	private String name;
	private String description;
	private Integer vacancy;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVacancy() {
		return vacancy;
	}

	public void setVacancy(Integer vacancy) {
		this.vacancy = vacancy;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	
	public Integer getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return courseName;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ShiftPage()
	{
		this(null);
	}
	
	public ShiftPage(Integer courseId)
	{
		this.courseId = courseId;
		this.shiftId = null;
		this.shiftPresenter.setView(this);
		this.shiftPresenter.init();
		
		add(new Label("courseName", new PropertyModel(ShiftPage.this, "courseName")));
		
		add(new FeedbackPanel("feedback"));
		add(new ShiftPageForm("form"));
		
		add(new DataView("table", new ShiftDataProvider())
		{
			protected void populateItem(final Item item)
			{
				Shift shift = (Shift)item.getModelObject();
				item.add(new Label("name", shift.getName()));
				item.add(new Label("description", shift.getDescription()));
				item.add(new Label("vacancy", new Integer(shift.getVacancy()).toString()));
				item.add(new Label("students", new Integer(shift.getStudents().size()).toString()));
				item.add(new ActionPanel("action", item.getModel()));
			}
		});
	}
	
	class ShiftPageForm extends Form
	{
		@SuppressWarnings("unchecked")
		public ShiftPageForm(String id) {
			super(id);

			add(new RequiredTextField("name", new PropertyModel(ShiftPage.this, "name"))
			{
				@Override
				public boolean isEnabled() {
					return shiftId==null;
				}
			});
			
			add(new RequiredTextField("description", new PropertyModel(ShiftPage.this, "description")));
			
			RequiredTextField rtf = new RequiredTextField("vacancy", new PropertyModel(ShiftPage.this, "vacancy"))
			{
				@Override
				public boolean isEnabled() {
					return shiftId==null;
				}
			};
			rtf.setType(int.class);
			rtf.add(NumberValidator.range(0, 100));
			add(rtf);
		}
		
		@Override
		protected void onSubmit() {
			
			try
			{
				ShiftPage.this.shiftPresenter.save();
				ShiftPage.this.shiftPresenter.init();
			}
			catch (ObjectAlreadyExistsException e) {
				info("Shift Already exists");
			}
		}
	}

/*	
	private void init()
	{
		this.courseName = courseRepository.get(courseId).getName();
		
		if(this.shiftId!=null)
		{
			Shift shift = this.courseRepository.get(courseId).getShift(shiftId);
			this.name = shift.getName();
			this.description = shift.getDescription();
			this.vacancy = shift.getVacancy();
		}
		else
		{
			this.name = null;
			this.description = null;
			this.vacancy = null;
		}
	}
	
	private void delete()
	{
		Shift shift = this.courseRepository.get(courseId).getShift(shiftId);
		shift.delete();
		shiftId = null;
	}
	
	
	private void save()
	{
		if(this.shiftId==null)
		{
			Course course = this.courseRepository.get(this.courseId);
			new Shift(this.name, this.description, this.vacancy, course);
		}
		else
		{
			Shift shift = this.courseRepository.get(courseId).getShift(shiftId);
			shift.modify(this.description);
		}
		this.shiftId = null;
	}
*/
	class ActionPanel extends Panel
	{
		public ActionPanel(String id, IModel model)
		{
			super(id, model);
			add(new Link("delete", model)
			{
				public void onClick()
				{
					shiftId = ((EntityDetachableModel)getModel()).getId();
					ShiftPage.this.shiftPresenter.delete();
				}
			});
			
			add(new Link("select", model)
			{
				public void onClick()
				{
					shiftId = ((EntityDetachableModel)getModel()).getId();
					ShiftPage.this.shiftPresenter.init();
				}
			});
		}
	}
	class ShiftDataProvider implements IDataProvider
	{
		
		public Iterator iterator(int from, int to) {
			
			return ShiftPage.this.shiftPresenter.getAllShift().iterator();
			
		}

		public IModel model(Object o) {
			return new EntityDetachableModel((Entity)o);
		}

		public int size() {
			return ShiftPage.this.shiftPresenter.countAllShift();
		}
	}


}
