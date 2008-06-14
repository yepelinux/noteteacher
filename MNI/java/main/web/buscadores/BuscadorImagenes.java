package buscadores;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

public interface BuscadorImagenes {
	
	public PhotoList getListaFotos(String modoBusqueda,int pagina ,int imagenesPorPagina)throws IOException, SAXException, FlickrException;
	
}
