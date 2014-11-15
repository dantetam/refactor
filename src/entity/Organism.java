package entity;

import java.util.ArrayList;

import level.Tile;

public class Organism {

	public ArrayList<Entity> units;
	public Tile center;
	
	public Organism()
	{
		units = new ArrayList<Entity>();
	}
	
	public Organism(Organism other)
	{
		units = new ArrayList<Entity>();
		for (int i = 0; i < other.units.size(); i++)
		{
			Entity en = other.units.get(i);
			Entity newEn = new Entity(this,en.rDis,en.cDis);
			units.add(newEn);
		}
	}
	
	public void addUnit(int rDis, int cDis)
	{
		units.add(new Entity(this,rDis,cDis));
	}
	
}
