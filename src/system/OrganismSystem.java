package system;

import java.util.ArrayList;

import level.Grid;
import level.Pathfinder;
import level.Tile;
import entity.Entity;
import entity.Organism;
import render.Main;

public class OrganismSystem extends BaseSystem {

	public boolean nextTurn = false;
	public Organism[][] records;
	public Pathfinder pathfinder;

	public OrganismSystem(Main m)
	{
		super(m);
	}

	public void tick()
	{
		if (nextTurn)
		{
			nextTurn = false;
			main.frameLastUpdate = main.frameCount;
			record();
			for (int i = 0; i < main.grid.organisms.size(); i++)
			{
				Organism org = main.grid.organisms.get(i);
				act(org);

				org.action = org.maxAction;
			}
			for (int i = main.grid.organisms.size() - 1; i >= 0; i--)
			{
				Organism org = main.grid.organisms.get(i);
				for (int j = org.units.size() - 1; j >= 0; j--)
				{
					Entity u = org.units.get(j);
					if (u.deathFlag)
					{
						org.units.remove(u);
					}
				}
			}
		}
	}

	//A rather functional way of thinking
	//think of it as grid.act(org)
	//although optimally it's org.act()
	public void act(Organism org)
	{
		while (org.action > 0)
		{
			if (org.queueTiles != null)
			{
				if (org.queueTiles.size() > 0)
				{
					Tile t = org.queueTiles.get(org.queueTiles.size()-1);
					Entity[] en = main.grid.valid(org, t.row, t.col); 
					if (en == null)
					{
						main.grid.moveCenterTo(org, t.row, t.col);
						org.queueTiles.remove(org.queueTiles.size()-1);
						org.action--;
					}
					else
					{
						System.out.println("Attack");
						int[] damage = main.grid.conflictSystem.attack(en[0], en[1]);
						if (en[0].health - damage[1] <= 0 && en[1].health - damage[0] <= 0)
						{
							if (en[0].health >= en[1].health)
							{
								en[1].deathFlag = true;
								//en[1].owner.destroy(en[1]);
								en[0].health -= damage[1];
							}
							else
							{
								en[0].deathFlag = true;
								en[1].health -= damage[0];
							}
						}
						else if (en[0].health - damage[1] <= 0)
						{
							en[0].deathFlag = true;
							en[1].health -= damage[0];
						}
						else if (en[1].health - damage[0] <= 0)
						{
							en[1].deathFlag = true;
							en[0].health -= damage[1];
						}
						else
						{
							en[1].health -= damage[0];
							en[0].health -= damage[1];
						}
						org.action--;
					}
					continue;
				}
			}
			if (!org.name.equals("Player"))
			{
				Tile t = main.grid.randomLand();
				if (pathFindTo(org, t.row, t.col) == null)
				{
					return;
				}
			}
			else
			{
				break;
			}
		}
	}

	public ArrayList<Tile> pathFindTo(Organism org, int r, int c)
	{
		org.queueTiles = pathfinder.findAdjustedPath(org, org.center.row, org.center.col, r, c);
		return org.queueTiles;
	}

	public void addGrid(Grid grid)
	{
		records = new Organism[grid.rows()][grid.cols()];
		pathfinder = new Pathfinder(grid);
	}

	public void record()
	{
		for (int r = 0; r < records.length; r++)
		{
			for (int c = 0; c < records[0].length; c++)
			{
				records[r][c] = null;
			}
		}
		for (int i = 0; i < main.grid.organisms.size(); i++)
		{
			Organism org = main.grid.organisms.get(i);
			for (int j = 0; j < org.units.size(); j++)
			{
				Entity u = org.units.get(j);
				if (main.grid.getTile(org.center.row + u.rDis, org.center.col + u.cDis) != null)
					records[org.center.row + u.rDis][org.center.col + u.cDis] = org;
			}
		}
	}

}
