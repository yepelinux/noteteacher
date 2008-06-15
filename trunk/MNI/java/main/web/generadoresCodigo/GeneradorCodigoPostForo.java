package generadoresCodigo;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import buscadores.BuscadorPorTag;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

import config.ConfigApp;

public class GeneradorCodigoPostForo implements IGeneradorCodigo{
	
	/** 
	 * Dejar en 1. 
	 */
	
	public static final int IMAGENES_POR_FILA = 1;
	
	/**
	 *  Genera todo el codigo (BBCODE) para utilizar imagenes en un post de un foro
	 *  
	 * @param ImagenUrl
	 * @return
	 */
	public String getCodigoPagina(PhotoList listaFotos, String titulo){
		
		String encabezado;
		String cuerpo;
		
		/**
		 * titulo
		 */
		
		encabezado = 
			"[size=24][b]" + titulo + "[/b][/size]"+'\n';
		
		
		/**
		 * abro el listado de fotos 
		 */
		
		cuerpo =
			"[list]"+'\n';
				
		/**
		 * Genero el contenido de imagenes del post
		 */
		cuerpo += generarContenido(listaFotos);
		
		/**
		 * cierro el listado de fotos 
		 */
		
		cuerpo +=
			"[/list]";
		
		return encabezado + cuerpo;
		
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
	public String generarContenido(PhotoList listaFotos){
		
		int i;
		String contenido;
		
		/**
		 * Comienza la primera fila
		 */
		
		contenido = "";
		
		
		
		
		i=0;
		for(Photo photo : (List<Photo>)listaFotos){
			
			if(i%IMAGENES_POR_FILA == 0 && i != 0){
				
				contenido +=
					
					'\n';
					
			}
			
			/**
			 * genero una a una las imagenes 
			 */
			
			contenido +=
				
				"[*][size=14][b]" + photo.getTitle() + "[/b][/size]"+'\n'+
				"[URL=http://" + ConfigApp.URL_APP + "][IMG]" + photo.getSmallUrl() + "[/IMG][/URL]"+'\n';
						
			i++;
		}

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
			GeneradorCodigoPostForo generadorCodigoPostForo = new GeneradorCodigoPostForo();
			String codigo = generadorCodigoPostForo.getCodigoPagina(listaFotos, "titulo del post");
			System.out.println(codigo);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
	}

}
