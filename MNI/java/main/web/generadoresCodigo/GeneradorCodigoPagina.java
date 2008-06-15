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

public class GeneradorCodigoPagina implements IGeneradorCodigo{
	
	public static final int IMAGENES_POR_FILA = 3;
	
	/**
	 *  Genera todo el codigo html de una pagina web que muestra las fotos que recibe por parametro
	 *  
	 * @param ImagenUrl
	 * @return
	 */
	public String getCodigoPagina(PhotoList listaFotos,String titulo){
		
		//TODO: se debe usar la lista de fotos, no una foto sola
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
	public String generarContenido(PhotoList listaFotos){
		
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
			String codigo = generadorCodigoPagina.getCodigoPagina(listaFotos,"mi pagina de fotos locas");
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
