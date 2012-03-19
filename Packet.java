public class Packet implements Cloneable
{
  public Node sender;      // The Node which sent this packet.
  public long id;          // id of the initial request; each has a unique id. This is simply a time-stamp.
  public boolean ifsent;   // so nodes can easily keep track of if they're sent this packet or not
  public DataType dType;   // The type of data which is being sent in the packet.
  public Data datum;
  public int requestedAmount;
  public int requestedPeriod;

  public Packet(Node sender, long id, boolean ifsent, DataType dType, Data datum, int requestedAmount, int requestedPeriod)
  {
    this.sender = sender;
    this.id = id;
    this.ifsent = false;
    this.dType = dType;
    this.datum = datum;
    this.requestedAmount = requestedAmount;
    this.requestedPeriod = requestedPeriod;
  }

  public Packet clone()
  {
    // Cloning always happens when a Packet is sent to another Node. ifsent is always set to false when cloned.
    return new Packet(sender, id, false, dType, datum, requestedAmount, requestedPeriod);
  }
}
