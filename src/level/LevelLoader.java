package level;

import java.util.Random;

import terrain.*;

public class LevelLoader {

	public Random random;
	public long theSeed;
	
	public double[][] theBiomes; //Store for renderSystem
	
	public LevelLoader(long seed)
	{
		theSeed = seed;
		seed(seed);
	}
	
	public void seed(long seed) {random = new Random(seed);}
	
	public int[][] newLevel(int len)
	{
		len = 64;
		double[][] temp = DiamondSquare.makeTable(50,50,50,50,len+1);
		DiamondSquare ds = new DiamondSquare(temp);

		double[][] landSea = ds.generate(new double[]{0, 0, len, 15, 0.6},theSeed);
		double cutoff = DiamondSquare.seaLevel(landSea, 60, 0.6);
		//int[][] biomes = new int[landSea.length][landSea[0].length];
		double[][] biomes = new PerlinNoise(theSeed).generate(new double[]{len/2,len/2,5.5,8,1,0.8,6,len});
		theBiomes = biomes;
		//double[][] 
		int[][] t = new int[biomes.length][biomes[0].length];
		for (int r = 0; r < biomes.length; r++)
		{
			for (int c = 0; c < biomes[0].length; c++)
			{
				if (landSea[r][c] < cutoff)
				{
					biomes[r][c] = -1;
				}
				else if (biomes[r][c] > 6)
				{
					biomes[r][c] = 6;
				}
				t[r][c] = (int)biomes[r][c]; //Send to the integer table to be returned
			}
		}
		return t;
	}
	
}
