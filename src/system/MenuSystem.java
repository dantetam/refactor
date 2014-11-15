package system;

import level.Tile;
import render.Main;

public class MenuSystem extends BaseSystem {

	public Tile highlighted;
	
	public MenuSystem(Main m)
	{
		super(m);
	}
	
	public void tick()
	{
		main.fill(0);
		main.rect(0,0,400,100);
		main.textAlign(main.LEFT, main.CENTER);
		main.fill(255);
		main.text("Action: " + main.grid.organisms.get(0).action, 25, 50);
	}
	
}
