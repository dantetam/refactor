package system;

import java.util.ArrayList;

import render.Main;

public class InputSystem extends BaseSystem {

	private ArrayList<Character> queue;
	
	public InputSystem(Main m) {
		super(m);
		queue = new ArrayList<Character>();
		// TODO Auto-generated constructor stub
	}

	public void tick() 
	{
		while (queue.size() > 0)
		{
			char key = queue.remove(0);
			if (key == (char)32)
			{
				System.out.println("Space");
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
		}
	}

	public void queueKey(char key)
	{
		queue.add(key);
	}
	
}
