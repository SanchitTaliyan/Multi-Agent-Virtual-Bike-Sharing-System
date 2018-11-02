package vehicleShareSystem.behaviours;

import vehicleShareSystem.Utils;
import vehicleShareSystem.agents.AgentMonitor;
import vehicleShareSystem.agents.AgentStation;
import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.model.Capsule;

import FIPA.Envelope;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

//-gui AgentStation:vehicleShareSystem.agents.AgentStation
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

		// Receive the message
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
			case "stationStatusNotification":	//content = list of station vehicles	
				this.monitor.refreshStation(sender, content);
				break;
				
			case "userStatusNotification":	//content = list of station vehicles	
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