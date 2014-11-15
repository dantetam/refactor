package entity;

import level.Tile;

public class Entity {

	public int rDis, cDis;
	public Organism owner;
	
	public int health; public boolean deathFlag = false;
	public float offensiveStr, defensiveStr, rangedStr;
	
	public long id;
	
	public Entity(Organism owner, int r, int c, int h, float o, float d, float ranged)
	{
		id = (long)(System.currentTimeMillis()*Math.random());
		this.owner = owner;
		rDis = r; cDis = c;
		health = h; 
		offensiveStr = o; defensiveStr = d; rangedStr = ranged;
	}
	
	public boolean sameLocation(Entity en)
	{
		return owner.center.row + rDis == en.owner.center.row + en.rDis &&
				owner.center.col + cDis == en.owner.center.col + en.cDis;
	}
	
	public String toString()
	{
		return "[" + (owner.center.row + rDis) + "," + (owner.center.col + cDis) + "]";
	}
	
	public boolean equals(Entity en)
	{
		return id == en.id;
	}
	
}
