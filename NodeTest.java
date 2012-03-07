import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;
import java.sql.Date;

public class NodeTest
{
  static int dimension = 10, numNodes = 50, radioRange = 4; //will be set by user
  public static long currentTime = 0;

  public static void main(String[] args)
  {
    ArrayList<Point> gridPoints = new ArrayList<Point>();
    ArrayList<Node> allNodes = new ArrayList<Node>();

   // CREATE THE GRID
    for(int i = 0; i < dimension; i++)  //Initializes grid points
      for(int j = 0; j < dimension; j++)
        gridPoints.add(new Point(i,j));

    Collections.shuffle(gridPoints);

   // CREATE THE NODES
    for(int i = 0; i < numNodes; i++) //Make the Nodes, giving them a random coordinate
      allNodes.add(new Node(i, (int)gridPoints.get(i).getX(), (int)gridPoints.get(i).getY(), radioRange, numNodes));

    for(int i = 0; i < numNodes; i++)
    { //Each node knows all nodes
      allNodes.get(i).setAllNodes(allNodes);
    }

    for(int i = 0; i < numNodes; i++)
    { //Each node finds its neighbors
      allNodes.get(i).findNeighbors();
    }

    // comment out the following two lines for easier testing; uncomment them for a more stochastic process.
    //    Collections.shuffle(allNodes);  //Randomizes the order of the nodes in the list.
    //    System.out.println("Shuffled nodes.");

   // CREATE A SOURCE AND A SINK: //
    //the order of the next two commands are important.
    //seed another node with generatedData (tell it that it makes a certain type of data)
    allNodes.get(1).startGeneration(DataType.TYPEA);
    //seed the first node with an interest
    allNodes.get(0).startInterest(2,10,DataType.TYPEA,currentTime);

   // RUN THE SIMULATION
    boolean keepgoing = true; //whether we are not done the simulation.
    while(keepgoing)
    {
      for(Node nod : allNodes)
      {
        // Do all of the sending and receiving for each node.
        nod.run(currentTime);
      }

      System.out.println("     | DONE ROUND: " + currentTime);

      // Check if any nodes have work still to be done.
      keepgoing = false;
      for(Node nod : allNodes)
      {
        keepgoing = keepgoing || nod.isThereStillWorkToBeDone();
      }

      //increment time to the next time-stamp
      currentTime++;
//      uncomment the next two lines to limit the length of the simulation
//      if(currentTime == 5)
//        keepgoing = false;
    }
    System.out.println("\n\n~* The Simulation Is Over *~\n");
    System.out.println("Node Energy Uses Are As Follows:");
    int energySum = 0;
    for(int i = 0; i < allNodes.size(); i++)
    {
      System.out.println("Node: " + allNodes.get(i).nodeID + "\tenergy: " + allNodes.get(i).nodeEnergyUsed);
      energySum+= allNodes.get(i).nodeEnergyUsed;
    }
    System.out.println("Total Energy Used: " + energySum);
    if(allNodes.get(0).myNeighbors.contains(allNodes.get(1)))
      System.out.println("Nodes 0 and 1 were right beside each other.");
  }

  public static void assertTest(boolean test, String value)
  {
    if(test)
    {
      System.out.println("- PASSED TEST: " + value);
    }else{
      System.out.println("==FAILED TEST: " + value);
    }
  }
}
