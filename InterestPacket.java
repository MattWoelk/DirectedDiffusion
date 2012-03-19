public class InterestPacket extends Packet
{
  public int level;

  public InterestPacket(Node sender, long id, boolean ifsent, DataType dType, Data datum, int requestedAmount, int requestedPeriod, int level)
  {
    super(sender, id, ifsent, dType, datum, requestedAmount, requestedPeriod);
    this.level = level;
  }

  public InterestPacket clone()
  {
    // Cloning always happens when a Packet is sent to another Node. ifsent is always set to false when cloned.
    return new InterestPacket(sender, id, false, dType, datum, requestedAmount, requestedPeriod, level);
  }
}
