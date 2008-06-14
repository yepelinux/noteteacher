package buscadores;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;

import config.ConfigFlickr;

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
	}
