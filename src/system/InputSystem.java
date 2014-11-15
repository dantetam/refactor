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
	}

	public void queueKey(char key)
	{
		queue.add(key);
	}
	
	public class Click {public float x,y; public Click(float a, float b) {x = a; y = b;}}
	public void queueMouse(float mouseX, float mouseY)
	{
		clicks.add(new Click(mouseX, mouseY));
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
