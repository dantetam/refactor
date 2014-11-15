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
	
	public Organism(String name)
	{
		id = System.currentTimeMillis()*(long)Math.random();
		this.name = name;
		units = new ArrayList<Entity>();
		queueTiles = new ArrayList<Tile>();
	}
	
	public Organism(Organism other)
	{
		id = System.currentTimeMillis()*(long)Math.random();
		name = other.name;
		units = new ArrayList<Entity>();
		queueTiles = new ArrayList<Tile>();
		for (int i = 0; i < other.units.size(); i++)
		{
			Entity en = other.units.get(i);
			Entity newEn = new Entity(this,en.rDis,en.cDis);
			units.add(newEn);
		}
		maxAction = other.maxAction;
		action = maxAction;
	}
	
	public void addUnit(int rDis, int cDis)
	{
		units.add(new Entity(this,rDis,cDis));
	}
	
	public boolean equals(Organism org)
	{
		return id == org.id;
	}
	
}
