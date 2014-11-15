package level;

public class Tile {

	public int row, col;
	public int biome;
	
	public Tile()
	{
		
	}
	
	public boolean equals(Tile t)
	{
		return row == t.row && col == t.col;
	}
	
}
