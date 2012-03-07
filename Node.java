import java.util.ArrayList;
import java.sql.Date;
import java.math.*;

public class Node
{
  ArrayList<Packet> interests = new ArrayList<Packet>();       // interests to be sent along
  ArrayList<Packet> exploratoryData = new ArrayList<Packet>(); // exploratory data to be sent along
  ArrayList<Packet> reinforcements = new ArrayList<Packet>();  // reinforcements to be sent along
  ArrayList<Packet> reinforcedData = new ArrayList<Packet>();  // reinforced data to be sent along

  ArrayList<Packet> interestsSentAsTheSink = new ArrayList<Packet>();  // interests which initiated with this node
  ArrayList<Packet> interestsToRespondToAsTheSource = new ArrayList<Packet>();  // reinforced data to be sent from this Node, which is the source.
  boolean generating; // whether this node is generating data. Set at the beginning of the simulation by NodeTest
  DataType genType;   // the type of data being generated by this node. Set at the beginning of the simulation by NodeTest
  Data genData;       // the data which was generated at the current point in time.

  boolean doneSendingRequestedGeneratedData = true;
  int genAmount = 0;  // the number of requested pieces of data
  int genPeriod = 1;  // the period of requested pieces of data (1 means at every run(); 2 at every second; etc.).
  int genPeriodCounter = 0;
  long requestID = 0;

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

  public void run(long currentTime) //This runs at each time-stamp
  {
    //System.out.println("- - running Node: " + this);
    generateData();
    //send all interests in the queue.
    sendInterests();
    //send all expData
    sendExploratoryDataPacketThrough();
    //send all reinforments
    sendReinforcements();
    //send all reinforcedData
    sendReinforcedDataPacketThrough();
    //send one generated data if enough time has passed. This amount of time might be 1 unit, maybe 0, maybe way more.
    sendExploratoryDataAsSource();
    sendReinforcedDataAsSource();
  }

  public void startGeneration(DataType dType)
  {
    genType = dType;
    generating = true;
  }

  public void generateData()
  {
    if(generating)
    {
      genData = new Data((int)(Math.random()*256), genType);
    }
  }

  public void startInterest(int period, int amount, DataType type, long currentTime)
  {
    Packet pkt = new Packet(this, PacketType.INTEREST, currentTime, false, type, null, amount, period);
    interestsSentAsTheSink.add(pkt);
    System.out.println("oE");
    broadcast(pkt);
  }

  public void receivePacket(Packet pkt)
  {
    switch(pkt.pType)
    {
      case INTEREST:
        //see if we have one with this id already
        boolean haveAlready = false;
        for(Packet p : interests)
        {
          if(p.id == pkt.id)
          {
            //We already have this ID. Ignore this packet.
            haveAlready = true;
            break;
          }
        }
        if(haveAlready)
          break;

        //System.out.println("Send Interest along!");
        if(generating && genType == pkt.dType)
        {
          //It's for me; I am the source for this packet.
          //System.out.println("THIS INTEREST IS FOR ME!!! :D  HEE YAW!!! WOO HOO!!!!");
          interestsToRespondToAsTheSource.add(pkt.clone());
        }
        interests.add(pkt); //send it along whether it's for me or not.
        break;

      case EXPLORATORYDATA:
        //See if we already have exploratory data for this id.
        boolean foundAlready = false;
        for(Packet parket : exploratoryData)
        {
          if(parket.id == pkt.id)
          {
            //We already have this ID. Ignore this packet.
            foundAlready = true;
            break;
          }
        }
        if(foundAlready)
          break;

        //only ever send the first time we see this ID
        //see if this node originated the interest
        for(Packet p : interestsSentAsTheSink)
        {
          //see if this node started this id
          if(p.id == pkt.id)
          {
            //Send reinforcement.
            reinforcements.add(new Packet(this, PacketType.REINFORCEMENT, pkt.id, false, pkt.dType, null, pkt.requestedAmount, pkt.requestedPeriod));
            break;
          }
        }
        exploratoryData.add(pkt); //if it's for us or not, send it along, but only send the first one we see.

        break;

      case REINFORCEMENT:
        //See if we already have exploratory data for this id.
        boolean alreadyHave = false;
        for(Packet parket : reinforcements)
        {
          if(parket.id == pkt.id)
          {
            //We already have this ID. Ignore this packet.
            alreadyHave = true;
            break;
          }
        }
        if(alreadyHave)
          break;

        if(generating && genType == pkt.dType)
        {
          //It's for me; I am the source for this packet.
          //enable the sending of generated data as reinforced data, and sets its parameters.
          doneSendingRequestedGeneratedData = false;
          genAmount = pkt.requestedAmount;
          genPeriod = pkt.requestedPeriod;
          requestID = pkt.id;
          genPeriodCounter = 0;
        }
        reinforcements.add(pkt); //send it along whether it's for me or not.
        break;

      case REINFORCEDDATA:
        boolean foundIt = false;
        // See if the packet is for me
        for(Packet p : interestsSentAsTheSink)
        {
          if(p.id == pkt.id)
          {
            System.out.println("- - - -o  Sink received data: " + pkt.datum.datum + "\t id: " + pkt.id);
            foundIt = true;
            break;
          }
        }
        if(!foundIt)
        {
          reinforcedData.add(pkt);
        }
        break;

      default:
        System.out.println("Error: No Packet Type.");
        break;

    }
  }

