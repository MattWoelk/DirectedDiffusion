import java.util.ArrayList;

public class Node
{
  int nodeID, xCoord, yCoord, radioRange, numNodes, neighborSize;
  ArrayList<Node> allNodes = new ArrayList<Node>();
  ArrayList<Node> myNeighbors = new ArrayList<Node>();
  EDReceive ERec = new EDReceive();
  PacketTransmitter PTrans = new PacketTransmitter();
  PacketReceiver PRec = new PacketReceiver();
  RnFDataSend RSend = new RnFDataSend();
  ExpDataSend ESend = new ExpDataSend();
  ArrayList<Gradient> Gradients = new ArrayList<Gradient>();

  public Node(int nodeID, int xCoord, int yCoord, int radioRange, int numNodes)
  {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.radioRange = radioRange;
    this.numNodes = numNodes;
    System.out.print("Node: "+nodeID+" has been created: ("+this.xCoord+","+this.yCoord+")\n");
    //System.out.print("numNodes = "+this.numNodes);
  }

  public int getID()
  {
    return this.nodeID;
  }

  public int getXCoord()
  {
    return this.xCoord;
  }

  public int getYCoord()
  {
    return this.yCoord;
  }

  public void setAllNodes(ArrayList<Node> allNodes)
  {
    this.allNodes = allNodes;
  }

  public void findNeighbors()
  {
    for(int i=0; i<numNodes; i++)
    {
      if(this.xCoord != allNodes.get(i).xCoord && this.yCoord != allNodes.get(i).yCoord)
      {
        int xDiff = Math.abs(allNodes.get(i).xCoord - this.xCoord);
        int yDiff = Math.abs(allNodes.get(i).yCoord - this.yCoord);
        //System.out.print("xDiff = "+xDiff+"\n");
        if(xDiff <= radioRange && yDiff <= radioRange)
        {
          myNeighbors.add(allNodes.get(i));
          //System.out.print("Found Neighbor: "+ allNodes.get(i).xCoord+","+allNodes.get(i).yCoord+ ")\n");
        }
      }
    }

    int neighborSize = myNeighbors.size();
    if(neighborSize==0)
    {
      System.out.print("Neighbor size = "+neighborSize+" Exit now.\n");
      System.exit(0);
    }else{
      System.out.print("Neighbor size = "+neighborSize+"\n");
    }

    System.out.print("Neigbors of Node ("+ xCoord+","+yCoord+ "):\n");
    for(int i=0; i<neighborSize; i++)
    {
      System.out.print("\t("+ myNeighbors.get(i).getXCoord()+","+myNeighbors.get(i).getYCoord()+ ")\n");
    }

    PTrans.setNeighbors(myNeighbors);
  }

  public boolean processNode()
  {
    System.out.print("Node = "+this.nodeID+" is processing its node"+"\n");
    return false; //or true depending on if processing was successful
  }

  public void broadcastInterestMessage(Interest intr)
  {
    Packet pkt = new Packet();
    //TODO: build broad interest message to be sent to PacketTransmitter
//    PTrans.addPacket(pkt);
  }

  public void receivePacket(Packet pkt)
  {
    PRec.receivePacket(pkt);
  }
}
