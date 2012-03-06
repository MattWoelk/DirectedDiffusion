public class Packet implements Cloneable
{
  public PacketType pType; // which of the four from the PacketType enum.
  public Node node;       // The Node which sent this packet.
  public DataType dType;  // The type of data which is being sent in the packet.
  public long id;         // id of the initial request; each has a unique id. This is simply a time-stamp.
  public boolean ifsent;  // so nodes can easily keep track of if they're sent this packet or not

  public Packet(Node node, PacketType pType, long id, boolean ifsent)
  {
    this.node = node;
    this.pType = pType;
    this.id = id;
    this.ifsent = false;
  }
}
