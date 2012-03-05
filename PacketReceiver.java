import java.util.ArrayList;
import java.sql.Date;

public class PacketReceiver
{
  ArrayList<Packet> myRecPac;

  public  PacketReceiver()
  {
    myRecPac = new ArrayList<Packet>();
  }

  public void populatereceiver(Nodes ob)
  {
    for(Packet a:ob.getpacinnode())
    {
      myRecPac.add(a);
    }
  }

  public ArrayList<Packet> getReceiver_transaction()
  {
    return myRecPac;
  }

  public void remove_trans()
  {
    myRecPac.clear();
  }

  public boolean check_empty()
  {
    return myRecPac.isEmpty();
  }

  public Packet getPacket()
  {
    return myRecPac.remove(myRecPac.size()-1);
  }

  public void receivePacket(Packet pkt)
  {
    myRecPac.add(pkt);
  }

  public void run()
  {
  }
}

class Nodes
{
    ArrayList<Packet> pac_in_node;
    public Nodes()
    {
      pac_in_node = new ArrayList <Packet>();
    }

    public ArrayList<Packet> getpacinnode()
    {
      return pac_in_node;
    }
}

class ReceivedPacket
{
  ArrayList<Packet> Rec_pac;
  public ReceivedPacket()
  {
    Rec_pac=new ArrayList<Packet>();

  }

  public void populatestorage(PacketReceiver ob)
  {

    if (ob.check_empty())
    {
      ob.remove_trans();
    }
    else
    {
      for(Packet a:ob.getReceiver_transaction())
      {

        Rec_pac.add(a);
      }

      ob.remove_trans();

    }
  }


  public ArrayList<Packet> getStorage_transaction()
  {
    return Rec_pac;
  }
}
