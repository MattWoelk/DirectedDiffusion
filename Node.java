import java.util.ArrayList;
import java.sql.Date;

public class Node
{
  ArrayList<Packet> interests = new ArrayList<Packet>();       // interests to be sent along
  ArrayList<Packet> exploratoryData = new ArrayList<Packet>(); // exploratory data to be sent along
  ArrayList<Packet> reinforcements = new ArrayList<Packet>();  // reinforcements to be sent along
  ArrayList<Packet> reincorcedData = new ArrayList<Packet>();  // reinforced data to be sent along
  ArrayList<Data> generatedData = new ArrayList<Data>();   // to be sent as reinforced data or exploratory data

  ArrayList<Node> allNodes;                            // every node in the entire network
  ArrayList<Node> myNeighbors = new ArrayList<Node>(); // neighbouring nodes

  public int nodeID;
  public int xCoord;
  public int yCoord;
  public int radioRange;
  public int numNodes;

  public Node(int nodeID, int xCoord, int yCoord, int radioRange, int numNodes)
  {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.radioRange = radioRange;
    this.numNodes = numNodes;
  }

  public void setAllNodes(ArrayList<Node> allNodes)
  {
    this.allNodes = allNodes;
  }

  public void findNeighbors()
  {
    for(int i=0; i<numNodes; i++)
    {
      if(this.xCoord != allNodes.get(i).xCoord || this.yCoord != allNodes.get(i).yCoord)
      {
        int xDiff = Math.abs(allNodes.get(i).xCoord - this.xCoord);
        int yDiff = Math.abs(allNodes.get(i).yCoord - this.yCoord);

        if(xDiff <= radioRange && yDiff <= radioRange)
        {
          myNeighbors.add(allNodes.get(i));
        }
      }
    }
  }
}
