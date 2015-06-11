
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class DataObjectHousing extends Centroids{
	
	double [] attributes = new double [14];
	double centroidDistance;
	static ArrayList<Centroids> centroids;
	static double[][] firstCentroids;
	int clusterIndex;										// cluster no the data object belongs
	static int [] clusterTags;
	static int iterations;
	int ID;
	static int[] numberOfMembers;
	
	DataObjectHousing (double[] att, int id) {
		this.attributes = att;
		this.centroidDistance = -1;
		centroids = new ArrayList<Centroids>();
		iterations = 0;
		ID = id;
		
	}
	
	public static void createFirstCentroids(ArrayList<DataObjectHousing> objects){
		
		firstCentroids = new double [k][14];
		
		numberOfMembers = new int[k];
		for (int i = 0; i < k; i++)
			numberOfMembers[i] = 0;
		
		Random rand = new Random();
		boolean flag;
		int[] random = new int [k];
		int r;
		for(int i = 0; i < k; i++){							// assign random firstCentroids into random[k]
			do{
			flag = false;
			r = rand.nextInt(objects.size()) + 0;			// minimum = 0 , max = objects.size()
			for (int j = 0; j < k; j++)
				if (r == random[j])
					flag = true;
			}while(flag);
			random[i] = r;
		}

		Arrays.sort(random);
				
		for (int i = 0; i < k; i++)										// firstCentroids array is filled
			for (int j = 0; j < 14; j++)
				firstCentroids[i][j] = objects.get(random[i]).attributes[j];
		
		Centroids c = new Centroids(firstCentroids, 14);
		centroids.add(c);												// First centroids are added to "centroids" ArrayList
		
		iterations++;
	}
	
	// Find which object belongs which cluster with euclidean distance
	public int findClusterTag()
	{		
		int sum = 0;
		double [] distinction = new double [k];
		for (int i = 0; i < k; i++){
			for (int j = 0; j < 14; j++)
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
		numberOfMembers[clusterIndex]++;
		
		return clusterIndex;
	}
	
	public static void createNewCentroids(ArrayList<DataObjectHousing> objects)
	{		
		for (int i = 0; i < k; i++)
			numberOfMembers[i] = 0;
		
		ArrayList<DataObjectHousing> cluster;
		Centroids c;
		double [][] centroidsAtt = new double [k][14];			// Keeps attributes of centroids to add centroids ArrayList
																// centroidsAtt[][] is equivalent to Centroids.attrbts[][]
		double [][] indexAtt;									// Each row keeps the attributes of each object. 
																// It is for getting average of the same type of attributes
		
		for (int i = 0; i < k; i++){
			cluster = new ArrayList<DataObjectHousing>();
			for (int j = 0; j < objects.size(); j++)		// For each cluster (or k value), temporary cluster ArrayList are created
				if(objects.get(j).clusterIndex == i)
					cluster.add(objects.get(j));
			indexAtt = new double[14][cluster.size()];
			for (int m = 0; m < 14; m++)					// Creating indexAtt[][]
				for (int n = 0; n < cluster.size(); n++){
					indexAtt[m][n] = cluster.get(n).attributes[m];
				}
			double sum, avg;
			for (int x = 0; x < 14; x++){					// average of same type attributes of objects are assigned to centroidsAtt[][]
				sum = 0;
				for (int y = 0; y < cluster.size(); y++){
					sum += indexAtt[x][y];
				}
				avg = sum / cluster.size();
				centroidsAtt[i][x] = avg;
			}
		}
		
		c = new Centroids (centroidsAtt, 14);				// 'Centroids' class construction 
															// Each 'Centroids' object keeps k centroids on each iteration
		centroids.add(c);						 // New created centroid 'c' is added to the primary 'centroids' ArrayList
			
		iterations++;
	}
	
	// Finds SSE of each cluster
	static double[] findSSE(ArrayList<DataObjectHousing> objects){
		double []SSE = new double [k];
		ArrayList<DataObjectHousing> clusterMembers = new ArrayList<DataObjectHousing>();
		
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
