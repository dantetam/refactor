package level;

public class Grid {

	private Tile[][] tiles;
	
	public Grid(int[][] terrain)
	{
		tiles = new Tile[terrain.length][terrain[0].length];
		for (int r = 0; r < tiles.length; r++)
		{
			for (int c = 0; c < tiles[0].length; c++)
			{
				tiles[r][c] = new Tile();
				tiles[r][c].row = r; tiles[r][c].col = c;
				tiles[r][c].biome = terrain[r][c];
			}
		}
	}
	
	public Tile getTile(int r, int c)
	{
		if (r >= 0 && r < tiles.length && c >= 0 && c < tiles[0].length)
		{
			return tiles[r][c];
		}
		return null;
	}
	
	public int rows() {return tiles.length;}
	public int cols() {return tiles[0].length;}
	
}
