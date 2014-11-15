package system;

import java.util.ArrayList;
import java.util.HashMap;

import data.Data;
import level.Grid;
import level.Pathfinder;
import level.Tile;
import entity.Entity;
import entity.Organism;
import render.Main;

public class OrganismSystem extends BaseSystem {

	public boolean nextTurn = false;
	public Organism[][] records;
	public HashMap<Entity,Float> respHealth;
	public Pathfinder pathfinder;
	public int turnsPassed = 0;

	public OrganismSystem(Main m)
	{
		super(m);
		respHealth = new HashMap<Entity,Float>();
	}

	public void tick()
	{
		if (nextTurn)
		{
			nextTurn = false;
			turnsPassed++;
			main.frameLastUpdate = main.frameCount;
			record();
			for (int i = 0; i < main.grid.organisms.size(); i++)
			{
				Organism org = main.grid.organisms.get(i);
				if (org.center != null)
					act(org);
				else
					continue;

				org.action = org.maxAction;
			}
			if (Math.random() < 1F - 1F/(((float)turnsPassed+1F)/20F))
			{
				Organism org = Data.getOrganism("Test");
				main.grid.organisms.add(org);
				Tile t;
				while (true)
				{
					t = main.grid.randomLand();
					//main.grid.moveCenterTo(org, t.row, t.col);
					Entity[] en = main.grid.valid(org, t.row, t.col); 
					if (en == null)
					{
						break;
					}
				}
				for (int i = 0; i < 10; i++)
				{
					int r = (int)(Math.random()*main.grid.rows());
					int c = (int)(Math.random()*main.grid.cols());
					main.grid.coins[r][c] += 1;
				}
				main.grid.moveCenterTo(org, t.row, t.col);
				org.color(150,150,150);
			}
		}
	}

	//A rather functional way of thinking
	//think of it as grid.act(org)
	//although optimally it's org.act()
	public void act(Organism org)
	{
		for (int i = 0; i < org.units.size(); i++)
		{
			org.units.get(i).attacked.clear();
		}
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
						boolean shot = false;
						for (int i = 0; i < org.units.size(); i++)
						{
							if (org.action < 2) break;
							Entity range = org.units.get(i);
							if (range.rangedStr > 0)
							{
								Entity target = randomTarget(range);
								if (target != null)
								{
									shoot(range,target);
									shot = true;
									org.action--;
									break;
								}
							}
						}
						if (shot)
						{
							org.queueTiles.clear();
						}
						else
						{
							main.grid.moveCenterTo(org, t.row, t.col);
							org.queueTiles.remove(org.queueTiles.size()-1);
							org.action--;
						}
					}
					else
					{
						//System.out.println("Attack");
						org.queueTiles.clear();
						Entity enAttack = org.units.get((int)(Math.random()*org.units.size()));
						int[] damage = main.grid.conflictSystem.attack(enAttack, en[1]);
						if (enAttack.health - damage[1] <= 0 && en[1].health - damage[0] <= 0)
						{
							if (enAttack.health >= en[1].health)
							{
								en[1].deathFlag = true;
								//en[1].owner.destroy(en[1]);
								enAttack.health -= damage[1];
							}
							else
							{
								enAttack.deathFlag = true;
								en[1].health -= damage[0];
							}
						}
						else if (enAttack.health - damage[1] <= 0)
						{
							enAttack.deathFlag = true;
							en[1].health -= damage[0];
						}
						else if (en[1].health - damage[0] <= 0)
						{
							en[1].deathFlag = true;
							enAttack.health -= damage[1];
						}
						else
						{
							en[1].health -= damage[0];
							enAttack.health -= damage[1];
						}
						if (!enAttack.deathFlag)
						{
							enAttack.attacked.add(en[1]);
							/*main.renderSystem.newArrow(
									main.grid.getTile(enAttack.owner.center.row + enAttack.rDis,enAttack.owner.center.col + enAttack.cDis),
									main.grid.getTile(en[1].owner.center.row + en[1].rDis,en[1].owner.center.col + en[1].cDis),
									main.frameCount);*/
						}
						else //Implying death
						{
							/*if (en[1].owner.name.equals("Player"))
							{
								en[1].owner.kills++;
							}*/
						}
						org.action--;
					}
					continue;
				}
			}
			if (!org.name.equals("Player"))
			{
				Organism plr = main.grid.organisms.get(0);
				Tile t;
				if (org.center.dist(plr.center) < 6) //Go attack the player if the player is near
				{
					t = plr.center;
				}
				else
				{
					t = main.grid.randomLand();
				}
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
		for (int j = org.units.size() - 1; j >= 0; j--)
		{
			if (j >= org.units.size()) continue;
			Entity u = org.units.get(j);
			if (u.deathFlag)
			{
				if ((float)(u.owner.units.size()-1)/(float)u.owner.origUnits < 0.5)
				{
					//System.out.println("Death");
					main.grid.coins[u.trueRow()][u.trueCol()] += u.owner.units.size();
					u.owner.units.clear();
					org.units.remove(u.owner);
					u.owner.center = null;
				}
				else
				{
					main.grid.coins[u.trueRow()][u.trueCol()] += 1;
					org.destroy(u);
				}
			}
		}
	}

	public Entity randomTarget(Entity a)
	{
		ArrayList<Entity> candidates = new ArrayList<Entity>();
		for (int r = a.trueRow() - a.range; r <= a.trueRow() + a.range; r++)
		{
			for (int c = a.trueCol() - a.range; c <= a.trueCol() + a.range; c++)
			{
				Entity candidate = main.grid.findEntity(r, c);
				if (!candidate.owner.equals(a.owner))
				{
					candidates.add(candidate);
				}
			}
		}
		if (candidates.size() == 0)
			return null;
		return candidates.get((int)(Math.random()*candidates.size()));
	}

	public void shoot(Entity shooter, Entity target)
	{
		int[] damage = main.grid.conflictSystem.fire(shooter, target);
		if (target.health - damage[0] <= 0)
		{
			target.deathFlag = true;
			//if (shooter.owner.name.equals("Player")) shooter.owner.kills++;
		}
		else
		{
			target.health -= damage[0];
		}
		main.renderSystem.newArrow(
				main.grid.getTile(shooter.owner.center.row + shooter.rDis,shooter.owner.center.col + shooter.cDis),
				main.grid.getTile(target.owner.center.row + target.rDis,target.owner.center.col + target.cDis),
				main.frameCount);
		//shooter.owner.action--; redundant
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
				respHealth.put(u, u.health);
				//System.out.println(respHealth.get(u));
			}
		}
	}

}
