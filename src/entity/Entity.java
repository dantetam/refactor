package entity;

import level.Tile;

public class Entity {

	public int rDis, cDis;
	public Organism owner;
	
	public Entity(Organism owner, int r, int c)
	{
		this.owner = owner;
		rDis = r; cDis = c;
	}
	
}
