package system;

import java.util.ArrayList;

import level.Tile;
import entity.Entity;
import entity.Organism;
import render.Main;

public class InputSystem extends BaseSystem {

	private ArrayList<Character> queue;
	private ArrayList<Click> clicks;

	public InputSystem(Main m) {
		super(m);
		queue = new ArrayList<Character>();
		clicks = new ArrayList<Click>();
		// TODO Auto-generated constructor stub
	}

	public void tick() 
	{
		while (queue.size() > 0)
		{
			char key = queue.remove(0);
			if (key == (char)32)
			{
				//System.out.println("Space");
				main.organismSystem.nextTurn = true;
			}
			else if (key == 'm')
			{
				if (main.renderSystem.sight == 10)
				{
					main.renderSystem.sight = 30;
				}
				else
				{
					main.renderSystem.sight = 10;
				}
			}
			else if (key == 'd')
			{
				Organism plr = main.grid.organisms.get(0);
				Entity[] en = main.grid.valid(plr, plr.center.row + 1, plr.center.col); 
				if (en == null && plr.action > 0)
				{
					main.grid.moveCenterTo(plr, plr.center.row + 1, plr.center.col);
					plr.action--;
				}
			}
			else if (key == 'a')
			{
				Organism plr = main.grid.organisms.get(0);
				Entity[] en = main.grid.valid(plr, plr.center.row - 1, plr.center.col); 
				if (en == null && plr.action > 0)
				{
					main.grid.moveCenterTo(plr, plr.center.row - 1, plr.center.col);
					plr.action--;
				}
			}
			else if (key == 's')
			{
				Organism plr = main.grid.organisms.get(0);
				Entity[] en = main.grid.valid(plr, plr.center.row, plr.center.col + 1); 
				if (en == null && plr.action > 0)
				{
					main.grid.moveCenterTo(plr, plr.center.row, plr.center.col + 1);
					plr.action--;
				}
			}
			else if (key == 'w')
			{
				Organism plr = main.grid.organisms.get(0);
				Entity[] en = main.grid.valid(plr, plr.center.row, plr.center.col - 1); 
				if (en == null && plr.action > 0)
				{
					main.grid.moveCenterTo(plr, plr.center.row, plr.center.col - 1);
					plr.action--;
				}
			}
		}
		Organism plr = main.grid.organisms.get(0);
		while (clicks.size() > 0) //Left click to move/select, right click to attack
		{
			if (clicks.get(0).mode == 0) 
			{
				Entity candidate = main.grid.findEntity(main.menuSystem.highlighted.row, main.menuSystem.highlighted.col);
				if (candidate != null)
				{
					if (candidate.owner.equals(main.grid.organisms.get(0)))
					{
						Entity lastSelected = main.menuSystem.selected;
						main.menuSystem.selected = candidate;
						//Attempt to unselect an item
						if (lastSelected != null)
						{
							if (lastSelected.equals(main.menuSystem.selected))
							{
								main.menuSystem.selected = null;
							}
						}
					}
				}
				else
				{
					/*if (main.menuSystem.selected != null)
					{
						main.menuSystem.selected.owner.queueTiles.clear();
						main.organismSystem.pathFindTo(main.menuSystem.selected.owner, 
								main.menuSystem.highlighted.row, 
								main.menuSystem.highlighted.col);
					}*/
					plr.queueTiles.clear();
					main.organismSystem.pathFindTo(plr, 
							main.menuSystem.highlighted.row, 
							main.menuSystem.highlighted.col);
				}
			}
			else 
			{
				Entity enemy = main.grid.findEntity(main.menuSystem.highlighted.row, main.menuSystem.highlighted.col);
				if (enemy != null)
				{
					if (!enemy.equals(plr) && plr.action > 0)
					{
						Entity enAttack;
						if (main.menuSystem.selected != null)
						{
							enAttack = main.menuSystem.selected;
						}
						else
						{
							enAttack = plr.units.get((int)(Math.random()*plr.units.size()));
						}
						int[] damage = main.grid.conflictSystem.attack(enAttack, enemy);
						if (enAttack.health - damage[1] <= 0 && enemy.health - damage[0] <= 0)
						{
							if (enAttack.health >= enemy.health)
							{
								enemy.deathFlag = true;
								//enemy.owner.destroy(enemy);
								enAttack.health -= damage[1];
							}
							else
							{
								enAttack.deathFlag = true;
								enemy.health -= damage[0];
							}
						}
						else if (enAttack.health - damage[1] <= 0)
						{
							enAttack.deathFlag = true;
							enemy.health -= damage[0];
						}
						else if (enemy.health - damage[0] <= 0)
						{
							enemy.deathFlag = true;
							enAttack.health -= damage[1];
						}
						else
						{
							enemy.health -= damage[0];
							enAttack.health -= damage[1];
						}
						if (!enAttack.deathFlag)
						{
							main.renderSystem.newArrow(
									main.grid.getTile(enAttack.owner.center.row + enAttack.rDis,enAttack.owner.center.col + enAttack.cDis),
									main.grid.getTile(enemy.owner.center.row + enemy.rDis,enemy.owner.center.col + enemy.cDis),
									main.frameCount);
						}
						plr.action--;
					}
				}
			}
			clicks.remove(0);
		}
	}

	public void queueKey(char key)
	{
		queue.add(key);
	}

	public class Click {public float x,y; public int mode; public Click(float a, float b, int m) {x = a; y = b; mode = m;}}
	public void queueMouse(float mouseX, float mouseY, int mode)
	{
		clicks.add(new Click(mouseX, mouseY, mode));
	}

	public void mousePass(float mouseX, float mouseY)
	{
		Tile pivot = main.grid.getTile(
				main.grid.organisms.get(0).center.row - main.renderSystem.sight,
				main.grid.organisms.get(0).center.col - main.renderSystem.sight
				);
		//float width = main.width/(main.renderSystem.sight*2 + 1), height = main.height/(main.renderSystem.sight*2 + 1);
		main.menuSystem.highlighted = main.grid.getTile(
				(int)(pivot.row + (mouseX/main.width)*(float)(main.renderSystem.sight*2 + 1)),
				(int)(pivot.col + (mouseY/main.height)*(float)(main.renderSystem.sight*2 + 1))
				);
		//System.out.println((int)(pivot.row + (mouseX/main.width)*(float)main.grid.rows()) + "," +
		//(int)(pivot.col + (mouseY/main.height)*(float)main.grid.cols()));
	}

}
