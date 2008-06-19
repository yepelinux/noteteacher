package pages.homePage;

import pages.idSearch.LastSearch;
import pages.lastSearch.IdSearch;
import pages.tagSearch.TagSearch;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.model.PropertyModel;

import com.teracode.example.web.BasePage;

@SuppressWarnings("serial")
public class HomePage extends BasePage {

	//-----------------------------------------------------
	public static Long none			 	= 0L;
	public static Long downloadImage 	= 1L;
	public static Long generateIC 		= 2L;
	public static Long generatePC 		= 3L;
	public static Long searchImage 		= 4L;
	public static Long generateFC 		= 5L;
	public static Long generateBC 		= 6L;
	//-----------------------------------------------------
	public static Long searchNone	 	= 0L;
	public static Long searchTag	 	= 1L;
	public static Long searchId 		= 2L;
	public static Long searchLast 		= 3L;
	//-----------------------------------------------------
	
	private boolean showPanel = false;
	private String operation = "";
	private String search = "";
	private Long operationType = HomePage.none; 
	private Long searchType	= HomePage.searchNone;
	//-----------------------------------------------------
	
	public HomePage() {
		
		add(new PageForm("pageForm"));
	}
	//-----------------------------------------------------
	
	private class PageForm extends Form{

		public PageForm(String id) {
			super(id);
			
			
			//---------------------------------------------------
			//------------Operation Type Label
			//---------------------------------------------------
			final Label operationLabel = new Label("operationType" , new PropertyModel(HomePage.this,"operation"));
			operationLabel.setOutputMarkupId(true);
			add(operationLabel);
			
			//Operation type Container
			final WebMarkupContainer mainContainer = new WebMarkupContainer("mainContainer");
			mainContainer.setOutputMarkupId(true);
			add(mainContainer);
			
			//---------------------------------------------------
			//------------search Type Label
			//---------------------------------------------------
			//Operation type Container
			final WebMarkupContainer searchContainer = new WebMarkupContainer("searchContainer");
			searchContainer.setOutputMarkupId(true);
			add(searchContainer);
			
			
			final WebMarkupContainer searchMsg = new WebMarkupContainer("searchMsg"){
				@Override
				public boolean isVisible() {
					return !getSearchType().equals(HomePage.searchNone);
				}	
			};
			searchMsg.setOutputMarkupId(true);
			searchContainer.add(searchMsg);
			
			
			final Label searchLabel = new Label("searchType" , new PropertyModel(HomePage.this,"search")){
				@Override
				public boolean isVisible() {
					return !getSearchType().equals(HomePage.searchNone);
				}
			};
			searchLabel.setOutputMarkupId(true);
			searchMsg.add(searchLabel);
			
			
			//---------------------------------------------------
			//------------Operation Type Buttons
			//---------------------------------------------------
//			AjaxLink downloadImage = new AjaxLink("download"){
//				@Override
//				public void onClick(AjaxRequestTarget target) {
//					setOperation("Download Imagen");
//					setOperationType(HomePage.downloadImage);
//					target.addComponent(operationLabel);
//					target.addComponent(mainContainer);
//				}
//			};
//			add(downloadImage);
			
			
			AjaxLink generateImage = new AjaxLink("generateCodeImage"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setOperation("Generar Código de Imagen");
					setOperationType(HomePage.generateIC);
					target.addComponent(operationLabel);
					target.addComponent(mainContainer);
				}
			};
			add(generateImage);

			
			AjaxLink generatePage = new AjaxLink("generateCodePage"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setOperation("Generar Código de Pagina");
					setOperationType(HomePage.generatePC);
					target.addComponent(operationLabel);
					target.addComponent(mainContainer);
				}
			};
			add(generatePage);
			
			AjaxLink generateForum = new AjaxLink("generateCodeForum"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setOperation("Generar Código de Forum");
					setOperationType(HomePage.generateFC);
					target.addComponent(operationLabel);
					target.addComponent(mainContainer);
				}
			};
			add(generateForum);

			AjaxLink generateBlog = new AjaxLink("generateCodeBlog"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setOperation("Generar Código de Blog");
					setOperationType(HomePage.generateBC);
					target.addComponent(operationLabel);
					target.addComponent(mainContainer);
				}
			};
			add(generateBlog);

			
			AjaxLink searchImage = new AjaxLink("searchImage"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setOperation("Buscar Imagen");
					setOperationType(HomePage.searchImage);
					target.addComponent(operationLabel);
					target.addComponent(mainContainer);
				}
			};
			add(searchImage);
			
			AjaxLink clear = new AjaxLink("clear"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setOperation("");
					setSearch("");
					setOperationType(HomePage.none);
					setSearchType(HomePage.searchNone);
					
					target.addComponent(getPage().get("pageForm"));
				}
			};
			add(clear);

			

			//---------------------------------------------------
			//------------Search Type Buttons
			//---------------------------------------------------
			
			
			WebMarkupContainer panelContainer = new WebMarkupContainer("panelContainer"){
				@Override
				public boolean isVisible() {
					return !getOperationType().equals(HomePage.none);
				}
			};
			panelContainer.setOutputMarkupId(true);
			mainContainer.add(panelContainer);
			
			
			AjaxLink tag = new AjaxLink("tag"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setSearch("Por Tag");
					setSearchType(HomePage.searchTag);
					target.addComponent(searchContainer);
				}
			};
			panelContainer.add(tag);
			
			
			AjaxLink flickrId = new AjaxLink("flickrId"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setSearch("Por Flickr Id");
					setSearchType(HomePage.searchId);
					target.addComponent(searchContainer);
				}
			};
			panelContainer.add(flickrId);

			
			AjaxLink last = new AjaxLink("last"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					setSearch("Ultimas");
					setSearchType(HomePage.searchLast);
					target.addComponent(searchContainer);
				}
			};
			panelContainer.add(last);
			
		}
		
		
		@Override
		protected void onSubmit() {
			
			if(!getOperationType().equals(HomePage.none)){
			
				if(getSearchType().equals(HomePage.searchTag)){
					setResponsePage(new TagSearch(getOperationType()));
					
				} else if(getSearchType().equals(HomePage.searchId)) {
					setResponsePage(new IdSearch(getOperationType()));
					
				} else if(getSearchType().equals(HomePage.searchLast)) {
					setResponsePage(new LastSearch(getOperationType()));				
				}
			}
				
		}
	}

	//-----------------------------------------------------
	public boolean isShowPanel() {return showPanel;}

	public void setShowPanel(boolean showPanel) {this.showPanel = showPanel;}

	public String getOperation() {return operation;}

	public void setOperation(String operation) {this.operation = operation;}

	public Long getOperationType() {return operationType;}

	public void setOperationType(Long operationType) {this.operationType = operationType;}

	public String getSearch() {return search;}

	public void setSearch(String search) {this.search = search;}

	public Long getSearchType() {return searchType;}

	public void setSearchType(Long searchType) {this.searchType = searchType;}
}

