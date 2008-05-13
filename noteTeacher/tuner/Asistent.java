package tuner;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class Asistent extends Applet {

	private Map<String, Collection<String>> listSequences = new HashMap<String, Collection<String>>();
	private Map<String, Double> notes = new HashMap<String, Double>();

	private String currentNote;
	private Collection<String> currentSecuence = new ArrayList<String>();
	private static double FREC_UMBRAL = 5;

	// Rango de frecuencias en el que se muestrea
	double freqMin = 10.0;
	double freqMax = 500.0;

	/* -------------------------- */

	private TargetDataLine targetDataLine;
	private CaptureThread captureThread;
	private AudioFormat audioFormat;

	private Panel infoPanel;
	private Button closeButton;
	boolean bStopTuner = false;

	@Override
	public void start() {

		initNotes();
		initSequence();
		initApplet();

		audioFormat = new AudioFormat(8000.0F, 8, 1, true, false);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);

			startNoteSequence("level 2");
			stopNoteSecuence();

			startNoteSequence("level 1");

		} catch (Exception e) {
			repaint();
			System.out.println("Error : Unable to start acqusition -> " + e);
		}
	}

	/**
	 * Calcula la transformada rápida de Fourier a partir de una señal
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
		System.out.println("Asigno nota: " + note);
		currentNote = note;
	}

	/**
	 * Determina si la diferencia entre las frecuencias es menor a FREC_UMBRAL
	 * @param frecNote frecuencia ingresada
	 * @param currentFrec frecuencia correcta
	 * @return
	 */
	public boolean isTheNote(double frecNote, double currentFrec) {
		if (Math.abs(currentFrec - frecNote) < FREC_UMBRAL)
			return true;

		return false;
	}

	/**
	 * Obtiene las notas y sus frecuencias a partir de un XML (note-frequence.xml)
	 */
	private void initNotes() {
		Document doc = parse("config/note-frequence.xml");
		System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());

		// Obtengo las notas a partir del documento y las almaceno en un Map (notes)
		NodeList listOfNotes = doc.getElementsByTagName("note");
		System.out.println("Total of notes frequence: "	+ listOfNotes.getLength());

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
	public void initSequence() {
		String seqNameStr = null;

		Document doc = parse("config/sequence.xml");
		NodeList listOfSequences = doc.getElementsByTagName("sequence");
		System.out.println("Total of sequences : " + listOfSequences.getLength());

		for (int s = 0; s < listOfSequences.getLength(); s++) {
			Collection<String> noteSecuence = new ArrayList<String>();
			
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

			listSequences.put(seqNameStr, noteSecuence);
		}// end of for loop with s var
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
	 * Incializa la parte visual del applet
	 */
	public void initApplet() {

		AppletListener listener = new AppletListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		TextArea infoArea = new TextArea("Notes Teacher", 8, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
		closeButton = new Button("Cerrar");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == closeButton) {
					infoPanel.setVisible(false);
					repaint();
				}
			}
		});
		infoPanel = new Panel();
		
		infoPanel.setBackground(Color.lightGray);
		infoPanel.add(infoArea);
		infoPanel.setVisible(true);
		add(infoPanel);

	}

	/**
	 * Setea la secuencia determinada
	 * @param sequenceName
	 */
	public void setSequence(String sequenceName) {
		currentSecuence.addAll(listSequences.get(sequenceName));
	}

	/**
	 * Prepara el thread para "escuchar" las notas ingresadas por el musico
	 * @param sequence
	 */
	public void startNoteSequence(String sequence) {
		setSequence(sequence);

		captureThread = new CaptureThread();
		captureThread.start();
	}

	/**
	 * Deja de escuchar las notas del musico
	 */
	public void stopNoteSecuence() {
		captureThread.interrupt();
		currentSecuence.clear();
	}

	/**
	 * getter
	 * @return
	 */
	public Panel getInfoPanel() {
		return infoPanel;
	}

	/**
	 * Thread encargado de leer el input del usuario 
	 */
	public class CaptureThread extends Thread {
		@Override
		public void run() {
			try {

				int spectreSize = 2048 * 2 * 2 * 2 * 2;
				int sampleSize = 2048 * 2 * 2;
				double divi = 4 * 2 * (4096 / 4000);
				byte data[] = new byte[spectreSize];
				targetDataLine.start();
				double[] ar = new double[spectreSize];
				double[] ai = new double[spectreSize];

				Iterator<String> it = currentSecuence.iterator();
				selectNote(it.next());

				// Leo la entrada de sonido a un array
				while (targetDataLine.read(data, 0, sampleSize) > 0) {
					try {
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
						// Aplico la transformada rápida de Fourier
						computeFFT(1, spectreSize, ar, ai);

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
							System.out.println("frecuencia: " + maxIndex / divi);

							currentFrec = maxIndex / divi;
							frecNote = notes.get(currentNote);

							// Si es la nota correcta paso a la siguiente
							if (isTheNote(frecNote, currentFrec)) {
								System.out.println("nodo completo!");
								if (it.hasNext()) {
									selectNote(it.next());
								} else {
									System.out.println("Secuencia Completada!");
									return;
								}
							} else {
								System.out.println("Frecuencia nota: " + frecNote + " | Actual: " + currentFrec);
							}
							
						}

					} catch (Exception e2) {
						System.out.println(e2);
					}
					targetDataLine.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}