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

  private Sender senderExist( long SenderID )
  {
    Sender sender = null;
    Iterator<Sender> it = senders.iterator();

    while( it.hasNext() && (null == sender ) )
    {
      Sender s = it.next();

      if( s.getID() == SenderID )
        sender = s;
    }

    return sender;
  }

  public long getSeqNum( long SenderID )
  {
    long SequenceNumber = Sender.INVALID;
    Sender s = senderExist( SenderID );

    if( null != s )
      SequenceNumber = s.getSequenceNumber();

    return SequenceNumber;
  }

  public boolean updateSeqNum( long SenderID, long SequenceNumber )
  {
    Sender s = senderExist( SenderID );

    if( null != s )
    {
      s.setSequenceNum( SequenceNumber );
      return true;
    }

    return false;
  }

  public boolean addSender( long SenderID )
  {
    Sender s = new Sender( SenderID );
    senders.add( s );
    return true;
  }

  public class Sender
  {
    private long SenderID;
    private long SequenceNumber;
    private Date timestamp;

    public static final long INVALID = -1;

    public Sender( long SenderID, long SequenceNumber, Date timestamp )
    {
      this.SenderID = SenderID;
      this.SequenceNumber = SequenceNumber;
      this.timestamp = timestamp;
    }

    public Sender( long SenderID )
    {
//      this( SenderID, Sender.INVALID, Calendar.getInstance().getTime() ); // TODO: fix this and then uncomment it. 
//commented because of error:
//
//EDReceive.java:80: cannot find symbol
//symbol  : variable Calendar
//location: class EDReceive.Sender
//      this( SenderID, Sender.INVALID, Calendar.getInstance().getTime() );
    }

    public long getID()
    {
      return SenderID;
    }

    public long getSequenceNumber()
    {
      return SequenceNumber;
    }

    public void setSequenceNum( long SequenceNumber )
    {
      this.SequenceNumber = SequenceNumber;
    }
  }
}
