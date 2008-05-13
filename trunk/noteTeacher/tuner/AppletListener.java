package tuner;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class AppletListener implements MouseListener, MouseMotionListener {
	
	private Asistent asistent;
	
	public AppletListener(Asistent asistent) {
		this.asistent = asistent; 
	}
		
	public void mouseDragged(MouseEvent e) {
		// ignore
	} 

	public void mouseEntered(MouseEvent e) {
		// ignore
	} 

	public void mouseExited(MouseEvent e) {
		// ignore
	} 

	public void mousePressed(MouseEvent e) {
		// ignore
	} 

	public void mouseReleased(MouseEvent e) {
		// ignore
	} 


	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (x > 24 && y > 179 && x < 313 && y < 198) {
			asistent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			asistent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void mouseClicked(MouseEvent e) {
		// ignore
	}
}
