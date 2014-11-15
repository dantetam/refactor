package entity;

import level.Tile;

public class Entity {

	public int rDis, cDis;
	public Organism owner;
	
	public float health, maxHealth; public boolean deathFlag = false;
	public float offensiveStr, defensiveStr, rangedStr; public int range;
	
	public long id;
	
	public Entity(Organism owner, int r, int c, float h, float o, float d, float ranged)
	{
		id = (long)(System.currentTimeMillis()*Math.random());
		this.owner = owner;
		rDis = r; cDis = c;
		health = h; maxHealth = h;
		offensiveStr = o; defensiveStr = d; rangedStr = ranged;
	}
	
	public int trueRow() {return rDis + owner.center.row;}
	public int trueCol() {return cDis + owner.center.col;}
	
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
		if (en == null) return false;
		return id == en.id;
	}
	
	public Entity range(int n) {range = n; return this;}
	
	public int dist(Entity entity)
	{
		return Math.abs(trueRow() - entity.trueRow()) + Math.abs(trueCol() - entity.trueCol());
	}
	
}
