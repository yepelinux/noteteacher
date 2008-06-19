package pages.pageGenerator;

import generadoresCodigo.GeneradorCodigoPagina;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import wicket.IRequestTarget;
import wicket.RequestCycle;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.model.PropertyModel;
import wicket.model.StringResourceModel;
import wicket.protocol.http.WebResponse;
import wicket.util.io.Streams;

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
			
			AjaxSubmitLink downloadButton = new AjaxSubmitLink("downloadButton", InputForm.this){

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					InputForm.this.onSubmit();
				}
			};
			add(downloadButton);

		}
		
		@Override
		protected void onSubmit() {
			System.err.println("down");
			downloadHtml(getHtmlText());
//			downloadSendingInZip();
		}
	}

	private void downloadHtml(final String sending) {
		
		if (sending.length() > 0){
			RequestCycle.get().setRequestTarget(new IRequestTarget() {
			
				public void detach(RequestCycle requestCycle) {}
	
				public Object getLock(RequestCycle requestCycle) { return null; }
				
				public void respond(RequestCycle requestCycle){
					WebResponse response = (WebResponse)requestCycle.getResponse();
					
					// Edison specs determines that chars 6 to 19 are the reference field
					String reference = sending.substring(6, 19);
					int spacePosition = sending.substring(6, 19).indexOf(' ');
					
					response.setAttachmentHeader(reference.substring(0, spacePosition) + ".txt");
					
					try {
						
						ByteArrayOutputStream ostream = new ByteArrayOutputStream();
						BufferedWriter out = new BufferedWriter(new OutputStreamWriter(ostream, "Cp850"));
						out.write(sending);
						out.close();

						InputStream istream = new ByteArrayInputStream(ostream.toByteArray()); 
						
						try	{
							
							Streams.copy(istream, response.getOutputStream());
							// Exception due to checked exception							
						} catch (Exception e) {
							throw new RuntimeException(e);
							
						} finally {
							
							try	{
								istream.close();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
						
					} catch (Exception e) {
						// Exception due to checked exception
						throw new RuntimeException(e);
					}
				}
			});
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
	
	private void downloadSendingInZip(){
		
		RequestCycle.get().setRequestTarget(new IRequestTarget() {
			
			public void detach(RequestCycle requestCycle) {}

			public Object getLock(RequestCycle requestCycle) { return null; }
			
			public void respond(RequestCycle requestCycle){
				
				WebResponse response = (WebResponse)requestCycle.getResponse();
				
				// Zip file
				ByteArrayOutputStream zipout = new ByteArrayOutputStream();				
		        ZipOutputStream s = new ZipOutputStream(zipout);
		        
		        s.setLevel(6);
				
				try{
											
						String sending = getHtmlText();
						
						byte[] buf = new byte[sending.length()];
						
						String reference = sending.substring(6, 19);
						int spacePosition = sending.substring(6, 19).indexOf(' ');
						
						// Create stream file with the sending
						ByteArrayOutputStream ostream = new ByteArrayOutputStream();
						BufferedWriter out = new BufferedWriter(new OutputStreamWriter(ostream, "Cp850"));
						out.write(sending);
						out.close();
	
						InputStream istream = new ByteArrayInputStream(ostream.toByteArray());
											
						// Add new entry in zip file
						ZipEntry entry = new ZipEntry(reference.substring(0, spacePosition));
												
						s.putNextEntry(entry);
						
						// Write the sending in the entry					
						istream.read(buf,0,buf.length);		            
			            s.write(buf,0,buf.length);
			            
						s.closeEntry();					
					
					s.finish();
		            s.close();
		            System.err.println("end down");
	            
				}catch(IOException e){
					throw new RuntimeException(e);	
				}
												
		            
				String zipFileName = new StringResourceModel("zipFileName", PageCodeGenerator.this, null).getString();
		        
				response.setAttachmentHeader(zipFileName + ".zip");
				
				try {
						
					InputStream istream = new ByteArrayInputStream(zipout.toByteArray());
					
					try	{						
						
						Streams.copy(istream, response.getOutputStream());
						
						// Exception due to checked exception							
					} catch (Exception e) {
						
						throw new RuntimeException(e);
						
					} finally {
						
						try	{
							istream.close();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					
				} catch (Exception e) {
					// Exception due to checked exception
					throw new RuntimeException(e);
				}
			}
		});
	}

}
