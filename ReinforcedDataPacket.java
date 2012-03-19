public class ReinforcedDataPacket extends Packet
{
  public ReinforcedDataPacket(Node sender, long id, boolean ifsent, DataType dType, Data datum, int requestedAmount, int requestedPeriod)
  {
    super(sender, id, ifsent, dType, datum, requestedAmount, requestedPeriod);
  }

  public ReinforcedDataPacket clone()
  {
    // Cloning always happens when a Packet is sent to another Node. ifsent is always set to false when cloned.
    return new ReinforcedDataPacket(sender, id, false, dType, datum, requestedAmount, requestedPeriod);
  }
}
