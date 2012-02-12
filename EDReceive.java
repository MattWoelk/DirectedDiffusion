import java.util.ArrayList;
import java.util.Calendar.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EDReceive
{
  List<Sender> senders;

  public EDReceive()
  {
    senders = new ArrayList<Sender>();
  }

  private Sender senderExist( SenderID id )
  {
    Sender sender = null;
    Iterator<Sender> it = senders.iterator();

    while( it.hasNext() && (null == sender ) )
    {
      Sender s = it.next();

      if( s.getID() == id )
        sender = s;
    }

    return sender;
  }

  public SequenceNumber getSeqNum( SenderID id )
  {
    SequenceNumber num = Sender.INVALID;
    Sender s = senderExist( id );

    if( null != s )
      num = s.getSequenceNumber();

    return num;
  }

  public boolean updateSeqNum( SenderID id, SequenceNumber num )
  {
    Sender s = senderExist( id );

    if( null != s )
    {
      s.setSequenceNum( num );
      return true;
    }

    return false;
  }

  public boolean addSender( SenderID id )
  {
    Sender s = new Sender( id );
    senders.add( s );
    return true;
  }

  public class Sender
  {
    private SenderID id;
    private SequenceNumber num;
    private Date timestamp;

    public static final SequenceNumber INVALID = -1;

    public Sender( SenderID id, SequenceNumber num, Date timestamp )
    {
      this.id = id;
      this.num = num;
      this.timestamp = timestamp;
    }

    public Sender( SenderID id )
    {
      this( id, Sender.INVALID, Calander.getInstance().getTime() );
    }

    public SenderID getID()
    {
      return id;
    }

    public SequenceNumber getSequenceNumber()
    {
      return num;
    }

    public void setSequenceNum( SequenceNumber num )
    {
      this.num = num;
    }
  }
}
