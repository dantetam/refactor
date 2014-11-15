package level;

public class Grid {

	public Tile[][] tiles;
	
	public Grid(int[][] terrain)
	{
		tiles = new Tile[terrain.length][terrain[0].length];
		for (int r = 0; r < tiles.length; r++)
		{
			for (int c = 0; c < tiles[0].length; c++)
			{
				tiles[r][c] = new Tile();
				tiles[r][c].row = r; tiles[r][c].col = c;
			}
		}
	}
	
}
