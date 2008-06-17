package pages.pageGenerator;

import generadoresCodigo.GeneradorCodigoPagina;

import java.util.HashMap;
import java.util.List;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.model.PropertyModel;

import com.teracode.example.web.BasePage;

@SuppressWarnings("serial")
public class PageCodeGenerator extends BasePage {
	
	private GeneradorCodigoPagina generador =  new GeneradorCodigoPagina();
	private String htmlText = "";
	private String title = "";
	private List<HashMap<String, Object>> photos;
	

	public PageCodeGenerator(List<HashMap<String, Object>> photos) {
		
		setPhotos(photos);
		add(new InputForm("inputForm"));
		
		WebMarkupContainer htmlContainer = new WebMarkupContainer("htmlContainer");
		htmlContainer.setOutputMarkupId(true);
		add(htmlContainer);
		
		
		TextArea html = new TextArea("html", new PropertyModel(PageCodeGenerator.this,"htmlText")){
			@Override
			public boolean isVisible() {
				return !getHtmlText().equals("");
			}
		};
		html.setOutputMarkupId(true);
		htmlContainer.add(html);
		
	}
	
	private class InputForm extends Form{

		public InputForm(String id) {
			super(id);
			
			
			add(new TextField("title", new PropertyModel(PageCodeGenerator.this,"title")));
			
			
			AjaxSubmitLink generateButton = new AjaxSubmitLink("generateButton", InputForm.this){

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					setHtmlText(generador.getCodigoPagina(getPhotos(), getTitle()));
					target.addComponent(getPage().get("htmlContainer"));
				}
			};
			add(generateButton);
		}
		
		@Override
		protected void onSubmit() {
			super.onSubmit();
		}
	}

	public GeneradorCodigoPagina getGenerador() {return generador;	}

	public void setGenerador(GeneradorCodigoPagina generador) {this.generador = generador;}

	public String getHtmlText() {return htmlText;}

	public void setHtmlText(String htmlText) {this.htmlText = htmlText;}

	public String getTitle() {return title;}

	public void setTitle(String title) {this.title = title;}

	public List<HashMap<String, Object>> getPhotos() {return photos;}

	public void setPhotos(List<HashMap<String, Object>> photos) {this.photos = photos;}
}
