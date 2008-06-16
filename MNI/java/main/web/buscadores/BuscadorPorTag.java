package buscadores;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;

import config.ConfigFlickr;

@SuppressWarnings("serial")
public class BuscadorPorTag implements IBuscadorImagenes{

	Flickr flickr = new MyFlickr(ConfigFlickr.API_KEY);

	/**
	 *   Busqueda Por Tag 
	 */
	
	
	
	public PhotoList getListaFotos(String tag, int pagina, int imagenesPorPagina)
			throws IOException, SAXException, FlickrException {

		String[] tags = { tag };

		SearchParameters busquedaPorTag = new SearchParameters();
		busquedaPorTag.setTags(tags);
		PhotosInterface photosInterface = flickr.getPhotosInterface();

		/**
		 * 1er arg: argumentos de la busqueda
		 * 2do arg: cantidad de imagenes por busqueda
		 * 3er arg: Pagina
		 */

		PhotoList resultado = photosInterface.search(busquedaPorTag,
				imagenesPorPagina, pagina);

		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getListaFotosMap(String tag,	int pagina, int imagenesPorPagina) 
			throws IOException,	SAXException, FlickrException {
		
		PhotoList listaFotos = this.getListaFotos(tag, 1, imagenesPorPagina);
		List<Object> result = new ArrayList<Object >();
		
		
		Iterator<Photo> listaIterator = listaFotos.iterator();
		while (listaIterator.hasNext()) {
			Photo photo = (Photo) listaIterator.next();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", photo.getId());
			map.put("url", photo.getSmallUrl());
			map.put("description", photo.getDescription());
			result.add(map);
		}
		return result;
	}
	
	
	/**
	 * Testea la busqueda
	 * 
	 * @param Arg
	 * @throws FlickrException 
	 * @throws SAXException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] Arg){
		BuscadorPorTag buscadorPorTag = new BuscadorPorTag();
		
		/**
		 * Busca las primeras 10 imagnes de la primera pagina del tag "nirvana"
		 */
		PhotoList listaFotos;
		try {
			listaFotos = buscadorPorTag.getListaFotos("nirvana", 1, 10);
			Iterator<Photo> lista = listaFotos.iterator();
			while (lista.hasNext()) {
				Photo photo = (Photo) lista.next();
				System.out.println(photo.getLargeUrl());
				System.out.println(photo.getSmallUrl() + "\n");
				System.out.println(photo.getSmallUrl());
				System.out.println(photo.isPublicFlag());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("serial")
	private class MyFlickr extends Flickr implements Serializable{

		public MyFlickr(String apiKey) {
			super(apiKey );
		}
	}
}
