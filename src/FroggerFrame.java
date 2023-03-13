// FroggerFrame.java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 *Implements the Frogger window.
 *@autor: Mark Ofosu
 *@date: 11/30/2021
 */

class FroggerFrame extends JFrame implements KeyListener, ActionListener  {
	public static final int DELAY = 400;  // milliseconds
	private javax.swing.Timer timer; // the timer object
	FroggerComponent froggerComponent;
	int round = 1;
	
	//set up game frame
	public FroggerFrame() {
		setTitle("Frogger");
		
		Container container = getContentPane( );
		container.setLayout(new BorderLayout( ));
		
		froggerComponent = new FroggerComponent();
		container.add(froggerComponent, BorderLayout.CENTER);
		froggerComponent.addKeyListener(this); // adding the KeyListener
		froggerComponent.setFocusable(true); // setting focusable to true
		
		JPanel panel = new JPanel( );
		container.add(panel, BorderLayout.SOUTH);
		
		timer = new javax.swing.Timer(DELAY, this);
		timer.start( );
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		froggerComponent.requestFocusInWindow( );
		pack( );
		setVisible(true);
	}
	
		
		
	
	
	public static void main(String[] args) {
		FroggerFrame frame = new FroggerFrame();
		
	}





	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource( ).equals(timer)) {
			froggerComponent.tick(round);
			round += 1;
		}	
	}





	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		froggerComponent.key(e.getKeyCode( ));
	}





	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}





	


	
}
