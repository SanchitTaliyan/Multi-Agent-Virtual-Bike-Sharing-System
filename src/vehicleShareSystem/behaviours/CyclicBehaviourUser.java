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
		//System.out.println(this.user.getDesiredStation() + " " + this.user.currentStation);
		if(this.user.arrivedToFinalStation())
		{
			System.out.println("I, " + this.user.getLocalName() + " arrived to Final station. ");
			//this.user.stablishDesiredVehicle();
			//this.user.stablishDesiredStation();
			System.out.println("Now, " + this.user.getLocalName() + " now i want to go to " + this.user.getDesiredStation() + " by " + this.user.desiredVehicle);

		}
		else 
		{
			System.out.println("I'm " + this.user.getLocalName() + " Im on " + this.user.currentStation + " and I want to go to " + this.user.desiredStation + " by " + this.user.desiredVehicle);
		}
		
		this.mensaje=scanner.nextLine();
		//temporal
		
		//realiza peticion
		this.msgObject = new Capsule(null,this.user.desiredVehicle,this.user.getDesiredStation(),null);
		
		this.user.stablishDestinationStation(this.user.getDesiredStation());
		
		Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "pedirVehiculo");
		System.out.println("I " + this.user.getLocalName() + " realiced a petition to " + this.user.getCurrentStation());
		
		
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
			
			System.out.println("The station " + msg.getSender().getLocalName() + " said " + this.comment);

			switch(this.comment)
			{
			case "entregaVehiculo":
				//obtiene el vehiculo
				this.user.takeVehicle(msgObject.getVehicle());
				
				
				System.out.println("I recived " +this.user.vehicle.getType());
				
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
				if(msgObject.getStation() != null)	//Si hay una parada alternativa con reserva, la marcamos
					this.user.stablishDestinationStation(msgObject.getStation());
				if(msgObject.getVehicle() != null) //si hay vehiculo alternativo
					this.user.takeVehicle(msgObject.getVehicle());
				
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