package data;

import java.util.ArrayList;
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
		temp.color(0,0,0);
		temp.addUnit(0, 0, 15, 4, 2, 2).range(5);
		temp.addUnit(0, 1, 10, 2, 2, 0);
		temp.addUnit(0, -1, 10, 2, 2, 0);
		temp.addUnit(1, 0, 10, 2, 2, 0);
		temp.addUnit(-1, 0, 10, 2, 2, 0);
		temp.maxAction = 1;
		organisms.put(temp.name, temp);
		
		temp = new Organism("Block");
		temp.color(100,50,0);
		temp.addUnit(0, 0, 30, 2, 3, 0);
		temp.addUnit(0, 1, 30, 2, 3, 0);
		temp.addUnit(1, 1, 30, 2, 3, 0);
		temp.addUnit(1, 0, 30, 2, 3, 0);
		temp.maxAction = 1;
		organisms.put(temp.name, temp);
		
		temp = new Organism("Spider");
		temp.color(0,0,0);
		temp.addUnit(0, 0, 15, 4, 2, 2).range(5);
		temp.addUnit(1, 1, 5, 2, 1, 0);
		temp.addUnit(2, 2, 5, 2, 1, 0);
		temp.addUnit(-1, -1, 5, 2, 1, 0);
		temp.addUnit(-2, -2, 5, 2, 1, 0);
		temp.addUnit(-1, 1, 5, 2, 1, 0);
		temp.addUnit(-2, 2, 5, 2, 1, 0);
		temp.addUnit(1, -1, 5, 2, 1, 0);
		temp.addUnit(2, -2, 5, 2, 1, 0);
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
	
	public static Organism randomOrganism()
	{
		ArrayList<String> candidates = new ArrayList<String>();
		for (Entry<String, Organism> i : organisms.entrySet())
		{
			if (!i.getKey().equals("Player"))
				candidates.add(i.getKey());
		}
		return getOrganism(candidates.get((int)(Math.random()*candidates.size())));
	}
	
}
