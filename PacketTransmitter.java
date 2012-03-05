import java.util.ArrayList;

public class PacketTransmitter
{
  ArrayList<Node> myNeighbors;
  ArrayList<Packet> txPkt;

  public PacketTransmitter()
  {
    txPkt = new ArrayList<Packet>();
  }

  public void setNeighbors(ArrayList<Node> myNeighbors)
  {
    this.myNeighbors = myNeighbors;
  }

  public void addPacket(Packet pkt)
  {
    txPkt.add(pkt);
    for(int i=0; i<myNeighbors.size(); i++)
    {
      myNeighbors.get(i).receivePacket(txPkt.get(txPkt.size()-1));
    }
  }

  public void setPacket(ArrayList<Packet> txPkt)
  {
    txPkt=txPkt;
  }

  public void run()
  {
  }
}
