import java.util.Timer;
import java.util.TimerTask;

public class ExpDataSend
{
  
	Timer m_timer=null;	
		
	public ExpDataSend(long a_interval, Node a_node, Interest a_interest)  
	{
		if(checkInterest(a_node,a_interest))//If true set the interval for sending Exploratory Data
		{
			m_timer = new Timer();
			m_timer.schedule(new ExpDataSendTimer(a_node), a_interval);//Considering interval sent is long
		}
		else//If the interest message doesn't match then stop the timer.
		{
			if(m_timer!=null)
				m_timer.cancel();
		}
		
	}
	
	class ExpDataSendTimer extends TimerTask 
	{
		
		Node m_Node;
		
		public ExpDataSendTimer(Node a_Node)
		{
			m_Node = a_Node;
		}
		
		public void run() {
			System.out.println("Sending Exploratory Data!");
			EDReceive m_Receive = new EDReceive();
			m_Receive.addSender(m_Node.getID());//Adding sender information
			
		}
	}
	  
	public boolean checkInterest(Node a_node, Interest a_interest)
	{
		//Assuming a getValue() method in Interest.java which will return the Interest value
		if(a_node.getID()==1)//replace 1 with a_interest.getValue()  
			return true;	  
		return false;
	}
	  
	public void EDSendFlag()
	{
		
	}
	//addSender
	
}
