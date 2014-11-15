package level;

import entity.Entity;

public class ConflictSystem {

	public Grid grid;

	public ConflictSystem(Grid grid)
	{
		this.grid = grid;
	}

	//Return the damage inflicted by a on d in an attack, and d on a in a defense
	public int[] attack(Entity a, Entity d)
	{
		return attack(a.offensiveStr, d.defensiveStr);
	}
	
	//Return the damage inflicted by a ranged attack
	public int[] fire(Entity a, Entity d)
	{
		return fire(a.rangedStr, d.defensiveStr);
	}

	public int[] attack(float a, float d)
	{
		float spread = 3F/3F;

		float r = Math.max(a,1)/Math.max(d,1);
		float c1 = 4*r - 1;
		c1 += c1*((float)Math.random()*spread*2 - spread); 
		if (c1 > (16F/3F*r - 4F/3F));
		{
			c1 = (float)(Math.floor(c1-1));
		}

		r = Math.max(d,1)/Math.max(a,1);
		float c2 = 4*r - 1;
		c2 += c2*((float)Math.random()*spread*2 - spread);
		if (c2 > (16F/3F*r - 4F/3F));
		{
			c2 = (float)(Math.floor(c2-1));
		}

		//Not sure why this works
		if (c1 > c2)
		{
			return new int[]{(int)(Math.max(1,c1/2)),(int)(Math.max(1,c2))};
		}
		else
		{
			return new int[]{(int)(Math.max(1,c1)),(int)(Math.max(1,c2/2))};
		}
	}
	
	public int[] fire(float a, float d)
	{
		float spread = 3F/3F;

		float r = Math.max(a,1)/Math.max(d,1);
		float c1 = 4*r - 1;
		c1 += c1*((float)Math.random()*spread*2 - spread); 
		if (c1 > (16F/3F*r - 4F/3F));
		{
			c1 = (float)(Math.floor(c1-1));
		}
		
		return new int[]{(int)(Math.max(1,c1)),0};
	}

}
