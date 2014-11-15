package entity;

import java.util.ArrayList;

import level.Tile;

public class Organism {

	public long id;
	public String name;
	public ArrayList<Entity> units;
	public Tile center;
	public ArrayList<Tile> queueTiles;
	public int action, maxAction;
	public float r,g,b;
	
	public Organism(String name)
	{
		id = System.currentTimeMillis()*(long)Math.random();
		this.name = name;
		units = new ArrayList<Entity>();
		queueTiles = new ArrayList<Tile>();
	}
	
	public Organism(Organism other)
	{
		id = (long)(System.currentTimeMillis()*Math.random());
		name = other.name;
		units = new ArrayList<Entity>();
		queueTiles = new ArrayList<Tile>();
		for (int i = 0; i < other.units.size(); i++)
		{
			Entity en = other.units.get(i);
			Entity newEn = new Entity(this,en.rDis,en.cDis,en.health,en.offensiveStr,en.defensiveStr,en.rangedStr);
			units.add(newEn);
		}
		maxAction = other.maxAction;
		action = maxAction;
	}
	
	public Entity addUnit(int rDis, int cDis, int h, int o, int d, int r)
	{
		Entity temp = new Entity(this,rDis,cDis,h,o,d,r);
		units.add(temp);
		return temp;
	}
	
	public void destroy(Entity en)
	{
		units.remove(en);
		en.owner = null;
		en.health = 0;
	}
	
	public boolean equals(Organism org)
	{
		return id == org.id;
	}
	
	public void color(float x, float y, float z)
	{
		r = x; g = y; b = z;
	}
	
}
