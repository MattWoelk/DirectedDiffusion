public class Packet implements Cloneable
{
  public PacketType pType; // which of the four from the PacketType enum.
  public Node sender;       // The Node which sent this packet.
  public long id;         // id of the initial request; each has a unique id. This is simply a time-stamp.
  public boolean ifsent;  // so nodes can easily keep track of if they're sent this packet or not
  public DataType dType;  // The type of data which is being sent in the packet.
  public Data datum;

  public Packet(Node sender, PacketType pType, long id, boolean ifsent)
  {
    this.sender = sender;
    this.pType = pType;
    this.id = id;
    this.ifsent = false;
  }

  public Packet(Node sender, PacketType pType, long id, boolean ifsent, DataType dType)
  {
    this.sender = sender;
    this.pType = pType;
    this.id = id;
    this.ifsent = false;
    this.dType = dType;
  }

  public Packet(Node sender, PacketType pType, long id, boolean ifsent, DataType dType, Data datum)
  {
    this.sender = sender;
    this.pType = pType;
    this.id = id;
    this.ifsent = false;
    this.dType = dType;
    this.datum = datum;
  }

  public Packet clone()
  {
    // Cloning happens only and always when a Packet is sent to another Node.
    return new Packet(sender, pType, id, false, dType, datum);
  }
}
