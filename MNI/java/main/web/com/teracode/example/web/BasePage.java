package com.teracode.example.web;

import java.io.Serializable;

import pages.homePage.HomePage;
import wicket.markup.html.link.PageLink;

public abstract class BasePage extends ExamplePage implements Serializable{

	public BasePage()
	{
		add(new PageLink("homePage", HomePage.class));
	}
	
	public String getStringOperation(Long operationType){
		
		if(operationType.equals(HomePage.downloadImage)){
			return "Download Imagen";
		
		} else if(operationType.equals(HomePage.generatePC)){
			return "Generar Codigo de Pagina";
			
		} else if(operationType.equals(HomePage.generateIC)){
			return "Generar Codigo de Imagen";
			
		} else if(operationType.equals(HomePage.searchImage)){
			return "BuscarImagen";
			
		} else if(operationType.equals(HomePage.generateFC)){
			return "Generar Código de Forum";
			
		} else if(operationType.equals(HomePage.generateBC)){
			return "Generar Código de Blog";
		}
		
		return "";
	}
	
}
