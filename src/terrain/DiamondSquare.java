package terrain;

import java.util.Random;
import java.util.ArrayList;

public class DiamondSquare {

	public double[][] t;
	public Random random;
	
	public static void main(String[] args)
	{
		double[][] temp = makeTable(50,50,50,50,17);
		DiamondSquare ds = new DiamondSquare(temp);
		//ds.diamond(0, 0, 4);
		ds.generate(new double[]{0, 0, 16, 15, 0.5},870L);
		printTable(ds.t);
	}

	public DiamondSquare(double[][] start)
	{
		t = start;
		forceStay = new boolean[start.length][start[0].length];
		for (int r = 0; r < start.length; r++)
		{
			for (int c = 0; c < start[0].length; c++)
			{
				if (start[r][c] != 0)
				{
					forceStay[r][c] = true;
				}
			}
		}
	}

	//Creates a table with 4 corners set to argument values
	public static double[][] makeTable(double topLeft, double topRight, double botLeft, double botRight, int width)
	{
		double[][] temp = new double[width][width];
		for (int r = 0; r < width; r++)
		{
			for (int c = 0; c < width; c++)
			{
				temp[r][c] = 0; //???
			}
		}
		temp[0][0] = topLeft;
		temp[0][width-1] = topRight;
		temp[width-1][0] = botLeft;
		temp[width-1][width-1] = botRight;
		return temp;
	}

	public static void printTable(double[][] a)
	{
		for (int i = 0; i < a.length; i++)
		{
			for (int j = 0; j < a[0].length; j++)
			{
				System.out.print((int)a[i][j] + " ");
			}
			System.out.println();
		}
	}

	//Starts the iterative loop over the terrain that modifies it
	//Returns a list of the tables between each diamond-square cycle
	public void dS(int sX, int sY, int width, double startAmp, double ratio)
	{
		while (true)
		{
			for (int r = sX; r <= t.length - 2; r += width)
			{
				for (int c = sY; c <= t[0].length - 2; c += width)
				{
					diamond(r, c, width, startAmp);
				}
			}
			if (width > 1)
			{
				width /= 2;
				startAmp *= ratio;
			}
			else
				break;
		}
	}

	public boolean[][] forceStay;
	public void diamond(int sX, int sY, int width, double startAmp)
	{
		//System.out.println(random);
		if (!forceStay[sX + width/2][sY + width/2])
			t[sX + width/2][sY + width/2] = (t[sX][sY] + t[sX+width][sY] + t[sX][sY+width] + t[sX+width][sY+width])/4 + 
					startAmp*(random.nextDouble() - 0.5)*2;
		/*System.out.println(t[sX][sY]);
		System.out.println(t[sX+width][sY]);
		System.out.println(t[sX][sY+width]);
		System.out.println(t[sX+width][sY+width]);
		System.out.println("-------");*/
		//printTable(t);
		//System.out.println("-------");
		if (width > 1)
		{
			square(sX + width/2, sY, width, startAmp);
			square(sX, sY + width/2, width, startAmp);
			square(sX + width, sY + width/2, width, startAmp);
			square(sX + width/2, sY + width, width, startAmp);
			//diamond(sX, sY, width/2);
			//diamond(sX + width/2, sY, width/2);
			//diamond(sX, sY + width/2, width/2);
			//diamond(sX + width/2, sY + width/2, width/2);
		}
	}

	public void square(int sX, int sY, int width, double startAmp)
	{
		if (forceStay[sX][sY]) return;
		if (sX - width/2 < 0)
		{
			t[sX][sY] = (t[sX][sY - width/2] + t[sX][sY + width/2] + t[sX + width/2][sY])/3;
		}
		else if (sX + width/2 >= t.length)
		{
			t[sX][sY] = (t[sX][sY - width/2] + t[sX][sY + width/2] + t[sX - width/2][sY])/3;
		}
		else if (sY - width/2 < 0)
		{
			t[sX][sY] = (t[sX][sY + width/2] + t[sX + width/2][sY] + t[sX - width/2][sY])/3;
		}
		else if (sY + width/2 >= t.length)
		{
			t[sX][sY] = (t[sX][sY - width/2] + t[sX + width/2][sY] + t[sX - width/2][sY])/3;
		}
		else
		{
			t[sX][sY] = (t[sX][sY + width/2] + t[sX][sY - width/2] + t[sX + width/2][sY] + t[sX - width/2][sY])/4;
		}
		t[sX][sY] += startAmp*(random.nextDouble() - 0.5)*2;
	}

	public double[][] generate(double[] args, long seed) {
		random = new Random(seed);
		dS((int)args[0],(int)args[1],(int)args[2],args[3],args[4]);
		return t;
	}

}
