package pages.pageGenerator;

import generadoresCodigo.GeneradorCodigoPagina;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;

import pages.basePage.BasePage;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.model.PropertyModel;
import wicket.protocol.http.WebResponse;


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

		@SuppressWarnings("synthetic-access")
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
			
			
			
			Link downloadLink = new Link("downloadButton"){ 
				private static final long serialVersionUID = 1L;
				 
				public void onClick(){ 
					
					if(getHtmlText().length()> 0){
						String tableCode = getHtmlText(); 
						BufferedInputStream bis = null; 
						BufferedOutputStream bos = null; 
						try { 
							String ContentType = "text/html"; 
							getResponse().setContentType(ContentType); 
							((WebResponse)getResponse()).setAttachmentHeader("page.html"); 
							ByteArrayInputStream bais = new ByteArrayInputStream(tableCode.getBytes()); 
							bis = new BufferedInputStream(bais); 
							bos=new BufferedOutputStream(getResponse().getOutputStream()); 
							byte[] buff=new byte[2048]; 
							int bytesread; 
							while((bytesread=bis.read(buff,0,buff.length)) != -1) { 
								bos.write(buff,0,bytesread); 
							} 
							bos.flush(); 
						} catch (IllegalStateException e) {
							//Do nothing 
						} catch (Exception e) {
							//Do nothing 
						} finally{ 
						 	try { 
						 		bos.close();
						 		bos=null; 
						 		bis.close();
						 		bis=null; 
						 
						} catch (IllegalStateException e) {
							//Do nothing
						} catch (Exception e) {
							//Do nothing
						}
					}
				}		 
			}};
			add(downloadLink);
		}
		
		@Override
		protected void onSubmit() {
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
