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

public class GeneradorCodigoBlog implements IGeneradorCodigo{
	
	public static final int IMAGENES_POR_FILA = 3;
	
	/**
	 *  Genera todo el codigo html de una pagina web que muestra las fotos que recibe por parametro
	 *  
	 * @param ImagenUrl
	 * @return
	 */
	public String getCodigoPagina(PhotoList listaFotos,String titulo){
		
		String cuerpo;
		String contenido;
		
		
		/**
		 * Genero el contenido de imagenes de la pagina
		 */
		contenido = generarContenido(listaFotos);
		
		
		
		cuerpo =
			
			"<table width=\"100%\">" +'\n';
			
		cuerpo += contenido;

		cuerpo +=
		
			"</table >" +'\n';
		
		return cuerpo;
		
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
			"	<tr>" +'\n';
		
		
		i=0;
		for(Photo photo : (List<Photo>)listaFotos){
			
			if(i%IMAGENES_POR_FILA == 0 && i != 0){
				
				contenido +=
				
					"	<tr>" +'\n'+
					"	<tr>" +'\n';
			}
			
			/**
			 * genero una a una las imagenes 
			 */
			
			contenido +=
			
				"		<td>" +'\n'+
				"			<div>" + photo.getTitle() + "</div>" +'\n'+
				"			<a href=\"http://" + ConfigApp.URL_APP + "\"><img border=\"10\" height=\"60\" width=\"60\" src=\""+ photo.getLargeUrl() +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
				"		<td>" +'\n';
			
			
			i++;
		}
		
		/**
		 * Cierro la ultima fila
		 */
		
		contenido += 
		
		"	<tr>" + '\n';
		
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
			GeneradorCodigoBlog generadorCodigoPagina = new GeneradorCodigoBlog();
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

		String cuerpo;
		String contenido;

		
		/**
		 * Genero el contenido de imagenes de la pagina
		 */
		contenido = generarContenido(listaFotos);
		
		

		
		
		cuerpo = 
			
			"<table width=\"100%\">" +'\n';
			
		cuerpo += contenido;

		cuerpo +=
		
			"</table >" +'\n';



		
		return cuerpo;
		
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
			"	<tr>" +'\n';
		
		
		i=0;
		for(Map photo : listaFotos){
			
			if(i%IMAGENES_POR_FILA == 0 && i != 0){
				
				contenido +=
				
					"	<tr>" +'\n'+
					"	<tr>" +'\n';
			}
			
			/**
			 * genero una a una las imagenes 
			 */
			contenido +=
			
				"		<td>" +'\n'+
				"			<div>" + photo.get("title") + "</div>" +'\n'+
				"			<a href=\"http://" + ConfigApp.URL_APP + "\"><img border=\"2px\" height=\"60\" width=\"60\" src=\""+ photo.get("url") +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
				"		<td>" +'\n';
			
			
			i++;
		}
		
		/**
		 * Cierro la ultima fila
		 */
		
		contenido += 
		
		"	<tr>" + '\n';
		
		return contenido;
		
	}
}
