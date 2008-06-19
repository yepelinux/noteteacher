package com.teracode.example.web.inscription;

import java.util.ArrayList;
import java.util.Iterator;

import wicket.Component;
import wicket.extensions.markup.html.repeater.data.DataView;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;
import wicket.model.PropertyModel;
import wicket.spring.injection.annot.SpringBean;

import com.teracode.example.core.domain.Shift;
import com.teracode.example.core.presenter.inscription.IInscriptionPresenter;
import com.teracode.example.core.presenter.inscription.IInscriptionPresenterView;
import com.teracode.example.web.BasePage;

@SuppressWarnings("serial")
public class InscriptionPage extends BasePage implements IInscriptionPresenterView{

	/*
	@SpringBean
	protected ICourseRepository courseRepository;
	
	@SpringBean
	protected IStudentRepository studentRepository;
	*/
	
	@SpringBean(name="inscriptionPresenter")
	private IInscriptionPresenter inscriptionPresenter;
	
	protected Integer studentId;
	protected String studentName;
	

	protected Integer selectedCourse;
	protected Integer selectedShift;

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStudentName()
	{
		return this.studentName;
	}
	
	public Integer getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Integer selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public Integer getSelectedShift() {
		return selectedShift;
	}

	public void setSelectedShift(Integer selectedShift) {
		this.selectedShift = selectedShift;
	}


	public InscriptionPage(Integer studentId)
	{
		this.studentId = studentId;
		this.inscriptionPresenter.setView(this);
		this.inscriptionPresenter.init();

		add(new Label("studentName", new PropertyModel(InscriptionPage.this, "studentName")));

		add(new FeedbackPanel("feedback"));
		
		add(new InscriptionPageForm("form"));
		
		add(new DataView("table", new InscriptionDataProvider())
		{
			protected void populateItem(final Item item)
			{
				Shift shift = (Shift)item.getModelObject();
				item.add(new Label("course", shift.getCourse().getName()));
				item.add(new Label("shift", shift.getName()));

				item.add(new ActionPanel("action", item.getModel()));
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	class InscriptionPageForm extends Form
	{
		public InscriptionPageForm(String id)
		{
			super(id);

			AbstractReadOnlyModel coursesModel = new AbstractReadOnlyModel()
			{
				@Override
				public Object getObject(Component component) {
					
					return new ArrayList<Integer>(InscriptionPage.this.inscriptionPresenter.getCourses().keySet());
				}
			};
			
			IChoiceRenderer coursesChoiseRenderer = new IChoiceRenderer()
			{
				public Object getDisplayValue(Object object) {
					return InscriptionPage.this.inscriptionPresenter.getCourses().get(object);
				}

				public String getIdValue(Object object, int index) {
					return object.toString();
				}
			};
			

			DropDownChoice ddc = new DropDownChoice("course", new PropertyModel(InscriptionPage.this, "selectedCourse"), coursesModel, coursesChoiseRenderer){
				@Override
				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}
				
				@Override
				protected void onSelectionChanged(Object selected) {
					selectedCourse = (Integer) selected;
					InscriptionPage.this.inscriptionPresenter.selectCourse();
				}
			};
			ddc.setRequired(true);
			add(ddc);

			AbstractReadOnlyModel shiftsModel = new AbstractReadOnlyModel()
			{
				@Override
				public Object getObject(Component component) {
					return new ArrayList<Integer>(InscriptionPage.this.inscriptionPresenter.getShifts().keySet());
				}
			};
			
		
			IChoiceRenderer shiftsChoiseRenderer = new IChoiceRenderer()
			{
				public Object getDisplayValue(Object object) {
					return InscriptionPage.this.inscriptionPresenter.getShifts().get(object);
				}

				public String getIdValue(Object object, int index) {
					return object.toString();
				}
			};
			
			DropDownChoice dds = new DropDownChoice("shift", new PropertyModel(InscriptionPage.this, "selectedShift"), shiftsModel, shiftsChoiseRenderer);
			dds.setRequired(true);
			add(dds);
		}
		
		@Override
		protected void onSubmit() {
			
			InscriptionPage.this.inscriptionPresenter.inscribe();
			InscriptionPage.this.inscriptionPresenter.init();
		}
	}
	/*
	private void init()
	{
		selectedCourse=null;
		selectedShift=null;
		this.courses = new HashMap<Integer, String>();
		this.shifts = new HashMap<Integer, String>();
		
		Student student = studentRepository.get(studentId);
		this.studentName = student.getFirstName();
		
		
		for (Course course : this.courseRepository.getAll())
		{
			if(!course.isInscribed(student))
				this.courses.put(course.getId(), course.getName());
			
		}
	}
	
	private void selectCourse()
	{
		if(selectedCourse!=null)
		{
			shifts = new HashMap<Integer, String>();
			Course course = courseRepository.get(selectedCourse);
			for(Shift shift : course.getShifts())
			{
				if(shift.hasVacancy())
					shifts.put(shift.getId(), shift.getDescription());
			}
			
		}
		
	}
	
	private void inscribe()
	{
		Course course = this.courseRepository.get(getSelectedCourse());
		Shift shift = course.getShift(getSelectedShift());
		Student student = this.studentRepository.get(studentId);
		shift.inscribe(student);
	}
	
	private void uninscribe()
	{
		Student student = studentRepository.get(studentId);
		Course course = courseRepository.get(selectedCourse);
		Shift shift = course.getShift(selectedShift);
		shift.uninscribe(student);
	}
	*/
	class ActionPanel extends Panel
	{

		public ActionPanel(String id, IModel model)
		{
			super(id, model);
			add(new Link("uninscribe", model)
			{
				public void onClick()
				{
					ShiftDetachableModel selected = ((ShiftDetachableModel)getModel());
					selectedCourse = selected.getCourseId();
					selectedShift = selected.getShiftId();
					InscriptionPage.this.inscriptionPresenter.uninscribe();
					InscriptionPage.this.inscriptionPresenter.init();
				}
			});
		}
		
	}

	class InscriptionDataProvider implements IDataProvider
	{
		public Iterator iterator(int from, int to) {
			return InscriptionPage.this.inscriptionPresenter.getAllShift().iterator();
		}

		public IModel model(Object o) {
			return new ShiftDetachableModel((Shift)o);
		}

		public int size() {
			return InscriptionPage.this.inscriptionPresenter.countAllShift();
		}
	}

	class ShiftDetachableModel extends AbstractReadOnlyModel
	{
		private Integer shiftId;
		private Integer courseId;
		private transient Shift shift;
		public ShiftDetachableModel(Shift shift)
		{
			this.shiftId = shift.getId();
			this.courseId = shift.getCourse().getId();
			this.shift = shift;
		}
		
		public Integer getCourseId() {
			return courseId;
		}

		public void setCourseId(Integer courseId) {
			this.courseId = courseId;
		}

		public Integer getShiftId() {
			return shiftId;
		}

		public void setShiftId(Integer shiftId) {
			this.shiftId = shiftId;
		}

		public Object getObject(Component arg0) {
			return shift;
		}
	}

}
