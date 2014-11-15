package render;

import java.util.ArrayList;

import data.Data;
import processing.core.PApplet;
import level.Grid;
import level.LevelLoader;
import system.*;

public class Main extends PApplet {

	public Grid grid;
	public LevelLoader levelLoader;
	
	public ArrayList<BaseSystem> systems = new ArrayList<BaseSystem>();
	public OrganismSystem organismSystem = new OrganismSystem(this);
	public InputSystem inputSystem = new InputSystem(this);
	public RenderSystem renderSystem = new RenderSystem(this);
	public MenuSystem menuSystem = new MenuSystem(this);
	
	public int frameLastUpdate = 0;
	
	public void setup()
	{
		size(900,900);
		Data.init();
		
		levelLoader = new LevelLoader(870L);
		grid = new Grid(levelLoader.newLevel(128));
		
		systems.add(organismSystem);
		systems.add(inputSystem);
		systems.add(renderSystem);
		systems.add(menuSystem);
		
		organismSystem.addGrid(grid);
		
		textSize(16);
	}
	
	public void draw()
	{
		inputSystem.mousePass(mouseX, mouseY);
		for (int i = 0; i < systems.size(); i++)
		{
			systems.get(i).tick();
		}
	}
	
	public void keyPressed()
	{
		inputSystem.queueKey(key);
	}
	
	public void mousePressed()
	{
		if (mouseButton == LEFT)
			inputSystem.queueMouse(mouseX, mouseY, 0);
		else
			inputSystem.queueMouse(mouseX, mouseY, 1);
	}
	
}
