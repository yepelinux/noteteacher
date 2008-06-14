package buscadores;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;

import config.ConfigFlickr;

public class BuscadorUltimas implements BuscadorImagenes{

	Flickr flickr = new Flickr(ConfigFlickr.API_KEY);
	private int IMAGENES_POR_BUSQUEDA;
	private int pagina = 0;
	
	public BuscadorUltimas(int imagenes_por_busqueda) {
	
		IMAGENES_POR_BUSQUEDA = imagenes_por_busqueda;
	}
	

	public List<Photo> getListaFotos(String modoBusqueda) throws IOException, SAXException, FlickrException {

		

		
        // 
        SearchParameters busquedaPorTag = new SearchParameters();
        busquedaPorTag.setTagMode("nirvana");
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        
        PhotoList resultado = photosInterface.getRecent(1, 5);
        
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
	public static void main(String[] Arg) throws IOException, SAXException, FlickrException {
		
		
		BuscadorUltimas buscadorPorTag = new BuscadorUltimas(10);
	
		List<Photo> listaFotos = buscadorPorTag.getListaFotos("pepe");
	
		for(Photo foto : listaFotos){
			
			System.out.println(foto.getId());
		}
		

		
		
	}
	
	
	

}
