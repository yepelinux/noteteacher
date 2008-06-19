package pages.components;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipMaker {

		 @SuppressWarnings("unused")
		private void doZip(String filename, String zipfilename){
		        try {
		            byte[] buf = new byte[1024];
		            FileInputStream fis = new FileInputStream(filename);
		            fis.read(buf,0,buf.length);
		            
		            CRC32 crc = new CRC32();
		            ZipOutputStream s = new ZipOutputStream(
		                    (OutputStream)new FileOutputStream(zipfilename));
		            
		            s.setLevel(6);
		            
		            ZipEntry entry = new ZipEntry(filename);
		            entry.setSize((long)buf.length);
		            crc.reset();
		            crc.update(buf);
		            entry.setCrc( crc.getValue());
		            s.putNextEntry(entry);
		            s.write(buf, 0, buf.length);
		            s.finish();
		            s.close();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		 }
		 
		 
	public void doZip(List<String> filesToZip, String outPutFile){
		
	    // Create a buffer for reading the files
	    byte[] buf = new byte[1024];
	    
	    try {
	        // Create the ZIP file
	        String outFilename = outPutFile;
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
	    
	        // Compress the files
//	        for (int i=0; i<filenames.length; i++) {
	        for (String filename : filesToZip) {
	            FileInputStream in = new FileInputStream(filename);
	    
	            // Add ZIP entry to output stream.
	            out.putNextEntry(new ZipEntry(filename));
	    
	            // Transfer bytes from the file to the ZIP file
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	    
	            // Complete the entry
	            out.closeEntry();
	            in.close();
	        }
	        
	        // Complete the ZIP file
	        out.close();
	    } catch (IOException e) {
	    	
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] Arg){
		
		List<String> files = new ArrayList<String>();
		files.add("c:\\sqmdata00.sqm");
		files.add("c:\\drmHeader.bin");
		
		String out = "prueba.zip";
		
		ZipMaker maker = new ZipMaker();
		
		maker.doZip(files, out);
	}

	
}

