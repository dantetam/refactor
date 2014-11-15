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
		
		temp = new Organism("Test");
		temp.addUnit(0, 0, 15, 4, 2, 0).range(5);
		temp.addUnit(0, 1, 10, 2, 2, 0);
		temp.addUnit(0, -1, 10, 2, 2, 0);
		temp.addUnit(1, 0, 10, 2, 2, 0);
		temp.addUnit(-1, 0, 10, 2, 2, 0);
		temp.maxAction = 3;
		organisms.put(temp.name, temp);
		
		temp = new Organism("Player");
		temp.addUnit(-1, 1, 10, 2, 4, 0);
		temp.addUnit(0, 1, 15, 4, 4, 0);
		temp.addUnit(1, 1, 10, 2, 4, 0);
		temp.addUnit(1, 0, 15, 4, 4, 0);
		temp.addUnit(1, -1, 10, 2, 4, 0);
		temp.addUnit(0, -1, 15, 4, 4, 0);
		temp.addUnit(-1, -1, 10, 2, 4, 0);
		temp.addUnit(-1, 0, 15, 4, 4, 0);
		temp.maxAction = 5;
		organisms.put(temp.name, temp);
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
