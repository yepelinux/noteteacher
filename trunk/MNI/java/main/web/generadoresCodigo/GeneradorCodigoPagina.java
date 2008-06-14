package generadoresCodigo;

import java.util.Iterator;

import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

import config.ConfigApp;

public class GeneradorCodigoPagina implements IGeneradorCodigo{
	
	/**
	 *  GENERO EL CODIGO DE UNA IMAGEN PARA USAR EN UN FORO
	 *  
	 * @param ImagenUrl
	 * @return
	 */
	public String getCodigoPagina(/*PhotoList listaFotos*/ String foto){
		
		//TODO: se debe usar la lista de fotos, no una foto sola
		
		
		String titulo = "titulo";

//		Iterator lista = listaFotos.iterator();
//		
//		//Tomo el 1ero
//		while (lista.hasNext()) {
//			Photo photo = (Photo) lista.next();
//			System.out.println(photo.getLargeUrl());
//		}
		
		String codigo = 
			
		"<html>" +
			"<head>" +
				"<title>" + titulo + "</title>" +
			"</head>" +
			"<body>" +
			
				"<a href=\"http://" + ConfigApp.URL_APP + "><img border=\"10\" height=\"60\" width=\"60\" src=\""+ foto +"\" Alt=\"MegaPhotos\"></a>"+

			
				
			"</body>" +
			
			
		"</html>";
		
		return codigo;
		
	}
	
	/**
	 * Testeo el generador de codigo
	 * 
	 * @param arg
	 */
	public static void main (String [] arg){
		
		GeneradorCodigoPagina generadorCodigoPagina = new GeneradorCodigoPagina();
		
		String codigo = generadorCodigoPagina.getCodigoPagina("http://farm2.static.flickr.com/1362/535093788_e18bc48644_b.jpg");
		
		System.out.println(codigo);
		
		
	}

}
