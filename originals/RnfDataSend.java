import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import EDReceive.Sender;
public class RnFDataSend
{
	Timer m_timer=null;
	Node sourceNode= null;
	Long interval = null;
	PacketTransmitter pktTrans = new PacketTransmitter();

	public RnFDataSend(long anInterval, Node aSourceNode)
	{

		this.sourceNode = aSourceNode;
		this.interval = anInterval;

//			  if(EDReceive(aSourceNode, a_interest)
//			  {
//			    m_timer = new Timer();
//			    m_timer.schedule( new EDReceiveTimer(a_node),a_interval);
//			  }
//			  else
//			  {
//			    if(m_timer!=null)
//			       m_timer.cancel();
//			  }
	}
	
	public void sendRnFData(Packet pkt)
	{
		Iterator<Node> iterator = sourceNode.getNeighbours().iterator();
		ArrayList<Node> myRnFDataNeighborList = new ArrayList<Node>();
		while (iterator.hasNext()) {
			Node aNeighborNode = iterator.next();
			if (aNeighborNode.getIsRnFDataNode()) { //Assuming that this was set in blue path
				myRnFDataNeighborList.add(aNeighborNode);
				pktTrans.setNeighbors(myRnFDataNeighborList);//Assuming that we need to use the same PacketTramsmitter to trasmit data
				pktTrans.addPacket(pkt);
				break;
			}
		}
	}

	public void RnFSendFlag()
	{
	}
}

