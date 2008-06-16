package pages.lastSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pages.components.StaticImage;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import buscadores.BuscadorPorNombre;
import buscadores.IBuscadorImagenes;

import com.teracode.example.web.BasePage;

import config.ConfigApp;

@SuppressWarnings("serial")
public class IdSearch extends BasePage {
	
	private String toSearch = "";
	private String operation = ""; 
	
	private IBuscadorImagenes buscador = new BuscadorPorNombre();
	private List<Object> photos = new ArrayList<Object>();
	
	private List<String> selected = new ArrayList<String>();

	public IdSearch(Long operationType) {
		
		setOperation(getStringOperation(operationType));
		add(new Label("operationType", new PropertyModel(IdSearch.this, "operation")));

		
		add(new SearchForm("searchForm"));
		
		WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
		tableContainer.setOutputMarkupId(true);
		add(tableContainer);
		
		ListView listView = createTable("table"); 
		listView.setOutputMarkupId(true);
		tableContainer.add(listView);
		
	}
	
	private class SearchForm extends Form{

		public SearchForm(String id) {
			super(id);
			
			final TextArea toSearch = new TextArea("toSearch", new PropertyModel(IdSearch.this,"toSearch"));
			toSearch.setOutputMarkupId(true);
			add(toSearch);
			
			
			AjaxSubmitLink searchButton = new AjaxSubmitLink("searchButton", SearchForm.this){

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					
					List<Object> list = new ArrayList<Object>();
					try {
						list = getBuscador().getListaFotosMap(getToSearch(), 1, ConfigApp.CAN_IMAGES);
					} catch (Exception e) {	}
					
					setPhotos(list);
					
					target.addComponent(getPage().get("tableContainer"));
				}
			};
			add(searchButton);
		}
		
		@Override
		protected void onSubmit() {
			super.onSubmit();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private ListView createTable(String id) {
		
		for(Object o : getPhotos()){
			((HashMap<String, Object>)o).put("check", false);
		}	
		
		final ListView table = new ListView(id, new PropertyModel(IdSearch.this, "photos")){

			@Override
			protected void populateItem(final ListItem item) {
				
				item.setOutputMarkupId(true);
				final IModel model = new CompoundPropertyModel(item.getModelObject());
				item.setModel(model);
				
				item.add(new Label("id"));
				item.add(new Label("url"));
				
				String url = (String)((HashMap<String, Object>)item.getModelObject()).get("url");
				StaticImage image = new StaticImage("image", new Model(url));
				item.add(image);
				
//				Object modelObject = item.getModelObject();
				CheckBox check = new CheckBox("check");
				check.add(new AjaxFormComponentUpdatingBehavior("onclick") {				
					@Override
					protected void onUpdate(AjaxRequestTarget target) {					
						
						boolean checkState = (Boolean)((HashMap<String, Object>)item.getModelObject()).get("check");
						String id = (String)((HashMap<String, Object>)item.getModelObject()).get("id");
						
						if(checkState){
							getSelected().add(id);
						} else {
							getSelected().remove(id);
						}
						
						target.addComponent(item);
					}
				});
				item.add(check);
				item.setOutputMarkupId(true);
			}
		};
		table.setOutputMarkupId(true);
		return table;
	}
	
	public String getToSearch() {return toSearch;}

	public void setToSearch(String toSearch) {this.toSearch = toSearch;}

	public IBuscadorImagenes getBuscador() {return buscador;}

	public void setBuscador(IBuscadorImagenes buscadotPorTag) {this.buscador = buscadotPorTag;}

	public List<Object> getPhotos() {return photos;}

	public void setPhotos(List<Object> photos) {this.photos = photos;}

	public List<String> getSelected() {return selected;}

	public void setSelected(List<String> selected) {this.selected = selected;}

	public String getOperation() {return operation;}

	public void setOperation(String operation) {this.operation = operation;}
}


