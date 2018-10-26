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
				this.user.takeVehicle(msgObject.getVehicle());
				
				//dirigirseAParada
				this.user.goToStation();
				
				//devuelve el vehiculo
				this.vehicleTemp = this.user.leaveVehicle();
				msgObject = new Capsule(this.vehicleTemp,null, null, null);
				Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "entregaVehiculo");

				//espera
				this.user.waitSomeTime(2000);
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
				this.user.goToStation();

				//Si tiene vehiculo, lo deja
				if(this.user.hasVehicle()) 
				{
					//envia vehiculo
					this.vehicleTemp = this.user.leaveVehicle();
					msgObject = new Capsule(this.vehicleTemp,null, null, null);
					Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "entregaVehiculo");
				}
				
				this.user.waitSomeTime(2000);
			}
		}
		catch (UnreadableException e)
		{
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}