package buscadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;

import config.ConfigFlickr;

/**
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
@SuppressWarnings("serial")
public class BuscadorPorNombre implements IBuscadorImagenes{
    
	Flickr flickr = new Flickr(ConfigFlickr.API_KEY);
    
    /**
     *   Busqueda Por nombre de usuario 
     */
	
    public PhotoList getListaFotos(String nombreUsuario, int pagina, int imagenesPorPagina) throws IOException, SAXException, FlickrException{
    	
		User user = flickr.getPeopleInterface().findByUsername(nombreUsuario);
		SearchParameters usernameSearch = new SearchParameters();
		usernameSearch.setUserId(user.getId());
		
		PhotosInterface photosInterface = flickr.getPhotosInterface();
		
		/**
		 * 1er arg: argumentos de la busqueda
		 * 2do arg: cantidad de imagenes por busqueda
		 * 3er arg: Pagina
		 */
		
		PhotoList resultado = photosInterface.search(usernameSearch, imagenesPorPagina, pagina);
		
		return resultado;
    }
    
	/**
	 * Testea la busqueda
	 * 
	 * @param Arg
	 */
    
	@SuppressWarnings("unchecked")
	public static void main(String[] Arg){
	
		IBuscadorImagenes buscadorPorNombre = new BuscadorPorNombre();
		
	/**
	 * Busca las primeras 10 imagenes de la primera pagina del usuario "pepe"
	 */
	
	try {
		
		PhotoList listaFotos = buscadorPorNombre.getListaFotos("pepe", 1, 10);
		Iterator<Photo> lista = listaFotos.iterator();
		while (lista.hasNext()) {
			Photo photo = (Photo) lista.next();
			System.out.println(photo.getLargeUrl());
		}
		} catch (IOException e) {			
			System.out.println(e.getMessage());
		} catch (SAXException e) {			
			System.out.println(e.getMessage());
		} catch (FlickrException e) {			
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getListaFotosMap(String id, int pagina, int imagenesPorPagina) 
		throws IOException,	SAXException, FlickrException {
		
		PhotoList listaFotos = this.getListaFotos(id, 1, imagenesPorPagina);
		List<Object> result = new ArrayList<Object >();
		
		Iterator<Photo> listaIterator = listaFotos.iterator();
		while (listaIterator.hasNext()) {
			Photo photo = (Photo) listaIterator.next();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", photo.getId());
			map.put("url", photo.getSmallUrl());
			map.put("largeUrl", photo.getLargeUrl());
			map.put("description", photo.getDescription());
			map.put("title", photo.getTitle() );
			result.add(map);
		}
		return result;
	}
	
}