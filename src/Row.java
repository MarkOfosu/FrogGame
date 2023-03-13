// Row.java
/*
 Encapsulates the overall information for a single Frogger row.
 Implements the Frogger window.
 *@autor: Mark Ofosu
 *@date: 11/30/2021
*/

public class Row {
	// Row types, returned by getType()
	public static final int ROAD = 1;
	public static final int WATER = 2;
	public static final int DIRT = 3;
	public int rowType;
	public int strike;
	public double density;
	
	
	//assign row type
	public Row(String info) {
		String[ ] data = info.split(" ");
		switch (data[0]) {
			case "road":
				this.rowType = ROAD;
				break;
			case "water":
				this.rowType = WATER;
				break;
			default:
				this.rowType = DIRT;
		}
		this.strike = Integer.parseInt(data[1]);
		this.density = Double.parseDouble(data[2]);
	}
	
		
	

}

