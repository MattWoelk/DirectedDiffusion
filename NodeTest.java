import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;
import java.sql.Date;

public class NodeTest
{
  static int dimension = 4, numNodes = 10, radioRange = 4; //will be set by user

  public static void main(String[] args)
  {
    String tmp = args[0];
    if(tmp.toLowerCase().equals("unittest"))
    {
      if(args.length >= 2)
        runUnitTests(args[1]);
      else
        runUnitTests("");
    }else{
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
  }

  public static void runUnitTests(String arg)
  {
    String tmp = arg;
    boolean all = false;
    if(tmp.toLowerCase().equals("all"))
      all = true;

    boolean testNode = all;
    boolean testEDReceive = all;
    boolean testExpDataSend = all;
    boolean testPacketReceiver = all;
    boolean testPacketTransmitter = all;
    boolean testRnFDataSend = all;

    System.out.println("----TESTING!----");

    if(testNode || arg.toLowerCase().equals("node"))
    {
      System.out.println("\n,.~*TESTING NODE!*~.,\n");
      ArrayList<Node> nods = new ArrayList<Node>();
      System.out.println("Testing Node neighbouring:");
      nods.add(new Node(0, 0, 0, 10, 2));
      nods.add(new Node(1, 1, 0, 10, 2));
      nods.get(0).setAllNodes(nods);
      nods.get(1).setAllNodes(nods);
      nods.get(0).findNeighbors();
      nods.get(1).findNeighbors();

      assertTest(nods.get(0).getXCoord() == 0, "xco1");
      assertTest(nods.get(0).getYCoord() == 0, "yco1");
      assertTest(nods.get(1).getXCoord() == 1, "xco2");
      assertTest(nods.get(1).getYCoord() == 0, "yco2");
      assertTest(nods.get(0).getNeighbours().get(0) == nods.get(1), "neib1");
      assertTest(nods.get(1).getNeighbours().get(0) == nods.get(0), "neib2");
      assertTest(nods.get(0).getNeighbours().size() == 1, "neibcount1");
      assertTest(nods.get(1).getNeighbours().size() == 1, "neibcount2");
    }

    if(testEDReceive || arg.toLowerCase().equals("edreceive"))
    {
      System.out.println("\n,.~*TESTING EDReceive!*~.,\n");
      EDReceive edTest = new EDReceive();
      assertTest(edTest.getSeqNum(0) == -1, "nosenders");
      edTest.addSender(42);
      assertTest(edTest.getSeqNum(0) != -1, "onesender");
      edTest.updateSeqNum(0, 99);
      assertTest(edTest.getSeqNum(0) == 99, "updateseqnum");
    }

    if(testExpDataSend || arg.toLowerCase().equals("expdatasend"))
    {
      System.out.println("\n,.~*TESTING ExpDataSend!*~.,\n");
      ArrayList<Node> nods = new ArrayList<Node>();
      nods.add(new Node(0, 0, 0, 10, 2));
      nods.add(new Node(1, 1, 0, 10, 2));
      nods.get(0).setAllNodes(nods);
      nods.get(1).setAllNodes(nods);
      nods.get(0).findNeighbors();
      nods.get(1).findNeighbors();
      ExpDataSend exTest = new ExpDataSend();
      exTest.EDSendFlag();
    }

    if(testPacketReceiver || arg.toLowerCase().equals("packetreceiver"))
    {
      System.out.println("\n,.~*TESTING PacketReceiver!*~.,\n");
      PacketReceiver prec = new PacketReceiver();
      Packet pkt = new Packet('a', "yup", (double)0, new Date((long)0));
      prec.receivePacket(pkt);
      Packet pkt2 = prec.getPacket();
      assertTest(pkt2 == pkt, "receive");
      assertTest(prec.check_empty(), "empty");
    }

    if(testPacketTransmitter || arg.toLowerCase().equals("packettransmitter"))
    {
      System.out.println("\n,.~*TESTING PacketTransmitter!*~.,\n");
      PacketTransmitter ptra = new PacketTransmitter();
      Packet pkt = new Packet('a', "yup", (double)0, new Date((long)0));

      ArrayList<Node> nods = new ArrayList<Node>();
      nods.add(new Node(0, 0, 0, 10, 2));
      nods.add(new Node(1, 1, 0, 10, 2));
      nods.get(0).setAllNodes(nods);
      nods.get(1).setAllNodes(nods);
      nods.get(0).findNeighbors();
      nods.get(1).findNeighbors();

      ptra.setNeighbors(nods.get(0).getNeighbours());
      ptra.addPacket(pkt);
      assertTest(nods.get(1).lastReceivedPacketTest() == pkt, "received packet");
    }

    if(testRnFDataSend || arg.toLowerCase().equals("rnfdatasend"))
    {
      System.out.println("\n,.~*TESTING RnfDataSend!*~.,\n");
      ArrayList<Node> nods = new ArrayList<Node>();
      nods.add(new Node(0, 0, 0, 10, 2));
      nods.add(new Node(1, 1, 0, 10, 2));
      nods.get(0).setAllNodes(nods);
      nods.get(1).setAllNodes(nods);
      nods.get(0).findNeighbors();
      nods.get(1).findNeighbors();
      RnFDataSend exTest = new RnFDataSend();
      exTest.RnFSendFlag();
    }
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
