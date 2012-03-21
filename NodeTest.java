import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;
import java.sql.Date;
import java.io.*;
import java.util.Collection;
import java.util.List;

public class NodeTest
{
  static int dimension = 10, numNodes = 50, radioRange = 4; //will be set by user
  public static long currentTime = 0;
  static boolean lastonly = false;
  static boolean suppressOutput = false;

  public static OutputMaker maker;

  private static void ExtractArgs(String[] args)
  {
    for(String s : args)
    {
      if(s.startsWith("--filename="))
      {
        try
        {
          maker = new OutputMaker(s.substring(s.indexOf("=")+1));
        }catch(IOException e){maker=null;}
      }
      else if(s.startsWith("--filename-last-only="))
      {
        try
        {
          maker = new OutputMaker(s.substring(s.indexOf("=")+1));
          lastonly = true;
        }catch(IOException e){maker=null;}
      }
      else if(s.startsWith("--dimension="))
      {
        if(s.substring(s.indexOf("=")+1).length() != 0)
          dimension = Integer.parseInt(s.substring(s.indexOf("=")+1));
      }
      else if(s.startsWith("--numNodes="))
      {
        if(s.substring(s.indexOf("=")+1).length() != 0)
          numNodes = Integer.parseInt(s.substring(s.indexOf("=")+1));
      }
      else if(s.startsWith("--range="))
      {
        if(s.substring(s.indexOf("=")+1).length() != 0)
          radioRange = Integer.parseInt(s.substring(s.indexOf("=")+1));
      }
      else if(s.startsWith("--suppressOutput"))
      {
        suppressOutput = true;
      }
    }
  }

  public static void main(String[] args)
  {
    ExtractArgs(args);

    ArrayList<Point> gridPoints = new ArrayList<Point>();
    ArrayList<Node> allNodes = new ArrayList<Node>();

    // CREATE THE GRID
    for(int i = 0; i < dimension; i++)  //Initializes grid points
      for(int j = 0; j < dimension; j++)
        gridPoints.add(new Point(i,j));

    Collections.shuffle(gridPoints);

    // CREATE THE NODES
    for(int i = 0; i < numNodes; i++) //Make the Nodes, giving them a random coordinate
      allNodes.add(new Node(i, (int)gridPoints.get(i).getX(), (int)gridPoints.get(i).getY(), radioRange, numNodes, suppressOutput));

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

      if(!suppressOutput) {System.out.println("     | DONE ROUND: " + currentTime);}

      // Check if any nodes have work still to be done.
      keepgoing = false;
      for(Node nod : allNodes)
      {
        keepgoing = keepgoing || nod.isThereStillWorkToBeDone();
        if(maker!=null && !lastonly) { maker.AddOutput(currentTime + "," + nod.nodeID + "," + nod.nodeEnergyUsed + "\n");}
      }

      //increment time to the next time-stamp
      currentTime++;
      //      uncomment the next two lines to limit the length of the simulation
      //      if(currentTime == 5)
      //        keepgoing = false;
    }
    if(!keepgoing)
    {
      if(lastonly)
      {
        int numHops = 0;
        //calculate how many hops there were
        for(Node nod : allNodes)
        {
          if(nod.nodeEnergyUsed > 4)
            numHops++;
        }
        //add how many hops to the csv file
        if(maker!=null) { maker.AddOutput(numHops + "\n");}
        //add the rest of the output information to the file
        for(Node nod : allNodes)
        {
          if(maker!=null) { maker.AddOutput(nod.nodeID + "," + nod.nodeEnergyUsed + "\n");}
        }
      }
    }
    if(!suppressOutput) {System.out.println("\n\n~* The Simulation Is Over *~\n");}
    //if(maker!=null) { maker.AddOutput("Simulation Over."); }
    if(!suppressOutput) {System.out.println("Node Energy Uses Are As Follows:");}
    int energySum = 0;
    for(int i = 0; i < allNodes.size(); i++)
    {
      if(!suppressOutput) {System.out.println("Node: " + allNodes.get(i).nodeID + "\tenergy: " + allNodes.get(i).nodeEnergyUsed);}
      energySum+= allNodes.get(i).nodeEnergyUsed;
    }
    if(!suppressOutput) {System.out.println("Total Energy Used: " + energySum);}
    if(allNodes.get(0).myNeighbors.contains(allNodes.get(1)))
    {
      if(!suppressOutput) {System.out.println("Nodes 0 and 1 were right beside each other.");}
      //if(maker != null) { maker.AddOutput("Nodes 0 and 1 were right beside each other" + "\n"); }
    }
  }

  public static void assertTest(boolean test, String value)
  {
    if(test)
    {
      if(!suppressOutput) {System.out.println("- PASSED TEST: " + value);}
    }else{
      if(!suppressOutput) {System.out.println("==FAILED TEST: " + value);}
    }
  }
}
