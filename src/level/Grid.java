package level;

import java.util.ArrayList;

import level.ConflictSystem;
import data.Data;
import entity.Entity;
import entity.Organism;

public class Grid {

	private Tile[][] tiles;
	public ArrayList<Organism> organisms;

	public ConflictSystem conflictSystem;
	
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

		for (int i = 1; i < 15; i++)
		{
			Organism org = Data.getOrganism("Test");
			organisms.add(org);
			t = randomLand();
			moveCenterTo(org, t.row, t.col);
		}
		
		conflictSystem = new ConflictSystem(this);
	}

	public Entity[] valid(Organism a, int r, int c)
	{
		for (int i = 0; i < organisms.size(); i++)
		{
			Organism b = organisms.get(i);
			//System.out.println(a.id + " " + b.id);
			if (!a.equals(b))
			{
				Entity[] possible = intersectInFuture(a,b,r,c);
				if (possible != null)
					return possible;
			}
		}
		return null;
	}
	
	//Checks to see if a hypothetical move by organism a would intersect organism b's territory
	private Entity[] intersectInFuture(Organism a, Organism b, int r, int c)
	{
		ArrayList<Entity> aFuture = new ArrayList<Entity>();
		ArrayList<Entity> bFuture = new ArrayList<Entity>();
		for (int i = 0; i < a.units.size(); i++)
		{
			Entity u = a.units.get(i);
			//aFuture.add(getTile(r+u.rDis,c+u.cDis));
			aFuture.add(u);
		}
		for (int i = 0; i < b.units.size(); i++)
		{
			Entity u = b.units.get(i);
			//aFuture.add(getTile(b.center.row+u.rDis,b.center.col+u.cDis));
			bFuture.add(u);
		}
		return intersect(aFuture, bFuture);
	}
	
	//Checks to see if two lists have an intersection
	private Entity[] intersect(ArrayList<Entity> a, ArrayList<Entity> b)
	{
		for (int i = a.size() - 1; i >= 0; i--)
		{
			for (int j = 0; j < b.size(); j++)
			{
				//if (a.get(i) == null || b.get(i) == null) continue;
				if (a.get(i).sameLocation(b.get(j)))
				{
					return new Entity[]{a.get(i),b.get(j)};
				}
			}
			//a.remove(i);
		}
		return null;
	}
	
	public Tile getTile(int r, int c)
	{
		if (r >= 0 && r < tiles.length && c >= 0 && c < tiles[0].length)
		{
			return tiles[r][c];
		}
		return null;
	}
	
	public Entity findEntity(int r, int c)
	{
		for (int i = 0; i < organisms.size(); i++)
		{
			Organism org = organisms.get(i);
			for (int j = 0; j < org.units.size(); j++)
			{
				Entity u = org.units.get(j);
				if (org.center.row + u.rDis == r && org.center.col + u.cDis == c)
				{
					return u;
				}
			}
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
