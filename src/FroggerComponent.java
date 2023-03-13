// FroggerComponent.java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.*;

/*
 *The main class for Frogger. Stores the state of the world,
 *draws it, handles the tick() and key() logic.
 *@autor: Mark Ofosu
 *@date: 11/30/2021
 */

public class FroggerComponent extends JComponent  {
	// Size of the game grid
	public static final int WIDTH = 20;
	public static final int HEIGHT = 7;
	

	// Initial pixel size for each grid square
	public static final int PIXELS = 50;
	public Row[] rows;

	// Image filenames for car, lily, and frog
	public static final String[] IMAGES = new String[] { "car.png", "lily.png" , "frog.png", };
	
	// Colors for ROAD, WATER, and DIRT
	public static final Color[] COLORS = new Color[] { Color.BLACK, Color.BLUE, Color.GRAY } ;
	
	// Codes to store what is in each square in the grid
	public static final int EMPTY = 0;
	public static final int CAR = 1;
	public static final int LILY = 2;
	
	boolean isDead = false;
	
	public HashMap<Integer, ArrayList<Point>> grid = new HashMap<>(); // Stores a table of objects with their coordinates
	
	// these are the grid coordinates, not pixels
	public int frogX = 0;
	public int frogY = 0;
	public boolean dead = false;
	
	private Image frogImage;
	private Image carImage;
	private Image lilyImage;
	private Image deadImage;
	
	public FroggerComponent () {
		this.rows = new Row[7];
		this.loadRows("world.txt");
		for (int i = 1; i < 6; i++) {
			this.grid.put(i, new ArrayList<>());
		}
		frogImage = readImage("frog.png"); // next slide
		
		carImage = readImage("car.png");
		
		lilyImage = readImage("lily.png");
		
		deadImage = readImage("dfrog.png");
		repaint( );
	}
	
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		int pixels = width / WIDTH;
		

		
		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 20; x++) {
				int rowtype = rows[Math.abs(y - 6)].rowType - 1;
				g.setColor(COLORS[rows[Math.abs(y - 6)].rowType - 1]);
				g.fillRect(x * pixels, y * pixels, pixels, pixels);
			}
		}
		
		for (int i = 1; i < 6; i++) {
			ArrayList<Point> points = grid.get(i);
			for (Point point : points) {
				if (rows[Math.abs(i - 6)].rowType == Row.WATER) {
					g.drawImage(lilyImage, point.x * pixels,
							pixels * Math.abs(point.y),
							pixels, pixels, null);
				} else {
					g.drawImage(carImage, point.x * pixels,
							pixels * Math.abs(point.y),
							pixels, pixels, null);
				}
			}
		}
		// draw dead from if isDead is true else draw a normal frog
		if (isDead) {
			g.drawImage(deadImage, this.frogX * pixels,
					pixels * Math.abs(this.frogY - 6),
					pixels, pixels, null);
		} else {
			g.drawImage(frogImage, this.frogX * pixels,
					pixels * Math.abs(this.frogY - 6),
					pixels, pixels, null);
		}
	}
	
	//ignore key commands if frog is dead
	public void key(int code) {
		if (isDead) {
			return;
		}
		
		int currentX = this.frogX;
		int currentY = this.frogY;
		switch(code) {
		case KeyEvent.VK_UP:
			currentY += 1;
			break;
		case KeyEvent.VK_LEFT:
			currentX -= 1;
			break;
		case KeyEvent.VK_RIGHT:
			currentX += 1;
			break;
		default:
			break;
		}
		
		
		if (currentX > -1 && currentX < 20 && currentY > -1 && currentY < 7) {
			this.frogX = currentX;
			this.frogY = currentY;
			drowned();
			repaint( );
		}
	}


	/*
	 Provided utility method to read in an Image object.
	 If the image cannot load, prints error output and returns null.
	 Uses Java standard ImageIO.read() method.
	*/
	private Image readImage(String filename) {
		Image image = null;
		try {
			image = ImageIO.read(new File(filename));
		}
		catch (IOException e) {
			System.out.println("Failed to load image '" + filename + "'");
			e.printStackTrace();
		}
		return(image);
	}
	
	
	private void loadRows(String filename) {
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			//Taking advantage of the fact that the file's numbers
			//correspond to the program constants
			this.rows[6] = new Row("dirt 0 0.0");
			for (int i = 1; i < 6; i++)
			{
				String line = in.readLine( );
				this.rows[i] = new Row(line);			
			}
			this.rows[0] = new Row("dirt 0 0.0");
		}
		catch (Exception e)
		{
			e.printStackTrace( );
				System.exit(1);
		}
	}
	
	// Check to see if frog is in water or bean hit by a car
	private void drowned() {  
		Row row = rows[frogY];
		if (row.rowType == Row.WATER) {
			ArrayList<Point> points = grid.get(frogY);
			for (Point point : points) {
				if (frogX == point.x) {
					return;
				}
			}
			
			isDead = true;
		}
	}
	
	// Move items to right according strike number.
	public void tick(int round) {  
		for (int i = 1; i < 6; i++) {
			Row row = rows[Math.abs(i - 6)];
			if (round % row.strike == 0) {

				ArrayList<Point> points = grid.get(i);
				Iterator iterator = points.iterator();
				while(iterator.hasNext()) {
					Point point = (Point) iterator.next();
					
					if (row.rowType == Row.WATER) { //if row is water
						if (frogX == point.x && frogY == point.y && !isDead) { //is frog occupying the same place and not dead?
							point.move(point.x + 1, point.y);
							if (point.x < WIDTH) {
								frogX = point.x;
								drowned();
							}
						} else {
							point.move(point.x + 1, point.y);
						}
					} else {
						if (frogX == point.x && frogY == point.y) {
							this.isDead = true;
						}
						point.move(point.x + 1, point.y);
					}
					// prevent frog from moving out of frame
					if (point.x >= WIDTH) {
						iterator.remove();
					}
				}
				// probability- using random float to add new objects.
				Random random = new Random();
				float odds = random.nextFloat();
				if (odds <= row.density) {
					points.add(new Point(0, i));
				}
			}
		}
		
		repaint( );
	}
	
}

