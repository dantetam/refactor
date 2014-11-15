package system;

import entity.Entity;
import entity.Organism;
import level.Tile;
import render.Main;

public class MenuSystem extends BaseSystem {

	public Tile highlighted;
	public Entity selected;

	public MenuSystem(Main m)
	{
		super(m);
	}

	public void tick()
	{
		Organism plr = main.grid.organisms.get(0);
		main.noStroke();
		main.fill(0);
		main.rect(0,0,400,100);
		main.textAlign(main.LEFT, main.CENTER);
		main.fill(255);
		main.text("Action: " + plr.action + "/" + plr.maxAction, 25, 40);
		main.text("Kills: " + plr.kills, 25, 55);
		
		if (selected != null)
		{
			main.fill(0);
			main.rect(500,0,400,100);
			main.fill(255);
			main.text("Name: " + selected.id, 525, 10);
			main.text((int)selected.offensiveStr + " attack; " + (int)selected.defensiveStr + " defense; " +
			(int)selected.rangedStr + " ranged", 525, 25);
		}
	}

}
