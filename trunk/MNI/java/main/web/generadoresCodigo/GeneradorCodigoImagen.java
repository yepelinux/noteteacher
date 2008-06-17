package generadoresCodigo;

import java.io.IOException;
import java.util.HashMap;

import org.xml.sax.SAXException;

import buscadores.BuscadorPorTag;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

import config.ConfigApp;

public class GeneradorCodigoImagen implements IGeneradorCodigo{
	
	/**
	 *  GENERO EL CODIGO DE UNA IMAGEN PARA USAR COMO AVATAR EN UN FORO
	 * 
	 * @param foto
	 * @param alto
	 * @param ancho
	 * @return
	 */
	public String getCodigoImagen(Photo foto, int alto, int ancho){
		
		return "<a href=\"http://" + ConfigApp.URL_APP + "\"><img height=\"" + alto + "\" width=\""+ ancho + "\" src=\"" + foto.getSmallUrl() + "\" Alt=\"MegaPhotos\"></a>";
		
	}
	
	/**
	 * Testeo el generador de codigo
	 * 
	 * @param arg
	 */
	public static void main (String [] arg){
		
		GeneradorCodigoImagen generadorCodigoImagen = new GeneradorCodigoImagen();
		
		/**
		 * testeo el generador de paginas buscando imagenes.
		 */
		
		try {
			
			/**
			 *  Tomo la primer imagen de una lista de fotos, y pruebo la generacion 
			 *  del codigo de esa imagen
			 */
			BuscadorPorTag buscadorPorTag = new BuscadorPorTag();
			PhotoList listaFotos = buscadorPorTag.getListaFotos("silverchair", 1, 9);
			
			Photo foto = (Photo)listaFotos.get(1);
			
			String codigo = generadorCodigoImagen.getCodigoImagen(foto, 200, 150);			
			System.out.println(codigo);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getCodigoImagen(HashMap<String, Object> foto, int alto, int ancho){
		
		return "<a href=\"http://" + ConfigApp.URL_APP + "\"><img height=\"" + alto + "\" width=\""+ ancho + "\" src=\"" + foto.get("url") + "\" Alt=\"MegaPhotos\"></a>";
		
	}


}
