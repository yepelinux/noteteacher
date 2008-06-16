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
			
		} else if(operationType.equals(HomePage.generateIC)){
			return "BuscarImagen";
		} 
		
		return "";
	}
	
}
