package level;

import java.util.ArrayList;

import data.Data;
import entity.Organism;

public class Grid {

	private Tile[][] tiles;
	public ArrayList<Organism> organisms;

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

		organisms = new ArrayList<Organism>();

		Organism plr = Data.getOrganism("Player");
		organisms.add(plr);
		Tile t = randomLand();
		moveCenterTo(plr, t.row, t.col);

		for (int i = 1; i < 10; i++)
		{
			Organism org = Data.getOrganism("Test");
			organisms.add(org);
			t = randomLand();
			moveCenterTo(org, t.row, t.col);
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
	
	public Tile randomLand()
	{
		int r,c;
		while (true)
		{
			r = (int)(Math.random()*tiles.length);
			c = (int)(Math.random()*tiles[0].length);
			if (getTile(r,c).biome != -1)
			{
				break;
			}
		}
		return getTile(r,c);
	}

	public int rows() {return tiles.length;}
	public int cols() {return tiles[0].length;}

	public void moveCenterTo(Organism organism, int r, int c)
	{
		if (getTile(r,c) != null)
		{
			organism.center = getTile(r,c);
		}
	}

}
