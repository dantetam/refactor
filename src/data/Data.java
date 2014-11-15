package data;

import java.util.HashMap;
import java.util.Map.Entry;

import entity.Organism;

public class Data {

	public static HashMap<String,Organism> organisms;
	
	public static void init()
	{
		organisms = new HashMap<String,Organism>();
		
		Organism temp;
		
		temp = new Organism("Test");
		temp.color(150,150,150);
		temp.addUnit(0, 0, 15, 4, 2, 0).range(5);
		temp.addUnit(0, 1, 10, 2, 2, 0);
		temp.addUnit(0, -1, 10, 2, 2, 0);
		temp.addUnit(1, 0, 10, 2, 2, 0);
		temp.addUnit(-1, 0, 10, 2, 2, 0);
		temp.maxAction = 1;
		organisms.put(temp.name, temp);
		
		temp = new Organism("Player");
		temp.color(255,0,0);
		temp.addUnit(-1, 1, 10, 2, 2, 0);
		temp.addUnit(0, 1, 15, 4, 4, 0);
		temp.addUnit(1, 1, 10, 2, 2, 0);
		temp.addUnit(1, 0, 15, 4, 4, 0);
		temp.addUnit(1, -1, 10, 2, 2, 0);
		temp.addUnit(0, -1, 15, 4, 4, 0);
		temp.addUnit(-1, -1, 10, 2, 2, 0);
		temp.addUnit(-1, 0, 15, 4, 4, 0);
		temp.addUnit(0, 0, 30, 6, 4, 6).range(5);
		temp.maxAction = 1;
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
