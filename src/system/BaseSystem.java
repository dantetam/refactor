package system;

import render.Main;

public abstract class BaseSystem {

	public Main main;
	
	public BaseSystem(Main m)
	{
		main = m;
	}

	public abstract void tick();
	
}
