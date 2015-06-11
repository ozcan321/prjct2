import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class KMeans {

	public static int[] clusterTags2;
	public static double[][] SSE;
	public static double [] minSSE;
	public static ArrayList<ClusterTags> allClusterTags = new ArrayList<ClusterTags>();
	public static int [] numberOfMembers;

	public static void main(String[] args) {

		//Notification about console buffer size
		System.err.println("Özcan ÖZSAYIN - 201011038\n\tTo be able to see all outputs, please adequately increase Console Buffer Size (characters) [e.g. 800,000]\n\t\tfrom Window -> Preferences -> Run/Debug -> Console\n\t\t\tby marking Limit Console Output\n");

		int [] tags;

		String fileName = null;
		do {
			// fileName will be chosen from the user
			System.out.println("Housing.txt ('H') or Breast-Cancer.txt ('B') ?"); 
																				
			@SuppressWarnings("resource")
			Scanner input1 = new Scanner(System.in);
			String str = input1.nextLine();
			if (str.equals("H") || str.equals("h")) {
				fileName = "housing.txt";
				System.out.println(fileName + " is selected.");
			} else if (str.equals("B") || str.equals("b")) {
				fileName = "breast-cancer.txt";
				System.out.println(fileName + " is selected.");
			}

		} while (fileName == null);
		
		boolean enter = true;
		
		System.out.println("\nPlease enter k value. k = ?"); // k will be input from user
		@SuppressWarnings("resource")
		Scanner input2 = new Scanner(System.in);
		int k = input2.nextInt();
		
		System.out.println("\nHow many times program will run?"); // run times will be input
		@SuppressWarnings("resource")
		Scanner input3 = new Scanner(System.in);
		int times = input3.nextInt();
		int cntr = 0;
		minSSE = new double[times];

		
		while (cntr < times) {
			
			numberOfMembers = new int [k];
			for (int i = 0; i < k; i++)
				numberOfMembers[i] = 0;
			
			// Data objects are kept in ArrayList
			ArrayList<DataObjectHousing> objectsH = new ArrayList<DataObjectHousing>(); // Housing objects				
			ArrayList<DataObjectCancer> objectsC = new ArrayList<DataObjectCancer>(); // Breast-cancer objects

			DataObjectHousing objectH = null;
			DataObjectCancer objectC = null;

			BufferedReader br = null;

			try {

				String line;

				br = new BufferedReader(new FileReader("src\\" + fileName)); // Loading file

				int cn = 0;
				// Objects' attributes in file are updating to ArrayList's objects
				while ((line = br.readLine()) != null) {

					String[] splittedLine = line.split(","); // Data on each line are split according to commas

					double[] objectAtt = new double[splittedLine.length]; // line of text is converted to double array (objectAttrbts)

					for (int i = 0; i < splittedLine.length; i++) {
						objectAtt[i] = Double.parseDouble(splittedLine[i]);
					}

					if (fileName.equals("housing.txt")) {
						objectH = new DataObjectHousing(objectAtt, cn);
						objectsH.add(objectH);
					} else if (fileName.equals("breast-cancer.txt")) {
						objectC = new DataObjectCancer(objectAtt, cn);
						objectsC.add(objectC);
					}
					cn++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(enter)
					System.out.println("\n" + fileName + " is loaded.");
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			
			// Objects are shown respectively and first centroids are created
			if (fileName.equals("housing.txt")) {
				
				Centroids.k = k;
				SSE = new double[100][k];

				if (enter) {
					System.out.println("\nOBJECTS :");
					System.out.println("---------");
					for (int i = 0; i < objectsH.size(); i++) {
						System.out.print("Object ");
						System.out.printf("%3d", i);
						System.out.print(": ");
						for (int j = 0; j < 14; j++) {
							System.out.printf("%10.4f",
									+ objectsH.get(i).attributes[j]);
						}
						System.out.println();
					}
					enter = false;
				}
				
				// First centroids are created
				DataObjectHousing.createFirstCentroids(objectsH);
				DataObjectHousing.clusterTags = new int[objectsH.size()];
				clusterTags2 = new int[objectsH.size()];
				

			} else if (fileName.equals("breast-cancer.txt")) {		// The same things are done for breast-cancer objects if fileName = "breast-cancer"
				
				Centroids.k = k;
				SSE = new double[100][k];
				DataObjectCancer.formatAtts(objectsC);

				if (enter) {
					System.out.println("\nOBJECTS :");
					System.out.println("---------");
					for (int i = 0; i < objectsC.size(); i++) {
						System.out.print("Object ");
						System.out.printf("%3d", i);
						System.out.print(": ");
						for (int j = 0; j < 10; j++) {
							System.out.printf("%10.4f",
									+ objectsC.get(i).attributes[j]);
						}
						System.out.println();
					}
					enter = false;
				}
				
				// First centroids are created
				DataObjectCancer.createFirstCentroids(objectsC);
				DataObjectCancer.clusterTags = new int[objectsC.size()];
				clusterTags2 = new int[objectsC.size()];
						
			} else
				System.out.println("Error occured, fileName is not valid");

			
			// Finding cluster tags after creation of first centroids
			if (fileName.equals("housing.txt")) {
				for (int i = 0; i < objectsH.size(); i++)
					DataObjectHousing.clusterTags[i] = (objectsH.get(i).findClusterTag());
				
				tags = new int [objectsH.size()];
				for (int i = 0; i < objectsH.size(); i++)
					tags[i] = DataObjectHousing.clusterTags[i];
				ClusterTags ct = new ClusterTags(tags, objectsH.size());	
				// After assessment of the cluster tags, they will be added into allClusterTags ArrayList
				allClusterTags.add(ct);

				double[] sse1 = new double[k];
				sse1 = DataObjectHousing.findSSE(objectsH);
				for (int j = 0; j < k; j++)
					SSE[0][j] = sse1[j];
				
				
			} else if (fileName.equals("breast-cancer.txt")) {
				for (int i = 0; i < objectsC.size(); i++)
					DataObjectCancer.clusterTags[i] = (objectsC.get(i).findClusterTag());

				tags = new int [objectsC.size()];
				for (int i = 0; i < objectsC.size(); i++)
					tags[i] = DataObjectCancer.clusterTags[i];
				ClusterTags ct = new ClusterTags(tags, objectsC.size());	
				// After assessment of the cluster tags, they will be added into allClusterTags ArrayList
				allClusterTags.add(ct);
				
				double[] sse1 = new double[k];
				sse1 = DataObjectCancer.findSSE(objectsC);
				for (int j = 0; j < k; j++)
					SSE[0][j] = sse1[j];
				
			} else
				System.out.println("Error occured, fileName is not valid");

			
			int cnt = 1;
			// New centroids are created according to new changing cluster tags
			if (fileName.equals("housing.txt")) {
				
				while (!Arrays.equals(clusterTags2, DataObjectHousing.clusterTags)) {
					
					for (int i = 0; i < objectsH.size(); i++)
						clusterTags2[i] = DataObjectHousing.clusterTags[i];
					DataObjectHousing.createNewCentroids(objectsH);
					for (int i = 0; i < objectsH.size(); i++)
						DataObjectHousing.clusterTags[i] = (objectsH.get(i).findClusterTag());

					// Find SSEs for each iterations SSE[][]
					double[] sse = new double[k];
					sse = DataObjectHousing.findSSE(objectsH);
					for (int j = 0; j < k; j++)
						SSE[cnt][j] = sse[j];
					cnt++;
					
					tags = new int [objectsH.size()];
					for (int i = 0; i < objectsH.size(); i++)
						tags[i] = DataObjectHousing.clusterTags[i];
					ClusterTags ct = new ClusterTags(tags, objectsH.size());				
					// After assessment of the cluster tags, they will be added into allClusterTags ArrayList
					allClusterTags.add(ct);

				}
				
				System.out.println("\n\n\n-------------------------"); // Shows number of which sequence is on
				System.out.println("\t" + (cntr+1) + ". RUN:");
				System.out.println("-------------------------");
				
				
				// Shows centroids for each iteration
				for (int i = 0; i < DataObjectHousing.centroids.size(); i++) { 	// centroids.size() = number of iterations
					System.out.println("\n" + (i + 1) + ". Iteration's Centroids: ");
					for (int m = 0; m < k; m++) {
						System.out.print("\tCentroid " + (m + 1) + ": ");
						for (int j = 0; j < 14; j++)
							System.out.printf("%10.4f", (DataObjectHousing.centroids.get(i).attrbts[m][j]));
					
						if (i == DataObjectHousing.centroids.size() - 1) {
							for (int y = 0; y < objectsH.size(); y++)
								if (allClusterTags.get(i).clusterTags[y] == m)
									numberOfMembers[m]++;
						}
						
						if (i == DataObjectHousing.centroids.size() - 1) 
							System.out.print("\tNumber of members: " + numberOfMembers[m]);
						
						System.out.print("\tConnected objects of IDs: ");
						for (int y = 0; y < objectsH.size(); y++)
							if (allClusterTags.get(i).clusterTags[y] == m)
								System.out.print(y + " ");
						
						System.out.println();
					}
					System.out.println();

					if (SSE[i][0] != 0) {
						for (int h = 0; h < k; h++)
							System.out.print("\tSSE C" + (h + 1) + ": "
									+ SSE[i][h] + " ");

						double totalSSE = 0;
						for (int p = 0; p < k; p++)
							totalSSE += SSE[i][p];

						System.out.println("\n\tTotal SSE" + ": " + totalSSE);
						
						minSSE[cntr] = totalSSE;
					}
				}
				
				NumberFormat df = DecimalFormat.getInstance(); // 4 digit decimal format
				df.setMaximumFractionDigits(4);
				df.setMinimumFractionDigits(4);
				
				System.out.println("\n\tCluster Distributions (%):");
				for (int i = 0; i < k; i++)
					System.out.print("\tC" + (i+1) + ": " + df.format((double)numberOfMembers[i]/(double)objectsH.size()) + "\t");
			}
			// New centroids are created according to new changing cluster tags
			else if (fileName.equals("breast-cancer.txt")) {
				while (!Arrays.equals(clusterTags2,
						DataObjectCancer.clusterTags)) {
					for (int i = 0; i < objectsC.size(); i++)
						clusterTags2[i] = DataObjectCancer.clusterTags[i];
					DataObjectCancer.createNewCentroids(objectsC);
					for (int i = 0; i < objectsC.size(); i++)
						DataObjectCancer.clusterTags[i] = (objectsC.get(i).findClusterTag());

					// Find SSEs for each iterations SSE[][]
					double[] sse = new double[k];
					sse = DataObjectCancer.findSSE(objectsC);
					for (int j = 0; j < k; j++)
						SSE[cnt][j] = sse[j];
					cnt++;
					
					tags = new int [objectsC.size()];
					for (int i = 0; i < objectsC.size(); i++)
						tags[i] = DataObjectCancer.clusterTags[i];
					ClusterTags ct = new ClusterTags(tags, objectsC.size());				
					// After assessment of the cluster tags, they will be added into allClusterTags ArrayList
					allClusterTags.add(ct);
				}		
				
				System.out.println("\n\n\n-------------------------"); // Shows which run is going
				System.out.println("\t" + (cntr+1) + ". RUN:");
				System.out.println("-------------------------\n");
				
				for (int i = 0; i < DataObjectCancer.centroids.size(); i++) {	// centroids.size() = number of iterations
					System.out.println((i + 1) + ". Iteration's Centroids: ");
					for (int m = 0; m < k; m++) {
						System.out.print("\tCentroid " + (m + 1) + ": ");
						for (int j = 0; j < 10; j++)
							System.out.printf("%10.4f", 
									(DataObjectCancer.centroids.get(i).attrbts[m][j]));
						
						if (i == DataObjectCancer.centroids.size() - 1) {
							for (int y = 0; y < objectsC.size(); y++)
								if (allClusterTags.get(i).clusterTags[y] == m)
									numberOfMembers[m]++;
						}
						
						if (i == DataObjectCancer.centroids.size() - 1) 
							System.out.print("\tNumber of members: " + numberOfMembers[m]);
						
						System.out.print("\tConnected objects of IDs: ");
						for (int y = 0; y < objectsC.size(); y++)
							if (allClusterTags.get(i).clusterTags[y] == m)
								System.out.print(y + " ");
						
						System.out.println();
					}
					System.out.println();

					if (SSE[i][0] != 0) {
						for (int h = 0; h < k; h++)
							System.out.print("\tSSE C" + (h + 1) + ": "
									+ SSE[i][h] + " ");

						double totalSSE = 0;
						for (int p = 0; p < k; p++)
							totalSSE += SSE[i][p];

						System.out.println("\n\tTotal SSE" + ": " + totalSSE
								+ "\n");
						
						minSSE[cntr] = totalSSE;
					}

				}
				
				NumberFormat df = DecimalFormat.getInstance(); // 4 digit decimal format
				df.setMaximumFractionDigits(4);
				df.setMinimumFractionDigits(4);
				
				System.out.println("\tCluster Distributions (%):");
				for (int i = 0; i < k; i++)
					System.out.print("\tC" + (i+1) + ": " + df.format((double)numberOfMembers[i]/(double)objectsC.size()) + "\t");
				
			} else
				System.out.println("Error occured, fileName is not valid");
			
			cntr++;
		}
		
		// Finds the minimum total SSE // Finds the best iteration in which creates the best clusters 
		double min = minSSE[0];
		int minIndex = 0;
		for(int i = 1; i < cntr; i++)
			if (min > minSSE[i]) {
				min = minSSE[i];
				minIndex = i;
			}
		
		// Shows the best run and its best clusters
		System.out.println("\n\n\n\n\n\t-------------------------------------------");
		System.out.println("\tTHE BEST CLUSTERS ARE CREATED IN " + (minIndex+1) + 
				". RUN. \n\tTHIS BEST CLUSTERS' TOTAL SSE IS " + min);
		System.out.println("\t-------------------------------------------\n");

	}
}
