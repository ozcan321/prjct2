
public class Centroids {
	public static int k;
	public double[][] attrbts;		// Each 'Centroids' object has k centroids
	
	Centroids()
	{
		k = -1;
		attrbts = new double [1][1];
	}
	
	Centroids(double[][] att, int attLength)
	{
		attrbts = new double [k][attLength];
		attrbts = att.clone();
	}
}
