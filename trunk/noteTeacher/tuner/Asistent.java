package tuner;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Asistent extends Applet implements MouseListener, ActionListener, MouseMotionListener
{  
	

	private Map<String,Collection> listSequences = new HashMap<String, Collection>();
	private Map<String,Double> notes = new HashMap<String,Double>();
	
	private String currentNote;
	private Collection currentSecuence = new ArrayList();
	private static double FREC_UMBRAL = 5;
	
	
	//Rango de frecuencias en el que se muestrea
	double freqMin = 10.0;
	double freqMax = 500.0;
	
	/* --------------------------*/
	
	Image img;
	Image imgE;
	Image imgA;
	Image imgD;
	Image imgG;
	Image imgB;
	Image imgSelected = null;
    	
	TargetDataLine targetDataLine;
	CaptureThread captureThread;
	AudioFormat audioFormat;
	


	int coordSelectedx = 70;
	int coordSelectedy = 134;
	int xPos = 171;
	int yPos = 98-40;

	int dispError = 0;
	Panel infoPanel = new Panel();
	Button closeButton = new Button("Close");
	boolean bStopTuner = false;

 	public void start ()
	{   
 		
 		initNotes();
 		initSequence();
 		initApplet();
 		
 		setSequence("level 2");

 		audioFormat = new AudioFormat(8000.0F,8,1,true,false);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class,audioFormat);
 		
		try
		{
			targetDataLine = (TargetDataLine)AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			
			captureThread = new CaptureThread();
			captureThread.start();
		
		}
		catch (Exception e2) 
		{
      			dispError = 1;
			repaint();
			System.out.println("Error : Unable to start acqusition -> "+e2);
    		}
	}


	public class CaptureThread extends Thread
	{			
	  	public void run()
		{
	    		try
			{
    			
			int cnt2 = 0;
			int spectreSize = 2048*2*2*2*2;
			int sampleSize = 2048*2*2;
			double divi =  4*2*(4096/4000);
			byte data[] = new byte[spectreSize];
			int valtemp = 0;
 			targetDataLine.start();
			int nbValues = 0;
			double tempValue = 0;
			int nbMesures = 1;
			double[] ar = new double[spectreSize];
      		double[] ai = new double[spectreSize];
      		
			Iterator it = currentSecuence.iterator();
			
			selectNote((String)it.next());
      			
      		while(((cnt2 = targetDataLine.read(data,0,sampleSize)) > 0))
			{					
				try
				{
 					for(int i = 0; i < sampleSize; i++)
      					{
         					ar[i] = (double)data[i];
         					ai[i] = 0.0;
      					}
					for (int i=sampleSize; i<spectreSize; i++)
					{
						ar[i] = 0.0;
         					ai[i] = 0.0;
      					}
					computeFFT(1, spectreSize, ar, ai);
      					
					double maxFreq = 0;
					double maxAmpl = 0;
					double maxIndex = 0;
					double erreur = 0;
					
					for (int i=(int)(freqMin*divi); i<(freqMax*divi);i++)
					{	
						if (Math.abs(ai[i]) > maxAmpl)
						{
							maxFreq = ar[i];
							maxAmpl = Math.abs(ai[i]);
							maxIndex = i;
							
						}
						
					}
					
					
					
					double frecNote = 0; 
					double currentFrec = 0;
					
					//Supere cierto umbrar de volumen
					if(maxAmpl > 0.01)
					{
					
						System.out.println("frecuencia: " + maxIndex/divi);
						
						currentFrec = maxIndex/divi;
						frecNote = notes.get(currentNote);
						
						if(isTheNote(frecNote,currentFrec)){
							System.out.println("nodo completo!");
							if(it.hasNext()){
								selectNote((String)it.next());
							}
							else{
								System.out.println("Secuencia Completada!");
								return;
							}
						}
					}
					
					
   				
					
   							
				}
				catch (Exception e2)
				{	
					System.out.println(e2);
      					
				}
				
				targetDataLine.flush();
      			}
	      		

    		}
		catch (Exception e) 
		{
      			System.out.println(e);
      			System.exit(0);
    		}		
	  	}
		
	}
   

	public static void computeFFT(int sign, int n, double[] ar, double[] ai)
	 {
      double scale = 2.0 / (double)n;
      int i, j;
      for(i = j = 0; i < n; ++i)
      {
         if (j >= i)
         {
            double tempr = ar[j] * scale;
            double tempi = ai[j] * scale;
            ar[j] = ar[i] * scale;
            ai[j] = ai[i] * scale;
            ar[i] = tempr;
            ai[i] = tempi;
         }
         int m = n/2;
         while ((m >= 1) && (j >= m))
         {
            j -= m;
            m /= 2;
         }
         j += m;
      }
      int mmax, istep;
      for(mmax = 1, istep = 2 * mmax; mmax < n; mmax = istep, istep = 2 * mmax)
      {
         double delta = sign * Math.PI / (double)mmax;
         for(int m = 0; m < mmax; ++m)
         {
            double w = m * delta;
            double wr = Math.cos(w);
            double wi = Math.sin(w);
            for(i = m; i < n; i += istep)
            {
               j = i + mmax;
               double tr = wr * ar[j] - wi * ai[j];
               double ti = wr * ai[j] + wi * ar[j];
               ar[j] = ar[i] - tr;
               ai[j] = ai[i] - ti;
               ar[i] += tr;
               ai[i] += ti;
            }
         }
         mmax = istep;
      }
   }

	
	public void paint(Graphics g) 
	{
          
		if (img!=null)
		{
			g.drawImage(img,0,0,350, 200,this);
			if (imgSelected!=null)
			{
				g.drawImage(imgSelected,coordSelectedx,coordSelectedy,this);
				
			}
				g.setColor(Color.black);
			g.drawLine(xPos, yPos, 171, 98);
		}
		else
		{
			g.drawString("Loading...",20,60);
		}
		if (dispError == 1)
		{
			g.setColor(Color.black);
			g.drawString("Error : Unable to start acquisition",86,124);
		}
	}

 	public void mouseClicked(MouseEvent e) 
	{
     	int x = e.getX();
		int y = e.getY();
	}
 
	public void mouseMoved(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
		if (x > 24 && y > 179 && x < 313 && y < 198)
		{
			
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
		}
	}
   
    public void mouseDragged (MouseEvent e) {}  // ignore
    public void mouseEntered (MouseEvent e) {}  // ignore
    public void mouseExited  (MouseEvent e) {}  // ignore
    public void mousePressed (MouseEvent e) {}  // ignore
    public void mouseReleased(MouseEvent e) {}  // ignore


	public void actionPerformed(ActionEvent evt) 
    	{		
    		if (evt.getSource()==closeButton)
		{
			infoPanel.setVisible(false);
			repaint();
		}
	}
	
	
	public void selectNote(String note){
		
		System.out.println("asigno nota !" + note);
		
		currentNote = note;
		
	}
	
	public boolean isTheNote(double frecNote,double currentFrec){
		
		if(Math.abs(currentFrec - frecNote) < FREC_UMBRAL)
			return true;
		
		return false;
	}
	
	public void initNotes(){
		
		try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("config/note-frequence.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ("Root element of the doc is " + 
                 doc.getDocumentElement().getNodeName());

            //------            
            
            
            NodeList listOfNotes = doc.getElementsByTagName("note");
            System.out.println("Total of notes frequence: " + listOfNotes.getLength());

            for(int s=0; s<listOfNotes.getLength() ; s++){


                Node firstPersonNode = listOfNotes.item(s);
                if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element)firstPersonNode;

                    //-------
                    NodeList nameNote = firstPersonElement.getElementsByTagName("nameNote");
                    Element nameNoteElement = (Element)nameNote.item(0);
                    
                    //-------
                    NodeList frequence = firstPersonElement.getElementsByTagName("frequence");
                    Element frequenceElement = (Element)frequence.item(0);
                    //------

                    notes.put(nameNoteElement.getChildNodes().item(0).getNodeValue().trim(), Double.valueOf(frequenceElement.getChildNodes().item(0).getNodeValue()));
                    
                }//end of if clause


            }//end of for loop with s var

            

        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
	}
	
	
	
	public void initSequence(){
		
		String nameSecuence = null;
		Collection noteSecuence = new ArrayList<String>();
		
		try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("config/sequence.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            //------            
            
            
            NodeList listOfSequences = doc.getElementsByTagName("sequence");
            System.out.println("Total of sequences : " + listOfSequences.getLength());

            for(int s=0; s<listOfSequences.getLength() ; s++){


                Node firstSecuenceNode = listOfSequences.item(s);
                if(firstSecuenceNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstSecuenceElement = (Element)firstSecuenceNode;

                    //-------
                    NodeList nameSequence = firstSecuenceElement.getElementsByTagName("name");
                    Element nameSequenceElement = (Element)nameSequence.item(0);
                    
                    nameSecuence = nameSequenceElement.getChildNodes().item(0).getNodeValue().trim();
                    
                    //-------
                    NodeList notesSequence = firstSecuenceElement.getElementsByTagName("note");
                    
                    for(int t=0; t<notesSequence.getLength() ; t++){
                    	
                        Node firstNoteSequenceNode = notesSequence.item(t);
                        
                        if(firstNoteSequenceNode.getNodeType() == Node.ELEMENT_NODE){
                        	
                        	Element firstNoteElement = (Element)firstNoteSequenceNode;
                            
                            noteSecuence.add(firstNoteElement.getChildNodes().item(0).getNodeValue().trim());
                        	
                        }
                    }    
                }//end of if clause
                
                listSequences.put(nameSecuence, noteSecuence);
                noteSecuence = new ArrayList<String>();

            }//end of for loop with s var
            
        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
		
		
	}
	
	public void initApplet(){
		
	    img = getImage(getCodeBase(),"images/backGround.jpg");

		this.addMouseListener(this);
		this.addMouseMotionListener(this);		

		infoPanel.setLayout(new BorderLayout());

		Panel buttonPanel = new Panel();
		Label titleLabel = new Label("GuitarTuner");



		TextArea infoArea = new TextArea("Notes Teacher\n", 8, 40, TextArea.SCROLLBARS_VERTICAL_ONLY );
		infoArea.setBackground(Color.lightGray);
		buttonPanel.add(closeButton);
		buttonPanel.setBackground(Color.lightGray);
		closeButton.addActionListener(this);
		//infoPanel.add(titleLabel);
		infoPanel.setBackground(Color.lightGray);
		infoPanel.add(infoArea);
		infoPanel.add(buttonPanel, BorderLayout.SOUTH);
		infoPanel.setVisible(false);
		add(infoPanel);
		
	}
	
	public void setSequence(String sequenceName){
		
		currentSecuence.addAll(listSequences.get(sequenceName));
		
	}
}
	

