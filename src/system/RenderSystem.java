package system;

import render.Main;
import level.Tile;

public class RenderSystem extends BaseSystem {

	public RenderSystem(Main m)
	{
		super(m);
	}
	
	public void tick()
	{
		main.background(255);
		for (int r = 0; r < main.grid.rows(); r++)
		{
			for (int c = 0; c < main.grid.cols(); c++)
			{
				Tile t = main.grid.getTile(r,c);
				switch (t.biome)
				{
				case -1: main.fill(150,225,255); break;
				case 0: main.fill(150,225,255); break;
				case 1: main.fill(255,255,255); break;
				case 2: main.fill(245,245,220); break;
				case 3: main.fill(153,255,153); break;
				case 4: main.fill(51,255,51); break;
				case 5: main.fill(51,255,51); break;
				case 6: main.fill(51,25,0); break;
				}
				float width = main.width/main.grid.rows(), height = main.height/main.grid.cols();
				main.stroke(255);
				main.rect(r*width,c*height,width,height);
			}
		}
	}
	
}
