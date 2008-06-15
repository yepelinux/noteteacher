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
	public String getCodigoPagina(/*PhotoList listaFotos*/ String foto,String titulo){
		
		//TODO: se debe usar la lista de fotos, no una foto sola
		
		
		titulo = "MegaPhotos - Usá tus fotos";

//		Iterator lista = listaFotos.iterator();
//		
//		//Tomo el 1ero
//		while (lista.hasNext()) {
//			Photo photo = (Photo) lista.next();
//			System.out.println(photo.getLargeUrl());
//		}
		

		
		String codigo = 
			
		"<html>" +'\n'+
		"	<head>" +'\n'+
		"		<title>" + titulo + "</title>" +'\n'+
		"	</head>" +'\n'+
		"	<body>" +'\n'+
		"		<font size=\"7\" face=\"Impact\">" + titulo + "</font>" +'\n'+		
		"		<table width=\"100%\">" +'\n'+
		"			<tr>" +'\n'+
		"				<td>" +'\n'+
		"					<a href=\"http://" + ConfigApp.URL_APP + "\"><img border=\"10\" height=\"60\" width=\"60\" src=\""+ foto +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
		"				<td>" +'\n'+
		"				<td>" +'\n'+
		"					<a href=\"http://" + ConfigApp.URL_APP + "\"><img border=\"10\" height=\"60\" width=\"60\" src=\""+ foto +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
		"				<td>" +'\n'+
		"				<td>" +'\n'+
		"					<a href=\"http://" + ConfigApp.URL_APP + "\"><img border=\"10\" height=\"60\" width=\"60\" src=\""+ foto +"\" Alt=\"MegaPhotos\"></a>"+'\n'+
		"				<td>" +'\n'+
		"			<tr>" + '\n' +
		"		</table >" +'\n'+
		"	</body>" +'\n'+			
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
		
		String codigo = generadorCodigoPagina.getCodigoPagina("http://farm2.static.flickr.com/1362/535093788_e18bc48644_b.jpg","mi pagina de fotos");
		
		System.out.println(codigo);
		
		
	}

}
