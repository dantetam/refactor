package system;

import entity.Entity;
import entity.Organism;
import render.Main;
import level.Tile;

public class RenderSystem extends BaseSystem {

	public int sight = 10;

	public RenderSystem(Main m)
	{
		super(m);
	}

	public void tick()
	{
		main.background(255);
		float width = main.width/(sight*2 + 1), height = main.height/(sight*2 + 1);
		Organism plr = main.grid.organisms.get(0);
		int nr = 0, nc = 0; //"Real" iterators to keep track of the row and column on screen
		for (int r = plr.center.row - sight; r <= plr.center.row + sight; r++)
		{
			for (int c = plr.center.col - sight; c <= plr.center.col + sight; c++)
			{
				Tile t = main.grid.getTile(r,c);
				if (t == null) continue;
				main.stroke(255);
				switch (t.biome)
				{
				case -1: main.fill(150,225,255); main.noStroke(); break;
				case 0: main.fill(150,225,255); break;
				case 1: main.fill(255,255,255); break;
				case 2: main.fill(245,245,220); break;
				case 3: main.fill(153,255,153); break;
				case 4: main.fill(51,255,51); break;
				case 5: main.fill(51,255,51); break;
				case 6: main.fill(51,25,0); break;
				default: main.fill(255,0,0); break;
				}
				main.rect(nr*width,nc*height,width,height);
				nc++;
			}
			nc = 0;
			nr++;
		}
		for (int i = 0; i < main.grid.organisms.size(); i++)
		{
			Organism org = main.grid.organisms.get(i);
			for (int j = 0; j < org.units.size(); j++)
			{
				Entity unit = org.units.get(j);
				main.fill(255,0,0);
				main.rect(
						(org.center.row + unit.rDis - plr.center.row + sight)*width,
						(org.center.col + unit.cDis - plr.center.col + sight)*height,width,height
						);
			}
		}
	}

}
