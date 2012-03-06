public class Data
{
  public int datum;
  public DataType dType; //Does this really need to be in here? It's already in Packet.

  public Data(int datum, DataType dType)
  {
    this.datum = datum;
    this.dType = dType;
  }
}
