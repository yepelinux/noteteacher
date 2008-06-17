package pages.imageGenerator;

import generadoresCodigo.GeneradorCodigoImagen;

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
public class ImageCodeGenerator extends BasePage {
	
	private GeneradorCodigoImagen generador =  new GeneradorCodigoImagen();
	private String htmlText = "";
//	private String title = "";
	private Long alto = 60L;
	private Long ancho = 60L;
	private List<HashMap<String, Object>> photos;
	

	public ImageCodeGenerator(List<HashMap<String, Object>> photos) {
		
		setPhotos(photos);
		add(new InputForm("inputForm"));
		
		WebMarkupContainer htmlContainer = new WebMarkupContainer("htmlContainer");
		htmlContainer.setOutputMarkupId(true);
		add(htmlContainer);
		
		
		TextArea html = new TextArea("html", new PropertyModel(ImageCodeGenerator.this,"htmlText")){
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
			
			
			add(new TextField("alto", new PropertyModel(ImageCodeGenerator.this,"alto")));
			
			add(new TextField("ancho", new PropertyModel(ImageCodeGenerator.this,"ancho")));
			
			
			AjaxSubmitLink generateButton = new AjaxSubmitLink("generateButton", InputForm.this){

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					setHtmlText(generador.getCodigoImagen(getPhotos().iterator().next(), getAlto().intValue(), getAncho().intValue()));
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


	public GeneradorCodigoImagen getGenerador() {return generador;}

	public void setGenerador(GeneradorCodigoImagen generador) {this.generador = generador;}

	public String getHtmlText() {return htmlText;}

	public void setHtmlText(String htmlText) {this.htmlText = htmlText;}

	public List<HashMap<String, Object>> getPhotos() {return photos;}

	public void setPhotos(List<HashMap<String, Object>> photos) {this.photos = photos;}

	public Long getAlto() {return alto;}

	public void setAlto(Long alto) {this.alto = alto;}

	public Long getAncho() {return ancho;}

	public void setAncho(Long ancho) {this.ancho = ancho;}
}
