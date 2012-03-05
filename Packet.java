public class Packet implements Cloneable
{
  public Node node;       // The Node which sent this packet
  public PacketType type; // which of the four from the PacketType enum.
  public long id;         // id of the initial request; each has a unique id.
  public boolean ifsent;  // so nodes can easily keep track of if they're sent this packet or not

  public Packet(Node node, PacketType type, long id, boolean ifsent)
  {
    this.node = node;
    this.type = type;
    this.id = id;
    this.ifsent = false;
  }
}
