import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class NodeTest
{
  static int dimension = 4, numNodes = 10, radioRange = 4; //will be set by user

  public static void main(String[] args)
  {
    ArrayList<Point> gridPoints = new ArrayList<Point>();
    ArrayList<Node> allNodes = new ArrayList<Node>();

    for(int i = 0; i < dimension; i++)  //Initializes grid points
      for(int j = 0; j < dimension; j++)
        gridPoints.add(new Point(i,j));

    //this debug code showing the effect of Collections.shuffle can be removed later
    System.out.println("Before shuffling, gridPoints contains:\n");

    for(int i = 0; i < dimension*dimension; i++)
      System.out.println("\t("+(int)gridPoints.get(i).getX()+","+(int)gridPoints.get(i).getY()+")");

    Collections.shuffle(gridPoints);
    System.out.println("After shuffling, gridPoints contains:\n");

    for(int i = 0; i < dimension*dimension; i++)
      System.out.println("\t("+(int)gridPoints.get(i).getX()+","+(int)gridPoints.get(i).getY()+")");

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

    while(true)
    {
        System.out.println("Shuffling nodes:\n");
      Collections.shuffle(allNodes);  //Randomizes the order of the nodes in the list.
      for(int i = 0; i < numNodes; i++)
      {  //Invoke the processNode method of each node
        boolean success = allNodes.get(i).processNode();
      }
      sleep(4000);   // sleep in ms
    }
  }
  public static void sleep(int ms)
  {
      try
      {
        Thread.sleep(ms);
      }catch (InterruptedException ie){
        System.out.println(ie.getMessage());
      }
  }
}
