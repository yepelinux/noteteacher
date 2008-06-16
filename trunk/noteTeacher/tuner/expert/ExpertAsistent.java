package tuner.expert;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ExpertAsistent extends JFrame {

	private Map<String, List<String>> sequences = new HashMap<String, List<String>>();
	private Map<String, Double> notes = new HashMap<String, Double>();

	private String currentNote;
	private List<String> currentSecuence = new ArrayList<String>();
	private int frecUmbral = 5;

	// Rango de frecuencias en el que se muestrea
	double freqMin = 10.0;
	double freqMax = 1000.0;
	int sampleSize = 2048 * 2 * 2;

	/* -------------------------- */

	private TargetDataLine targetDataLine;
	private CaptureThread captureThread;
	private AudioFormat audioFormat;

	private JComboBox sequenceCombo;
	private JLabel noteLabel;
	private JLabel noteFrecLabel;
	private JLabel readNoteFrecLabel;
	private Graficacion grafSonido;
	private Graficacion grafFourier;
	protected JButton startStop;
	protected boolean stopped = true;
	

	public ExpertAsistent() {
		super();

		setTitle("Assistant");
		setLookAndFeel();
		getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec("250dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec("250dlu"),
				FormFactory.RELATED_GAP_COLSPEC},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				new RowSpec("10dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				new RowSpec("40dlu"),
				FormFactory.DEFAULT_ROWSPEC, 
				new RowSpec("400"),
				FormFactory.DEFAULT_ROWSPEC,
				new RowSpec("23dlu")}));
		setSize(new Dimension(800, 600));
		setResizable(false);
		
		ExpertAppletListener listener = new ExpertAppletListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		// Combo secuencia
		final JLabel seqLabel = new JLabel();
		seqLabel.setText("Secuencia");
		getContentPane().add(seqLabel, new CellConstraints(2, 2, CellConstraints.DEFAULT, CellConstraints.BOTTOM));
		sequenceCombo = new JComboBox();
		getContentPane().add(sequenceCombo, new CellConstraints(2, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

		// Slider tolerancia
		final JLabel sliderLabel = new JLabel();
		sliderLabel.setText("Error máximo permitido (Hz)");
		getContentPane().add(sliderLabel, new CellConstraints(4, 2));

		final JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, 10, frecUmbral);
		slider.setOpaque(false);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
		        JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            frecUmbral = source.getValue();		            
		        }
			}
		});
		getContentPane().add(slider, new CellConstraints(4, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

		// Nota en la secuencia
		JPanel notePanel = getNotePanel();
		getContentPane().add(notePanel, new CellConstraints(2, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
		
		
		//Gráficos
		getContentPane().add(getGraficacionSonido(), new CellConstraints(2, 6, CellConstraints.DEFAULT, CellConstraints.FILL));
		getContentPane().add(getGraficacionFourier(), new CellConstraints(4, 6, CellConstraints.DEFAULT, CellConstraints.FILL));
		
		// Nota leída
		TitledBorder border = BorderFactory.createTitledBorder("Frecuencia leída");
		border.setTitleJustification(TitledBorder.CENTER);
		readNoteFrecLabel = new JLabel();
		readNoteFrecLabel.setBorder(border);
		readNoteFrecLabel.setHorizontalAlignment(SwingConstants.CENTER);
		readNoteFrecLabel.setFont(new Font("Arial", Font.BOLD, 20));
		getContentPane().add(readNoteFrecLabel, new CellConstraints(4, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
		
		// Botón
		startStop = new JButton();
		startStop.setText("Iniciar");
		startStop.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent event) {
				if(stopped) {
					startListening();
				} else {
					stopListening();
				}				
			}

			public void mouseEntered(MouseEvent arg0) {return;}
			public void mouseExited(MouseEvent arg0) {return;}
			public void mousePressed(MouseEvent arg0) {return;}
			public void mouseReleased(MouseEvent arg0) {return;}

		});
		getContentPane().add(startStop, new CellConstraints(2, 8, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

		// Inicializo los componentes principales
		initNotes();
		initSequences();
		initThread();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	private JPanel getNotePanel() {
		TitledBorder border = BorderFactory.createTitledBorder("Nota");
		border.setTitleJustification(TitledBorder.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new FormLayout(
			"33dlu, 38dlu",
			"23dlu"));
		panel.setBorder(border);
		panel.setOpaque(false);
		
		noteLabel = new JLabel();
		noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		noteLabel.setFont(new Font("Arial", Font.BOLD, 20));
		panel.add(noteLabel, new CellConstraints(1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT));
		noteFrecLabel = new JLabel();
		noteFrecLabel.setHorizontalAlignment(SwingConstants.CENTER);
		noteFrecLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		panel.add(noteFrecLabel, new CellConstraints(2, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT));
		return panel;
	}
	
	private Graficacion getGraficacionSonido() {
		   grafSonido = new Graficacion();
		   grafSonido.setDimensiones(400,400);
		   grafSonido.setSimetricoY(true);
		   grafSonido.setTitulo("Gráfica de la onda");
		   
		   grafSonido.setMinimoValorGraficable(10);
//		   grafSonido.setTopeMaxEjeY(20);
//		   grafSonido.setTopeMinEjeY(-20);
		   //2.- Insertamos el tamaño de la grafica en pixeles, insertar los Datos: 
//		   graf.setDimensiones(20000,20000); 
		   //graf.Datos(X,Y,200,200,"Grafica 1 X vs Y","X","Y");
		   
		   return grafSonido;

	}
	
	private Graficacion getGraficacionFourier() {
		   grafFourier = new Graficacion();
		   grafFourier.setDimensiones(400,400);
		   grafFourier.setSimetricoY(true);
		   grafFourier.setTitulo("Grafica de las Frecuencias");
//		   grafFourier.setTopeMaxEjeY(5);
//		   grafFourier.setTopeMinEjeY(-5);
//		   grafFourier.set
		   //2.- Insertamos el tamaño de la grafica en pixeles, insertar los Datos: 
//		   graf.setDimensiones(20000,20000); 
		   //graf.Datos(X,Y,200,200,"Grafica 1 X vs Y","X","Y");
		   
		   return grafFourier;

	}
	
	private void fillGraficacionSonido(double[] Y) {
		double[] X = new double[Y.length];
		for (int i = 1; i < sampleSize; i++) {
			X[i] = i;
		}
		
		
		grafSonido.reset();
		grafSonido.Datos(X, Y, 400, 400, "Grafica de la onda", "Tiempo", "Onda");
		grafSonido.repaint();

	}
	
	private void fillGraficacionFourier(double[] Y) {
		double[] X = new double[Y.length];
		for (int i = 1; i < Y.length; i++) {
			X[i] = i;
		}
		
		grafFourier.reset();
		grafFourier.Datos(X, Y, 400, 400, "Grafica de las Frecuencias", "Frecuencia", "Amplitud");
		grafFourier.repaint();

	}

	private void startListening() {
		sequenceCombo.setEnabled(false);
		startStop.setText("Detener");
		String seq = (String) sequenceCombo.getSelectedItem();
		startNoteSequence(seq);
		if(captureThread.getState().equals(Thread.State.TERMINATED)) {
			initThread();
		}
		if(!captureThread.isAlive()) {			
			captureThread.start();
		}

	}
	
	private void stopListening() {
		sequenceCombo.setEnabled(true);
		startStop.setText("Iniciar");
		stopNoteSecuence();
	}
	
	private void initThread() {
		audioFormat = new AudioFormat(8000.0F, 8, 1, true, false);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
		} catch (LineUnavailableException e) {						
			System.out.println("No se puede leer de la línea de audio");
			e.printStackTrace();
		}

		captureThread = new CaptureThread();
	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Calcula la transformada rápida de Fourier a partir de una señal
	 * 
	 * @param sign
	 * @param n
	 * @param ar La señal
	 * @param ai
	 */
	public static void computeFFT(int sign, int n, double[] ar, double[] ai) {
		double scale = 2.0 / n;
		int i, j;
		for (i = j = 0; i < n; ++i) {
			if (j >= i) {
				double tempr = ar[j] * scale;
				double tempi = ai[j] * scale;
				ar[j] = ar[i] * scale;
				ai[j] = ai[i] * scale;
				ar[i] = tempr;
				ai[i] = tempi;
			}
			int m = n / 2;
			while ((m >= 1) && (j >= m)) {
				j -= m;
				m /= 2;
			}
			j += m;
		}
		int mmax, istep;
		for (mmax = 1, istep = 2 * mmax; mmax < n; mmax = istep, istep = 2 * mmax) {
			double delta = sign * Math.PI / mmax;
			for (int m = 0; m < mmax; ++m) {
				double w = m * delta;
				double wr = Math.cos(w);
				double wi = Math.sin(w);
				for (i = m; i < n; i += istep) {
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

	/**
	 * Asigna una nota
	 * @param note
	 */
	public void selectNote(String note) {
		currentNote = note;
		noteLabel.setText(note);
		noteFrecLabel.setText("(" + notes.get(note) + " Hz)");
	}

	/**
	 * Determina si la diferencia entre las frecuencias es menor a FREC_UMBRAL
	 * @param frecNote frecuencia ingresada
	 * @param currentFrec frecuencia correcta
	 * @return
	 */
	public boolean isTheNote(double frecNote, double currentFrec) {
		if (Math.abs(currentFrec - frecNote) < frecUmbral)
			return true;

		return false;
	}

	/**
	 * Obtiene las notas y sus frecuencias a partir de un XML (note-frequence.xml)
	 */
	private void initNotes() {
		Document doc = parse("config/note-frequence.xml");

		// Obtengo las notas a partir del documento y las almaceno en un Map (notes)
		NodeList listOfNotes = doc.getElementsByTagName("note");

		for (int s = 0; s < listOfNotes.getLength(); s++) {

			Node firstPersonNode = listOfNotes.item(s);
			if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

				Element firstPersonElement = (Element) firstPersonNode;
				// Obtengo el nombre de la nota
				NodeList nameNote = firstPersonElement.getElementsByTagName("nameNote");
				Element nameNoteElement = (Element) nameNote.item(0);

				// Obtengo la frecuencia
				NodeList frequence = firstPersonElement.getElementsByTagName("frequence");
				Element frequenceElement = (Element) frequence.item(0);

				notes.put(nameNoteElement.getChildNodes().item(0).getNodeValue().trim(), 
						Double.valueOf(frequenceElement.getChildNodes().item(0).getNodeValue()));

			}// end of if clause

		}// end of for loop with s var
	}

	/**
	 * Obtiene las secuencias de notas para mostrar al usuario de un xml (sequence.xml) 
	 */
	public void initSequences() {
		String seqNameStr = null;

		Document doc = parse("config/sequence.xml");
		NodeList listOfSequences = doc.getElementsByTagName("sequence");

		for (int s = 0; s < listOfSequences.getLength(); s++) {
			List<String> noteSecuence = new ArrayList<String>();
			
			Node firstSecuenceNode = listOfSequences.item(s);
			if (firstSecuenceNode.getNodeType() == Node.ELEMENT_NODE) {

				Element firstSecuenceElement = (Element) firstSecuenceNode;

				// Obtengo el nombre de la secuencia
				NodeList nameSequence = firstSecuenceElement.getElementsByTagName("name");
				Element nameSequenceElement = (Element) nameSequence.item(0);
				
				seqNameStr = nameSequenceElement.getChildNodes().item(0).getNodeValue().trim();

				// Obtengo las notas
				NodeList notesSequence = firstSecuenceElement.getElementsByTagName("note");
				for (int t = 0; t < notesSequence.getLength(); t++) {
					Node firstNoteSequenceNode = notesSequence.item(t);

					if (firstNoteSequenceNode.getNodeType() == Node.ELEMENT_NODE) {
						Element firstNoteElement = (Element) firstNoteSequenceNode;
						noteSecuence.add(firstNoteElement.getChildNodes().item(0).getNodeValue().trim());
					}
				}
				
			}// end of if clause

			sequences.put(seqNameStr, noteSecuence);
			sequenceCombo.addItem(seqNameStr);
		}// end of for loop with s var
		
		sequenceCombo.setSelectedIndex(0);
	}

	/**
	 * Obtiene un Document a partir de un XML
	 * @param filename la ruta del XML
	 * @return el objeto Document
	 */
	private Document parse(String filename) {
		try {
			// Parseo el XML con las frecuencias de las notas.
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(filename));

			// normalize text representation
			doc.getDocumentElement().normalize();

			return doc;
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * Setea la secuencia determinada
	 * @param sequenceName
	 */
	public void setSequence(String sequenceName) {
		currentSecuence.addAll(sequences.get(sequenceName));
	}

	/**
	 * Prepara el thread para "escuchar" las notas ingresadas por el musico
	 * @param sequence
	 */
	public void startNoteSequence(String sequence) {
		setSequence(sequence);
		stopped = false;
	}

	/**
	 * Deja de escuchar las notas del musico
	 */
	public void stopNoteSecuence() {		
		stopped = true;
		currentSecuence.clear();
	}

	/**
	 * Thread encargado de leer el input del usuario 
	 */
	public class CaptureThread extends Thread {
		@Override
		public void run() {
			int spectreSize = 2048 * 2 * 2 * 2 * 2;
			double divi = 4 * 2 * (4096 / 4000);
			byte data[] = new byte[spectreSize];
			targetDataLine.start();
			double[] ar = new double[spectreSize];
			double[] ai = new double[spectreSize];

			Iterator<String> it = currentSecuence.iterator();
			selectNote(it.next());

			// Leo la entrada de sonido a un array
			while (targetDataLine.read(data, 0, sampleSize) > 0) {
				if(!stopped) {
					// 
					for (int i = 0; i < sampleSize; i++) {
						ar[i] = data[i];
						ai[i] = 0.0;
					}
					// Completo el espectro con ceros
					for (int i = sampleSize; i < spectreSize; i++) {
						ar[i] = 0.0;
						ai[i] = 0.0;
					}
					
					//grafico
					fillGraficacionSonido(ar);
					
					// Aplico la transformada rápida de Fourier
					computeFFT(1, spectreSize, ar, ai);
	
					//grafico
					fillGraficacionFourier(ai);
					
					// Determino la amplitud de la componente principal
					double maxAmpl = 0;
					double maxIndex = 0;
					for (int i = (int) (freqMin * divi); i < (freqMax * divi); i++) {
						if (Math.abs(ai[i]) > maxAmpl) {
							maxAmpl = Math.abs(ai[i]);
							maxIndex = i;
						}
					}
	
					double frecNote = 0;
					double currentFrec = 0;
					// Supere cierto umbral de volumen
					if (maxAmpl > 0.01) {
						currentFrec = maxIndex / divi;
						readNoteFrecLabel.setText(""+currentFrec);
						frecNote = notes.get(currentNote);
	
						// Si es la nota correcta paso a la siguiente
						if (isTheNote(frecNote, currentFrec)) {
							readNoteFrecLabel.setForeground(Color.GREEN);
							
							if (it.hasNext()) {
								selectNote(it.next());
							} else {
								stopListening();
								return;
							}
						} else {
							readNoteFrecLabel.setForeground(Color.RED);
						}
						
					}
					targetDataLine.flush();
				} else {
					return;	
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		ExpertAsistent a = new ExpertAsistent();		
		
	   double[] Y = {0,2,1.0};
	   double[] X = {1,2,3};
	   Graficacion graf = new Graficacion();
	   //2.- Insertamos el tamaño de la grafica en pixeles, insertar los Datos: 
	   graf.setDimensiones(200,200); 
	   graf.Datos(X,Y,200,200,"Grafica 1 X vs Y","X","Y");
//	   a.getFrames()[1].add(graf).validate();
	   a.getContentPane().add(graf, new CellConstraints(5, 6, CellConstraints.FILL, CellConstraints.DEFAULT));
	   a.validate();
	   a.repaint();
	   
//	   Frame f = getFrame();
//	   f.add(graf);
//	   f.validate();
//	   f.repaint();
		   
		   


	}
}