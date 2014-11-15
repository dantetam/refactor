package render;

import java.util.ArrayList;
import processing.core.PApplet;

import level.Grid;
import level.LevelLoader;
import system.*;

public class Main extends PApplet {

	public Grid grid;
	public LevelLoader levelLoader;
	
	public ArrayList<BaseSystem> systems = new ArrayList<BaseSystem>();
	public RenderSystem renderSystem = new RenderSystem(this);
	
	public void setup()
	{
		size(1600,900);
		levelLoader = new LevelLoader(870L);
		grid = new Grid(levelLoader.newLevel(64));
		
		systems.add(renderSystem);
	}
	
	public void draw()
	{
		for (int i = 0; i < systems.size(); i++)
		{
			systems.get(i).tick();
		}
	}
	
}
