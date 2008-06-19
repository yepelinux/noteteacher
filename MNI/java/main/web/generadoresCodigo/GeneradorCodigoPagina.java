package generadoresCodigo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import buscadores.BuscadorPorTag;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

import config.ConfigApp;

public class GeneradorCodigoPagina implements IGeneradorCodigo{
	
	public static final int IMAGENES_POR_FILA = 3;
	
	/**
	 *  Genera todo el codigo html de una pagina web que muestra las fotos que recibe por parametro
	 *  
	 * @param ImagenUrl
	 * @return
	 */
	public String getCodigoPagina(PhotoList listaFotos,String titulo){

		String encabezado;
		String cuerpo;
		String contenido;
		String pie;
		
		/**
		 * Genero el contenido de imagenes de la pagina
		 */
		contenido = generarContenido(listaFotos);
		
		
		
		encabezado = 
		
			"<html>" +'\n'+
			"	<head>" +'\n'+
			"		<title>" + titulo + "</title>" +'\n'+
			"	</head>" +'\n';
		
		
		cuerpo = 
			
			"	<body>" +'\n'+
			"		<font size=\"7\" face=\"Impact\">" + titulo + "</font>" +'\n'+
			"		<br>" +'\n'+
			"		<table width=\"100%\">" +'\n';
			
		cuerpo += contenido;

		cuerpo +=
		
			"		</table >" +'\n'+
			"	</body>" +'\n';
		
		pie = 
			
			"</html>";

		
		return encabezado + cuerpo + pie;
		
	}
	
	/**
	 * 
	 * Genera el codigo html de la porcion de la pagina que muestra la fotos. 
	 * Las imagenes se generan con filas de tres imagenes
	 * 
	 * @param foto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String generarContenido(PhotoList listaFotos){
		
		int i;
		String contenido;
		
		/**
		 * Comienza la primera fila
		 */
		
		contenido = 
			"			<tr>" +'\n';
		
		
		i=0;
		for(Photo photo : (List<Photo>)listaFotos){
			
			if(i%IMAGENES_POR_FILA == 0 && i != 0){
				
				contenido +=
				
					"			<tr>" +'\n'+
					"			<tr>" +'\n';
			}
			
			/**
			 * genero una a una las imagenes 
			 */
			
			contenido +=
			
				"				<td>" +'\n'+
				"					<div>" + photo.getTitle() + "</div>" +'\n'+
				"					<a href=\"http://" + ConfigApp.URL_APP + "\"><img border=\"10\" height=\"60\" width=\"60\" src=\""+ photo.getLargeUrl() +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
				"				<td>" +'\n';
			
			
			i++;
		}
		
		/**
		 * Cierro la ultima fila
		 */
		
		contenido += 
		
		"			<tr>" + '\n';
		
		return contenido;
		
	}
	
	/**
	 * Testeo el generador de codigo
	 * 
	 * @param arg
	 */
	public static void main (String [] arg){
		
		/**
		 * testeo el generador de paginas buscando imagenes.
		 */
		
		BuscadorPorTag buscadorPorTag = new BuscadorPorTag();
		
		try {
			
			/**
			 * busco fotos
			 */
			PhotoList listaFotos = buscadorPorTag.getListaFotos("silverchair", 1, 9);
			
			/**
			 * genero la pagina html
			 */			
			GeneradorCodigoPagina generadorCodigoPagina = new GeneradorCodigoPagina();
			String codigo = generadorCodigoPagina.getCodigoPagina(listaFotos,"Mi pagina de fotos locas");
			System.out.println(codigo);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getCodigoPagina(List<HashMap<String, Object>> listaFotos,  String titulo){

		String encabezado;
		String cuerpo;
		String contenido;
		String pie;
		
		/**
		 * Genero el contenido de imagenes de la pagina
		 */
		contenido = generarContenido(listaFotos);
		
		
		
		encabezado = 
		
			"<html>" + '\n'+ 
			"	<head>" +'\n'+
			"		<title>" + titulo + "</title>" +'\n'+
			"	</head>" +'\n' +
		
			"<style type=\"text/css\">" +'\n'+			
			"body { "+ '\n'+
				"font-family: Verdana, arial, sans-serif;"+'\n'+
				"font-size: 82%;"+'\n'+
				"background-color: white;"+'\n'+
				"background-image: url(images/pageBg.jpg);"+'\n'+
				"background-repeat: repeat-x;"+'\n'+
			"}"+'\n'+
				
			"H1 {"+'\n'+
			"	color: #005066;"+'\n'+
			"	text-align: center;"+'\n'+
			"}"+'\n'+
				
			"div.imageTitle{"+'\n'+
			"	font-size: 0.8em;"+'\n'+
			"	color: 005066;"+'\n'+
			"	background-image: url(images/top_subpanel/top_subpanel_center.jpg);"+'\n'+ 
			"	background-repeat: repeat-x;"+'\n'+
			"	padding-left: 7px;"+'\n'+
			"	padding-right: 7px;"+'\n'+
			"} "+'\n'+
			
			".maintable{"+'\n'+
			"	position: relative;"+'\n'+
			"	top: 50px;"+'\n'+
			"}"+'\n'+
			
			"table td{"+'\n'+
			"	padding: 20px;"+'\n'+
			"}"+'\n'+
			
			"img{"+'\n'+
			"	margin-top: 10px;"+'\n'+
			"}"+'\n'+
			" </style>"+'\n';

		
		
		cuerpo = 
			
			"	<body>" +'\n'+
			"		<H1 >" + titulo + "</H1>" +'\n'+
			"		<br>" +'\n'+
			"		<table align=\"center\" class=\"mainTable\" >" + '\n';
			
		cuerpo += contenido;

		cuerpo +=
		
			"		</table >" +'\n'+
			"	</body>" +'\n';
		
		pie = 
			
			"</html>";

		
		return encabezado + cuerpo + pie;
		
	}
	
	/**
	 * 
	 * Genera el codigo html de la porcion de la pagina que muestra la fotos. 
	 * Las imagenes se generan con filas de tres imagenes
	 * 
	 * @param foto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String generarContenido(List<HashMap<String, Object>> listaFotos){
		
		int i;
		String contenido;
		
		/**
		 * Comienza la primera fila
		 */
		
		contenido = 
			"			<tr>" +'\n';
		
		
		i=0;
		for(Map photo : listaFotos){
			
			if(i%IMAGENES_POR_FILA == 0 && i != 0){
				
				contenido +=
				
					"			<tr>" +'\n'+
					"			<tr>" +'\n';
			}
			
			/**
			 * genero una a una las imagenes 
			 */
			contenido +=
			
				"				<td align=\"center\">" +'\n'+
				"					<div class=\"imageTitle\">" + photo.get("title") + "</div>" +'\n'+
				"					<a href=\"http://" + ConfigApp.URL_APP + "\"><img height=\"110\" width=\"110\" src=\""+ photo.get("url") +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
				"				<td>" +'\n';
			
			
			i++;
		}
		
		/**
		 * Cierro la ultima fila
		 */
		
		contenido += 
		
		"			<tr>" + '\n';
		
		return contenido;
		
	}

	
	

}
