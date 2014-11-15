package data;

import java.util.HashMap;
import java.util.Map.Entry;

import entity.Organism;

public class Data {

	private static HashMap<String,Organism> organisms;
	
	public static void init()
	{
		organisms = new HashMap<String,Organism>();
		
		Organism temp;
		
		temp = new Organism();
		temp.addUnit(0, 0);
		temp.addUnit(0, 1);
		temp.addUnit(0, -1);
		temp.addUnit(1, 0);
		temp.addUnit(-1, 0);
		organisms.put("Test", temp);
		
		temp = new Organism();
		temp.addUnit(-1, 1);
		temp.addUnit(0, 1);
		temp.addUnit(1, 1);
		temp.addUnit(1, 0);
		temp.addUnit(1, -1);
		temp.addUnit(0, -1);
		temp.addUnit(-1, -1);
		temp.addUnit(-1, 0);
		organisms.put("Player", temp);
	}
	
	public static Organism getOrganism(String name)
	{
		for (Entry<String, Organism> i : organisms.entrySet())
		{
			if (i.getKey().equals(name))
			{
				return new Organism(organisms.get(name));
			}
		}
		return null;
	}
	
}
