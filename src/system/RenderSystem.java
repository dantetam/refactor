package system;

import java.util.ArrayList;

import entity.Entity;
import entity.Organism;
import render.Main;
import level.Tile;

public class RenderSystem extends BaseSystem {

	public int sight = 10;
	public float debounce = 40;
	private ArrayList<AttackArrow> arrows = new ArrayList<AttackArrow>();
	
	public RenderSystem(Main m)
	{
		super(m);
	}

	public void tick()
	{
		main.background(255);
		main.textAlign(main.CENTER);
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
				main.strokeWeight(1);
				if (main.menuSystem.highlighted != null)
				{
					if (main.menuSystem.highlighted.equals(t))
					{
						main.stroke(0,0,255);
						main.strokeWeight(5);
					}
				}
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
		float frames = (float)Math.min(main.frameCount - main.frameLastUpdate,debounce)/debounce;
		for (int i = main.grid.organisms.size() - 1; i >= 0; i--)
		{
			Organism org = main.grid.organisms.get(i);
			main.fill(org.r, org.g, org.b);
			for (int j = 0; j < org.units.size(); j++)
			{
				Entity unit = org.units.get(j);

				//Find out if the health decreased and shake the box if hurt
				float xDis = 0;
				nr = (org.center.row + unit.rDis - plr.center.row + sight);
				nc = (org.center.col + unit.cDis - plr.center.col + sight);

				//System.out.println(main.organismSystem.respHealth.get(unit));

				if (main.organismSystem.respHealth.get(unit) != null)
				{
					float dHealth = main.organismSystem.respHealth.get(unit) - unit.health;
					if (dHealth > 0 && frames < 1)
					{
						xDis = width*0.2F*(1-frames%4)*(float)Math.pow(-1,main.frameCount-main.frameLastUpdate);
					}
				}

				if (main.organismSystem.records[org.center.row][org.center.col] == null) //The unit recently moved to the spot
				{
					main.fill(org.r, org.g, org.b);
					main.rect(
							nr*width + width*(0.5F - frames/2F) + xDis,
							nc*height + height*(0.5F - frames/2F),
							width*frames,height*frames
							);
				}
				else if (main.organismSystem.records[org.center.row][org.center.col].equals(org)) //The unit is still there
				{
					main.fill(org.r, org.g, org.b);
					main.rect(
							(org.center.row + unit.rDis - plr.center.row + sight)*width + xDis,
							(org.center.col + unit.cDis - plr.center.col + sight)*height,
							width,height
							);
				}
			}
		}
		nr = 0; nc = 0; //"Real" iterators to keep track of the row and column on screen
		for (int r = plr.center.row - sight; r <= plr.center.row + sight; r++)
		{
			for (int c = plr.center.col - sight; c <= plr.center.col + sight; c++)
			{
				if (main.grid.getTile(r, c) == null) continue;
				Organism candidate = main.organismSystem.records[r][c];
				Entity unit = main.grid.findEntity(r, c);
				if (candidate != null &&
						main.grid.findEntity(r, c) == null) //A unit left
				{
					main.fill(candidate.r, candidate.g, candidate.b);
					main.rect(
							nr*width + width*(frames/2F),
							nc*height + height*(frames/2F),
							width*(1-frames),height*(1-frames)
							);
				}
				else if (main.organismSystem.respHealth.get(unit) != null)
				{
					float dHealth = main.organismSystem.respHealth.get(unit) - unit.health;
					if (dHealth > 0)
					{
						main.fill(0,0,0);
						main.text((int)-dHealth + "", (nr+0.5F)*width, (nc+0.5F)*height - height*frames/2);
					}
					if (unit.health < unit.maxHealth)
					{
						main.fill(255,0,0);
						main.rect((nr+0.1F)*width, (nc+0.45F)*height, 0.8F*width, 0.1F*height);
						main.fill(0,255,0);
						main.rect((nr+0.1F)*width, (nc+0.45F)*height, 0.8F*width*(unit.health/unit.maxHealth), 0.1F*height);
					}
				}
				nc++;
			}
			nc = 0;
			nr++;
		}
		
		for (int i = 0; i < arrows.size(); i++)
		{
			AttackArrow ar = arrows.get(i);
			if (main.frameCount - ar.frameCreated > 40) continue;
			if (plr.center == null) continue;
			int nr1 = ar.a.row - plr.center.row + sight;
			int nc1 = ar.a.col - plr.center.col + sight;
			int nr2 = ar.d.row - plr.center.row + sight;
			int nc2 = ar.d.col - plr.center.col + sight;
			//System.out.println(nr1 + " " + nc1 + " " + nr2 + " " + nc2);
			if (nr1 > 0 && nr1 < sight*2 + 1 && nr2 > 0 && nr2 < sight*2 + 1 && 
					nc1 > 0 && nc1 < sight*2 + 1 && nc2 > 0 && nc2 < sight*2 + 1)
			{
				//System.out.println(nr1*width + " " + nc1*height + " " + nr2*width + " " + nc2*height);
				main.stroke(255,0,0);
				//main.line(nr1*width,nc1*height,nr2*width,nc2*height);
				main.line((nr1+0.5F)*width,(nc1+0.5F)*height,(nr2+0.5F)*width,(nc2+0.5F)*height);
				main.rect((nr2+0.5F)*width,(nc2+0.5F)*height,2,2);
			}
		}
	}
	
	public class AttackArrow
	{
		public Tile a,d; public int frameCreated;
		public AttackArrow(Tile x, Tile y, int frame) {a=x; d=y; frameCreated=frame;}
	}
	public void newArrow(Tile x, Tile y, int frame) {arrows.add(new AttackArrow(x,y,frame));}

}
