package generadoresCodigo;

import config.ConfigApp;

public class GeneradorCodigoImagen implements IGeneradorCodigo{
	
	/**
	 *  GENERO EL CODIGO DE UNA IMAGEN PARA USAR EN UN FORO
	 *  
	 * @param ImagenUrl
	 * @return
	 */
	public String getCodigoImagen(String ImagenUrl){
		
		return "[URL=http://" + ConfigApp.URL_APP + "][IMG]" + ImagenUrl + "[/IMG][/URL]";
	}
	
	/**
	 * Testeo el generador de codigo
	 * 
	 * @param arg
	 */
	public static void main (String [] arg){
		
		GeneradorCodigoImagen generadorCodigoImagen = new GeneradorCodigoImagen();
		
		String codigo = generadorCodigoImagen.getCodigoImagen("http://farm2.static.flickr.com/1362/535093788_e18bc48644_b.jpg");
		
		System.out.println(codigo);
		
		
	}

}
