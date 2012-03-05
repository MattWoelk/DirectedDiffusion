import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;
import java.sql.Date;

public class NodeTest
{
  static int dimension = 4, numNodes = 10, radioRange = 4; //will be set by user
  public static int currentTime = 0;

  public static void main(String[] args)
  {
    ArrayList<Point> gridPoints = new ArrayList<Point>();
    ArrayList<Node> allNodes = new ArrayList<Node>();

    for(int i = 0; i < dimension; i++)  //Initializes grid points
      for(int j = 0; j < dimension; j++)
        gridPoints.add(new Point(i,j));

    Collections.shuffle(gridPoints);

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

    Collections.shuffle(allNodes);  //Randomizes the order of the nodes in the list.
    System.out.println("Shuffled nodes.");
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
