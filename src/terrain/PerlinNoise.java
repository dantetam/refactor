package terrain;

import java.util.Random;

public class PerlinNoise {
	
	public Random random;
	
	public static void main(String[] args)
	{
		double[][] test = new PerlinNoise(870L).generate(new double[]{32,32,6,8,1,0.8,6,32,55});
		for (int r = 0; r < test.length; r++)
		{
			for (int c = 0; c < test[0].length; c++)
			{
				System.out.print((int)test[r][c] + " ");
			}
			System.out.println();
		}
	}
	
	public PerlinNoise(double seed)
	{
		random = new Random((long)seed);
	}

	public double[][] makePerlinNoise(int width, int height, double startAmp, double startFreq, double persistence, double ampFreqRatio, int times)
	{
		double[][][] averageLater = new double[times][width][height];
		for (int i = 0; i < times; i++)
		{
			averageLater[i] = generateNoise(width,height,startAmp*Math.pow(ampFreqRatio, i),(int)(startFreq/Math.pow(ampFreqRatio, i)));
		}
		double[][] b = averageTables(persistence,averageLater);
		b = positiveTable(b);
		return b;
	}
	
	public static double[][] recurInter(double[][] source, int times, double nDiv)
	{
		if (times < 0)
		{
			return source;
		}
		return recurInter(expand(source,nDiv),times-1,nDiv);
	}
	
	public static double[][] expand(double[][] a, double nDiv)
	{
		BicubicInterpolator bi = new BicubicInterpolator();
		double[][] returnThis = new double[(int)nDiv][(int)nDiv];
		for (int i = 0; i < nDiv; i++)
		{
			for (int j = 0; j < nDiv; j++)
			{
				double idx = (double)(a.length*i)/nDiv;
				double idy = (double)(a[0].length*j)/nDiv;
				double zeroCheck = bi.getValue(a,idx,idy);
				returnThis[i][j] = zeroCheck >= 0 ? zeroCheck : 0;
			}
		}
		return returnThis;
	}

	public double[][] positiveTable(double[][] a)
	{
		double[][] b = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++)
		{
			for (int j = 0; j < a[0].length; j++)
			{
				b[i][j] = Math.abs(a[i][j]);
			}
		}
		return b;
	}
	
	public double[][] scalar(double ratio, double[][] a)
	{
		for (int i = 0; i < a.length; i++)
		{
			for (int j = 0; j < a[0].length; j++)
			{
				a[i][j] *= ratio;
			}
		}
		return a;
	}

	public double[][] averageTables(double ratio, double[][][] a)
	{
		double[][] returnThis = new double[a[0].length][a[0][0].length];
		for (int tNum = 0; tNum < a.length; tNum++)
		{
			//System.out.println("kek");
			for (int i = 0; i < a[0].length; i++)
			{
				for (int j = 0; j < a[0][0].length; j++)
				{
					returnThis[i][j] += a[tNum][i][j]*(1/Math.pow(ratio, tNum-1));
				}
			}
		}
		return returnThis;
	}

	public double[][] generateNoise(int width, int height, double amp, int freq)
	{
		double[][] returnThis = new double[width][height];
		for (int i = 0; i < width; i += width/freq)
		{
			for (int j = 0; j < height; j += height/freq)
			{
				fillPatch(returnThis,i,j,width/freq,height/freq,random.nextDouble()*amp - amp/2);
			}
		}
		return returnThis;
	}

	public void fillPatch(double[][] arr, int a, int b, int w, int h, double number)
	{
		for (int i = a; i < a+w; i++)
		{
			for (int j = b; j < b+w; j++)
			{
				if (i < arr.length && j < arr[0].length)
					arr[i][j] = number;
			}
		}
	}

	public double dist(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
	}

	public double bInter(double[] distances, double[] values)
	{
		if (distances.length != values.length) return -1;
		double returnThis = 0;
		double sum = 0;
		for (int i = 0; i < distances.length; i++) sum += distances[i];
		for (int i = 0; i < distances.length; i++)
		{
			returnThis += values[i]*(sum-distances[i]);
		}
		returnThis /= sum;
		return returnThis;
	}
	
	public double[][] generate(double[] a) {
		double[][] source = makePerlinNoise((int)a[0],(int)a[1],a[2],a[3],a[4],a[5],(int)a[6]);
		source = PerlinNoise.expand(PerlinNoise.expand(source,a[7]/2),a[7]);
		return source;
	}

}
