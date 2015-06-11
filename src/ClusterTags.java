// To keep all arrays of cluster tags in ArrayList structure after every iteration 
public class ClusterTags {
	int [] clusterTags;
	
	ClusterTags (int arr[], int size) {
		clusterTags = new int [size];
		for (int i = 0; i < arr.length; i++)
			clusterTags[i] = arr[i];
	}
}
