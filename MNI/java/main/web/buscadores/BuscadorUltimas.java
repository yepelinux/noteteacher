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
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;

import config.ConfigFlickr;

@SuppressWarnings("serial")
public class BuscadorUltimas implements IBuscadorImagenes{

	Flickr flickr = new Flickr(ConfigFlickr.API_KEY);

	public PhotoList getListaFotos(String arg0, int pagina, int imagenesPorPagina) throws IOException, SAXException, FlickrException {

		PhotosInterface photosInterface = flickr.getPhotosInterface();
        
        PhotoList resultado = photosInterface.getRecent(imagenesPorPagina, pagina);
        
		return resultado;
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
		
		BuscadorUltimas buscadorPorTag = new BuscadorUltimas();
	
		try {
			
			PhotoList listaFotos = buscadorPorTag.getListaFotos(null, 10, 10);
			Iterator<Photo> lista = listaFotos.iterator();
			while (lista.hasNext()) {
				Photo photo = (Photo) lista.next();
				System.out.println(photo.getLargeUrl());
			}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (FlickrException e) {
				e.printStackTrace();
			}
		}


	@SuppressWarnings("unchecked")
	public List<Object> getListaFotosMap(String ultimas, int pagina, int imagenesPorPagina) 
			throws IOException,	SAXException, FlickrException {
		PhotoList listaFotos = this.getListaFotos(ultimas, 1, imagenesPorPagina);
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
