package buscadores;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.PhotoList;

public interface IBuscadorImagenes extends Serializable{
	
	public PhotoList getListaFotos(String modoBusqueda,int pagina ,int imagenesPorPagina)throws IOException, SAXException, FlickrException;
	
	public List<Object> getListaFotosMap(String modoBusqueda,int pagina ,int imagenesPorPagina)throws IOException, SAXException, FlickrException;
	
}
