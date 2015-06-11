
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class DataObjectCancer extends Centroids{

	double [] attributes = new double [10];
	double centroidDistance;
	static ArrayList<Centroids> centroids;
	static double[][] firstCentroids;
	int clusterIndex;										// cluster no the data object belongs
	static int [] clusterTags;
	static int iterations;
	int ID;
	
	DataObjectCancer (double[] att, int id) {
		this.attributes = att;
		this.centroidDistance = -1;
		centroids = new ArrayList<Centroids>();
		iterations = 0;
		ID = id;
	}
	public static void formatAtts(ArrayList<DataObjectCancer> objects){
		double indexAtt[][] = new double [10][objects.size()];
		
		
		for (int i = 0; i < 10; i++)
			for(int j = 0; j < objects.size(); j++)
				indexAtt[i][j] = objects.get(j).attributes[i];
		
		double sum = 0;
		int size = 0;
		for (int i = 0; i < 10; i++){
			sum = 0;
			size = 0;
			for(int j = 0; j < objects.size(); j++){
				String s = String.valueOf(objects.get(j).attributes[i]);
				if ( !s.equals("NaN") ){
					sum += indexAtt[i][j];
					size ++;
				}
				else
					indexAtt[i][j] = -1;
			}
			for (int k = 0; k < objects.size(); k++)
				if (indexAtt[i][k] == -1)
				{
					indexAtt[i][k] = sum / size;
				}
		}
		
		for (int i = 0; i < objects.size(); i++)
			for(int j = 0; j < 10; j++)
				objects.get(i).attributes[j] = indexAtt[j][i];
	}
	
	public static void createFirstCentroids(ArrayList<DataObjectCancer> objects){

		firstCentroids = new double [k][10];
		
		Random rand = new Random();
		boolean flag;
		int[] random = new int [k];
		int r;
		for(int i = 0; i < k; i++){							
			do{
			flag = false;
			r = rand.nextInt(objects.size()) + 0;			
			for (int j = 0; j < k; j++)
				if (r == random[j])
					flag = true;
			}while(flag);
			random[i] = r;
		}

		Arrays.sort(random);
				
		for (int i = 0; i < k; i++)										
			for (int j = 0; j < 10; j++)
				firstCentroids[i][j] = objects.get(random[i]).attributes[j];
		
		Centroids c = new Centroids(firstCentroids, 10);
		centroids.add(c);												
		
	}
	
	
	public int findClusterTag()
	{
		int sum = 0;
		double [] distinction = new double [k];
		for (int i = 0; i < k; i++){
			for (int j = 0; j < 10; j++)
				sum += Math.pow(((double)attributes[j] - (double)centroids.get(centroids.size() - 1).attrbts[i][j]) , 2);
			distinction[i] = Math.sqrt(sum);
			sum = 0;
		}
		double min = distinction[0];
		int minIndex = 0;
		for (int i = 1; i < k; i++)
			if (min > distinction[i]){
				min = distinction[i];
				minIndex = i;
			}

		centroidDistance = min;
		clusterIndex = minIndex;
		
		return clusterIndex;
	}
	
	public static void createNewCentroids(ArrayList<DataObjectCancer> objects)
	{
		ArrayList<DataObjectCancer> cluster;
		Centroids c;
		double [][] centroidsAtt = new double [k][10];			
		double [][] indexAtt;		
		
		for (int i = 0; i < k; i++){
			cluster = new ArrayList<DataObjectCancer>();
			for (int j = 0; j < objects.size(); j++)		
				if(objects.get(j).clusterIndex == i)
					cluster.add(objects.get(j));
			indexAtt = new double[10][cluster.size()];
			for (int m = 0; m < 10; m++)		
				for (int n = 0; n < cluster.size(); n++){
					indexAtt[m][n] = cluster.get(n).attributes[m];
				}
			double sum, avg;
			for (int x = 0; x < 10; x++){					
				sum = 0;
				for (int y = 0; y < cluster.size(); y++){
					sum += indexAtt[x][y];
				}
				avg = sum / cluster.size();
				centroidsAtt[i][x] = avg;
			}
		}
		
		c = new Centroids (centroidsAtt, 10);
		centroids.add(c);									// New centroids c are added to primary 
	}
	
	static double[] findSSE(ArrayList<DataObjectCancer> objects){
		double []SSE = new double [k];
		ArrayList<DataObjectCancer> clusterMembers = new ArrayList<DataObjectCancer>();
		
		for (int i = 0; i < k; i++){
			for (int j = 0; j < objects.size(); j++)
				if (objects.get(j).clusterIndex == i)
					clusterMembers.add(objects.get(j));
			for (int j = 0; j < clusterMembers.size(); j++)
				SSE[i] += Math.pow(clusterMembers.get(j).centroidDistance , 2);
		}
		
		return SSE;
	}

}
