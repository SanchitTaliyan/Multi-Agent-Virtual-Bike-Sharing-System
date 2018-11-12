package vehicleShareSystem.behaviours;

import vehicleShareSystem.agents.AgentMonitor;
import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.model.Capsule;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

//Run configuration required:
//-gui Monitor:vehicleShareSystem.agents.AgentMonitor
@SuppressWarnings("serial")
public class CyclicBehaviourMonitor extends CyclicBehaviour 
{
	AgentMonitor monitor;
	
	ACLMessage msg;
	Capsule msgObject;
	
	String content;
	
	Vehicle vTemp;
	String comment;
	
	public CyclicBehaviourMonitor(AgentMonitor monitor)
	{
		super();
		
		this.monitor = monitor;
	}
	
	
	@Override
	public void action() 
	{
		this.monitor.printMonitorText();
		this.monitor.printMonitorMap();

		// Receive messages from users and stations
		msg=this.myAgent.blockingReceive(
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
						MessageTemplate.MatchOntology("ontologia"))
				);
				
		try
		{
			this.comment = msg.getEnvelope().getComments();
			String sender =(String) msg.getSender().getLocalName();
			content = (String) msg.getContentObject();

			switch(this.comment)
			{
			case "STATIONSTATUS_NOTIFICATION":	//content : list of station vehicles	
				this.monitor.refreshStation(sender, content);
				break;
				
			case "USERSTATUS_NOTIFICATION":	//content : splitable array with user information
				this.monitor.refreshUser(sender, content);
				break;
			}
		}
		catch (UnreadableException e)
		{
			e.printStackTrace();
		}
	}
}