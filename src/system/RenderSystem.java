package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import data.Data;
import processing.core.PImage;
import entity.Entity;
import entity.Organism;
import render.Main;
import level.Tile;

public class RenderSystem extends BaseSystem {

	public int sight = 10;
	public float debounce = 40;
	private ArrayList<AttackArrow> arrows = new ArrayList<AttackArrow>();
	public HashMap<String, PImage> textures = new HashMap<String, PImage>();
	public PImage[][] terrainTextures;

	public RenderSystem(Main m)
	{
		super(m);
	}
	
	public void terrainTextures(double[][] biomes)
	{
		int rows = biomes.length; int cols = biomes[0].length;
		terrainTextures = new PImage[rows][cols];
		float width = main.width/(sight*2 + 1), height = main.height/(sight*2 + 1);
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				switch ((int)biomes[r][c])
				{
				case -1: terrainTextures[r][c] = getTerrainBlock(150,225,255,(int)width,(int)height); break;
				case 0: terrainTextures[r][c] = getTerrainBlock(150,225,255,(int)width,(int)height); break;
				case 1: terrainTextures[r][c] = getTerrainBlock(255,255,255,(int)width,(int)height); break;
				case 2: terrainTextures[r][c] = getTerrainBlock(245,245,220,(int)width,(int)height); break;
				case 3: terrainTextures[r][c] = getTerrainBlock(153,255,153,(int)width,(int)height); break;
				case 4: terrainTextures[r][c] = getTerrainBlock(51,225,51,(int)width,(int)height); break;
				case 5: terrainTextures[r][c] = getTerrainBlock(51,225,51,(int)width,(int)height); break;
				case 6: terrainTextures[r][c] = getTerrainBlock(51,25,0,(int)width,(int)height); break;
				default: terrainTextures[r][c] = getTerrainBlock(255,0,0,(int)width,(int)height); break;
				}
			}
		}
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

				//main.rect(nr*width,nc*height,width,height);
				main.image(terrainTextures[r][c],nr*width,nc*height,width,height);
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

				if (nr >= 0 && nr <= 2*sight + 1 && nc >= 0 && nc <= 2*sight + 1)
				{
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
						/*main.rect(
								nr*width + width*(0.5F - frames/2F) + xDis,
								nc*height + height*(0.5F - frames/2F),
								width*frames,height*frames
								);*/
						main.image(textures.get(org.name), nr*width + width*(0.5F - frames/2F) + xDis,
								nc*height + height*(0.5F - frames/2F), width*frames, height*frames);
					}
					else if (main.organismSystem.records[org.center.row][org.center.col].equals(org)) //The unit is still there
					{
						main.fill(org.r, org.g, org.b);
						main.image(textures.get(org.name),
								(org.center.row + unit.rDis - plr.center.row + sight)*width + xDis,
								(org.center.col + unit.cDis - plr.center.col + sight)*height,
								width,height
								);
					}
					else //A different unit occupies the spot
					{
						main.fill(org.r, org.g, org.b);
						main.image(textures.get(org.name),
								(org.center.row + unit.rDis - plr.center.row + sight)*width,
								(org.center.col + unit.cDis - plr.center.col + sight)*height,
								width,height
								);
					}
				}
			}
		}

		main.fill(255,255,0);
		main.stroke(1);
		nr = 0; nc = 0;
		for (int r = plr.center.row - sight; r <= plr.center.row + sight; r++)
		{
			for (int c = plr.center.col - sight; c <= plr.center.col + sight; c++)
			{
				if (main.grid.getTile(r, c) == null) continue;
				if (main.grid.coins[r][c] > 0)
				{
					main.rect((nr+0.3F)*width, (nc+0.3F)*height, width*0.4F, height*0.4F);
				}
				nc++;
			}
			nc = 0;
			nr++;
		}
		main.noStroke();

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
					main.image(textures.get(candidate.name),
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
				}
				if (unit != null)
					if (unit.health < unit.maxHealth)
					{
						main.fill(255,0,0);
						main.rect((nr+0.1F)*width, (nc+0.45F)*height, 0.8F*width, 0.1F*height);
						main.fill(0,255,0);
						main.rect((nr+0.1F)*width, (nc+0.45F)*height, 0.8F*width*(unit.health/unit.maxHealth), 0.1F*height);
					}
				if (main.menuSystem.highlighted != null)
				{
					if (main.menuSystem.highlighted.equals(main.grid.getTile(r,c)))
					{
						main.stroke(0,0,255);
						main.strokeWeight(5);
						main.line((nr+1)*width, nc*height, nr*width, (nc+1)*height);
						main.line(nr*width, nc*height, (nr+1)*width, (nc+1)*height);
					}
				}
				main.strokeWeight(1);
				nc++;
			}
			nc = 0;
			nr++;
		}

		if (plr.queueTiles != null)
		{
			for (int i = 0; i < plr.queueTiles.size(); i++)
			{
				Tile t = plr.queueTiles.get(i);
				int r = t.row - plr.center.row + sight;
				int c = t.col - plr.center.col + sight;
				if (r > 0 && r < sight*2 + 1 && c > 0 && c < sight*2 + 1)
				{
					main.fill(255,0,0);
					main.stroke(255,0,0);
					int len = 10;
					main.rect((r+0.5F)*width - len/2,(c+0.5F)*height - len/2,len,len);
				}
			}
		}

		//Add the little damage arrows
		for (int i = 0; i < main.grid.organisms.size(); i++)
		{
			Organism org = main.grid.organisms.get(i);
			for (int j = 0; j < org.units.size(); j++)
			{
				Entity unit = org.units.get(j);
				for (int k = 0; k < unit.attacked.size(); k++)
				{
					if (unit.owner != null && unit.attacked.get(k).owner != null)
						if (unit.attacked.get(k).owner.center != null)
							newArrow(main.grid.getTile(unit.trueRow(),unit.trueCol()),
									main.grid.getTile(unit.attacked.get(k).trueRow(),unit.attacked.get(k).trueCol()),
									main.frameCount);
				}
			}
		}

		for (int i = 0; i < arrows.size(); i++)
		{
			AttackArrow ar = arrows.get(i);
			if (main.frameCount - ar.frameCreated > 40) continue;
			if (plr.center == null || ar.a == null || ar.d == null) continue;
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
				main.fill(0,0,0);
				main.rect(((nr1+0.5F) + (nr2-nr1)*frames)*width,((nc1+0.5F) + (nc2-nc1)*frames)*height,5,5);
				main.rect((nr2+0.5F)*width,(nc2+0.5F)*height,2,2);
			}
		}
	}

	public void models()
	{
		for (Entry<String, Organism> i : Data.organisms.entrySet())
		{
			Organism org = i.getValue();
			int width = (int)(main.width/(sight*2F + 1F)), height = (int)(main.height/(sight*2F + 1F));
			textures.put(i.getKey(), getBlock(org.r, org.g, org.b, width, height));
		}
	}

	public PImage getTerrainBlock(float red, float green, float blue, int w, int h)
	{
		PImage temp = main.createImage(w,h,main.ARGB);
		main.pushStyle();
		float maxVari = 10; int spacing = 2;
		for (int r = 0; r < w; r += spacing)
			for (int c = 0; c < h; c += spacing)
			{
				for (int nr = r; nr < r + spacing; nr++)
				{
					for (int nc = c; nc < c + spacing; nc++)
					{
						temp.pixels[nr*w + nc] = main.color(
								red + (float)(maxVari*2*Math.random()) - maxVari,
								green + (float)(maxVari*2*Math.random()) - maxVari,
								blue + (float)(maxVari*2*Math.random()) - maxVari,
								255);
					}
				}
			}
		temp.updatePixels();
		main.popStyle();
		return temp;
	}

	public PImage getBlock(float red, float green, float blue, int w, int h)
	{
		PImage temp = main.createImage(w,h,main.ARGB);
		main.pushStyle();
		for (int r = 0; r < w; r++)
			for (int c = 0; c < h; c++)
				temp.pixels[r*w + c] = main.color(red,green,blue,0);
		int borderWidth = 10;
		for (int i = borderWidth; i >= 0; i--)
		{
			float a = (borderWidth-Math.min(i,w-i-1))/(float)(borderWidth)*255F - 50;
			for (int row = 0; row < h; row++)
			{
				temp.pixels[row*h + i] = main.color(red,green,blue,a);
				temp.pixels[row*h + (w-i-1)] = main.color(red,green,blue,a);
			}
			for (int col = 0; col < w; col++)
			{
				temp.pixels[i*w + col] = main.color(red,green,blue,a);
				temp.pixels[(w-i-1)*h + col] = main.color(red,green,blue,a);
			}
		}
		temp.updatePixels();
		main.popStyle();
		return temp;
	}

	public class AttackArrow
	{
		public Tile a,d; public int frameCreated;
		public AttackArrow(Tile x, Tile y, int frame) {a=x; d=y; frameCreated=frame;}
	}
	public void newArrow(Tile x, Tile y, int frame) {arrows.add(new AttackArrow(x,y,frame));}

}