  public void sendInterests()
  {
    // send all of the unsent interests.
    for(Packet pkt : interests)
    {
      if(pkt.ifsent == false)
      {
        System.out.println("-E");
        broadcast(pkt);
        pkt.ifsent = true;
      }
    }
  }

  public void sendExploratoryDataPacketThrough()
  {
    // send all of the unsent expData packets.
    for(Packet pkt : exploratoryData)
    {
      if(pkt.ifsent == false)
      {
        System.out.println("- -E");
        broadcast(pkt);
        pkt.ifsent = true;
      }
    }
  }

  public void sendReinforcements()
  {
    Node sendTo = null;
    // send all of the unsent reinforcements packets.
    //System.out.println("SIZE: "+ reinforcements.size());
    int siz = reinforcements.size();
    for(int i = 0; i < siz; i++)
    {
      //System.out.println(i + " " + reinforcements.get(i).ifsent + " " + reinforcements.get(i).id);
      if(reinforcements.get(i).ifsent == false)
      {
        //send to the node from the packet with this id from the exploratoryData list.
        for(Packet expkt : exploratoryData)
        {
          if(expkt.id == reinforcements.get(i).id)
          {
            sendTo = expkt.sender;
            //System.out.println("We found which to send reinf to: " + i);
          }
        }

        if(sendTo == null)
        {
          System.out.println("could not find who to send reinf to.");
          return;
        }
        if(reinforcements.get(i).sender == this)
        {
          System.out.println("o - -+");
        }else{
          System.out.println("- - -+");
        }
        monocast(reinforcements.get(i), sendTo);
        reinforcements.get(i).ifsent = true;
      }
    }
  }

  public void sendReinforcedDataPacketThrough()
  {
    Node sendTo = null;
    // send all of the unsent reinforcedData packets.
    for(Packet pkt : reinforcedData)
    {
      if(pkt.ifsent == false)
      {
        //send to the node from the packet with this id from the reinforcements list.
        for(Packet reinfpkt : reinforcements)
        {
          if(reinfpkt.id == pkt.id)
          {
            sendTo = reinfpkt.sender;
            //System.out.println("We found which to send reinfdata to.");
          }
        }

        if(sendTo == null)
        {
          System.out.println("could not find who to send reinfdata to.");
          return;
        }
        System.out.println("- - - -+");
        monocast(pkt, sendTo);
        pkt.ifsent = true;
      }
    }
  }

  public void sendExploratoryDataAsSource()
  {
    for(Packet pkt : interestsToRespondToAsTheSource)
    {
      if(pkt.ifsent == false)
      {
        //genPeriodCounter = 0;
        System.out.println("o -E");
        broadcast(new Packet(this, PacketType.EXPLORATORYDATA, requestID, false, genType, genData, pkt.requestedAmount, pkt.requestedPeriod));
        pkt.ifsent = true;
      }
    }

  }

  public void sendReinforcedDataAsSource()
  {
    Packet pkt = null;
    //This function will send data of type genType every genPeriod runs for a total of genAmount times.
    //check to see that we have received a request
    if(doneSendingRequestedGeneratedData)
      return;

    if(genAmount <= 0)
    {
      doneSendingRequestedGeneratedData = true;
      return;
    }

    genPeriodCounter++;

    if(genPeriodCounter % genPeriod != 0)
      return;

    genAmount--;

    Node sendTo = null;
    //send to the node from the packet with this id from the reinforcements list.
    for(Packet reinfpkt : reinforcements)
    {
      if(reinfpkt.id == requestID)
      {
        sendTo = reinfpkt.sender;
        pkt = reinfpkt;
        break;
        //System.out.println("We found which to send reinfdata to [from source].");
      }
    }

    if(sendTo == null)
    {
      System.out.println("could not find who to send reinfdata to [from source].");
      return;
    }
    System.out.println("o - - -+");
    monocast(new Packet(this, PacketType.REINFORCEDDATA, requestID, false, genType, genData, pkt.requestedAmount, pkt.requestedPeriod), sendTo);
  }

  public void broadcast(Packet pkt)
  {
    for(Node nod : myNeighbors)
    {
      nod.receivePacket(pkt.clone());
    }
  }

  public void monocast(Packet pkt, Node nod)
  {
    nod.receivePacket(pkt.clone());
  }

  public boolean isThereStillWorkToBeDone()
  {
    if(!doneSendingRequestedGeneratedData)
      return true;
    for(Packet pkt : interests)
    {
      if(!pkt.ifsent)
        return true;
    }
    for(Packet pkt : exploratoryData)
    {
      if(!pkt.ifsent)
        return true;
    }
    for(Packet pkt : reinforcements)
    {
      if(!pkt.ifsent)
        return true;
    }
    for(Packet pkt : reinforcedData)
    {
      if(!pkt.ifsent)
        return true;
    }
    return false;
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
