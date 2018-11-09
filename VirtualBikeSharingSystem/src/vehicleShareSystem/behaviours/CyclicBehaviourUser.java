package vehicleShareSystem.behaviours;

import vehicleShareSystem.Utils;
import vehicleShareSystem.agents.AgentUser;
import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.model.Capsule;

import java.util.Scanner;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

//-main false -host 127.0.0.1 AgentUser:vehicleShareSystem.agents.AgentUser
public class CyclicBehaviourUser extends CyclicBehaviour 
{
	AgentUser user;
	Scanner scanner;
	
	String mensaje;
	Capsule msgObject;
	String comment;
	
	Vehicle vehicleTemp; 
	
	public CyclicBehaviourUser(AgentUser agent)
	{
		super();
		
		this.user = agent;
		scanner=new Scanner(System.in);
		//this.mensaje=scanner.nextLine();

	}
	
	
	@Override
	public void action() 
	{
		if(this.user.arrivedToFinalStation())
		{
			this.user.stablishDesiredVehicle();
			this.user.stablishDesiredStation();
		}
		
		//Temporal
		System.out.println("\nI'm " + this.user.getLocalName() + ": I'm on " + this.user.getCurrentStation() + " and I want to go to " + this.user.desiredStation + " by " + this.user.desiredVehicle +"\n");
		//this.mensaje=scanner.nextLine();
		//temporal
		
		//realiza peticion
		System.out.println("I realiced a petition to " + this.user.getCurrentStation() + ": I'm on " + this.user.getCurrentStation() + " and I want to go to " + this.user.desiredStation + " by " + this.user.desiredVehicle);
		this.msgObject = new Capsule(null,this.user.getDesiredVehicle(),this.user.getDesiredStation(),null);
		Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "pedirVehiculo");
		
		// Receive the message
		ACLMessage msg=this.myAgent.blockingReceive(
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
						MessageTemplate.MatchOntology("ontologia"))
				);
		
		try
		{
			this.comment = msg.getEnvelope().getComments();
			this.msgObject = (Capsule) msg.getContentObject();
			System.out.println("\t" + this.user.getCurrentStation() + " told me " + this.comment + ". ");
			
			switch(this.comment)
			{
			case "entregaVehiculo":
				//obtiene el vehiculo
				if(msgObject.getVehicle() != null) {
					this.user.takeVehicle(msgObject.getVehicle());
				}
				
				//dirigirseAParada
				this.notifyMonitor("moving");
				this.user.goToStation();
				this.notifyMonitor("waiting");

				//devuelve el vehiculo
				if(this.user.hasVehicle())
				{
					this.vehicleTemp = this.user.leaveVehicle();
					msgObject = new Capsule(this.vehicleTemp,null, null, null);
					Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "entregaVehiculo");
				}

				//espera
				this.user.waitSomeTime(8000);
				break;
				
			case "pedidoNoSatisfacotrio":
				if(msgObject.getStation() != null) {	//Si hay una parada alternativa con reserva, la marcamos
					System.out.println("\t"+this.user.getCurrentStation() + " told me that there's an alternative route -> " + msgObject.getStation() + ". ");
					this.user.stablishDestinationStation(msgObject.getStation());
				}
				if(msgObject.getVehicle() != null) { //si hay vehiculo alternativo
					System.out.println("\t"+this.user.getCurrentStation() + " gived to me an alternative vehicle -> " + msgObject.getVehicle().getType() + ". ");
					this.user.takeVehicle(msgObject.getVehicle());
				} else {
					System.out.println("\tI have to go walking. ");
				}
				
				//dirigirseAParada
				this.notifyMonitor("moving");
				this.user.goToStation();
				this.notifyMonitor("waiting");

				//Si tiene vehiculo, lo deja
				if(this.user.hasVehicle()) 
				{
					//envia vehiculo
					this.vehicleTemp = this.user.leaveVehicle();
					msgObject = new Capsule(this.vehicleTemp,null, null, null);
					Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "entregaVehiculo");
				}
				
				this.user.waitSomeTime(4000);
			}
		}
		catch (UnreadableException e)
		{
			e.printStackTrace();
		}
	}
	
	public void notifyMonitor(String action)
	{
		String msg;
		switch(action)
		{
		case "moving":
			msg = "moving " + this.user.currentStation + " " + this.user.destinationStation + " " + this.user.getVehicleName();
			Utils.enviarMensaje(myAgent, "Monitor", msg, "userStatusNotification");
			break;
			
		case "waiting":
			if(this.user.arrivedToFinalStation()) 
				msg = "final" + " "+ this.user.currentStation;
			else 
				msg = "waiting" + " "+ this.user.currentStation;
			Utils.enviarMensaje(myAgent, "Monitor", msg, "userStatusNotification");
			break;
		}
	}
}