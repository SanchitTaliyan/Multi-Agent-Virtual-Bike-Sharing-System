package vehicleShareSystem.behaviours;

import vehicleShareSystem.Utils;
import vehicleShareSystem.agents.AgentUser;
import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.model.Capsule;

import java.util.Scanner;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

//Run configuration required:
//- monitor instance required.
//-main false -host 127.0.0.1 AgentUser:vehicleShareSystem.agents.AgentUser
@SuppressWarnings("serial")
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
		//this.mensaje=scanner.nextLine(); //debug

	}
	
	
	@Override
	public void action() 
	{
		//If the user has arrived to the final station, he asigns randomly another desired vehicle and station
		if(this.user.arrivedToFinalStation())
		{
			this.user.stablishDesiredVehicle();
			this.user.stablishDesiredStation();
		}
		
		//printing info
		System.out.println("\nI'm " + this.user.getLocalName() + ": I'm on " + this.user.getCurrentStation() + " and I want to go to " + this.user.desiredStation + " by " + this.user.desiredVehicle +".\n");
		//this.mensaje=scanner.nextLine(); //debug
		
		//PetitionRequest
		System.out.println("I make a vehicle request to " + this.user.getCurrentStation() + ": I'm on " + this.user.getCurrentStation() + " and I want to go to " + this.user.desiredStation + " by " + this.user.desiredVehicle + ".");
		this.msgObject = new Capsule(null,this.user.getDesiredVehicle(),this.user.getDesiredStation(),null);
		Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "VEHICLE_REQUEST");
		
		//Waiting for answer
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
			case "VEHICLE_DELIVERY":
				//1. The station give to the user the vehicle normaly
				if(msgObject.getVehicle() != null) {
					this.user.takeVehicle(msgObject.getVehicle());
				}
				
				//2. The user goes to the final station
				this.notifyMonitor("moving");
				this.user.goToStation();
				this.notifyMonitor("waiting");

				//3. Leaves the vehicle on the final station
				if(this.user.hasVehicle())
				{
					this.vehicleTemp = this.user.leaveVehicle();
					msgObject = new Capsule(this.vehicleTemp,null, null, null);
					Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "VEHICLE_RETURN");
				}

				//4. The user waits a bit
				this.user.waitSomeTime(8000);
				break;
				
			case "UNSATISFACTORY_REQUEST":
				// The station reports that the request couldn't be completed properly
				//1. The capsule can cointain: alternativeStation and alternativeVehicle
				if(msgObject.getStation() != null) {	//checks if there's an alternative reserved station
					System.out.println("\t"+this.user.getCurrentStation() + " told me that there's an alternative route -> " + msgObject.getStation() + ". ");
					this.user.stablishDestinationStation(msgObject.getStation());
				}
				if(msgObject.getVehicle() != null) { //checks if the station gave an alternative vehicle
					System.out.println("\t"+this.user.getCurrentStation() + " gived to me an alternative vehicle -> " + msgObject.getVehicle().getType() + ". ");
					this.user.takeVehicle(msgObject.getVehicle());
				} else {
					System.out.println("\tI have to go walking. ");
				}
				
				//2. Moving to the destination station
				this.notifyMonitor("moving");
				this.user.goToStation();
				this.notifyMonitor("waiting");

				//3. If has a vehicle, the user leaves it
				if(this.user.hasVehicle()) 
				{
					//send the vehicle to the station
					this.vehicleTemp = this.user.leaveVehicle();
					msgObject = new Capsule(this.vehicleTemp,null, null, null);
					Utils.enviarMensaje(myAgent, this.user.getCurrentStation(), this.msgObject, "VEHICLE_RETURN");
				}
				
				//4. the user waits before restarting the behavior
				this.user.waitSomeTime(4000);
			}
		}
		catch (UnreadableException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * This method sends splitable String message to the monitor with user information
	 * msg = [moving] currentStation destinationStation vehicleType
	 * msg = [waiting|final] currentStation 
	 */
	public void notifyMonitor(String action)
	{
		String msg;
		switch(action)
		{
		case "moving":
			msg = "moving " + this.user.currentStation + " " + this.user.destinationStation + " " + this.user.getVehicleName();
			Utils.enviarMensaje(myAgent, "Monitor", msg, "USERSTATUS_NOTIFICATION");
			break;
			
		case "waiting":
			if(this.user.arrivedToFinalStationVerboseOff()) 
				msg = "final" + " "+ this.user.currentStation;
			else 
				msg = "waiting" + " "+ this.user.currentStation;
			Utils.enviarMensaje(myAgent, "Monitor", msg, "USERSTATUS_NOTIFICATION");
			break;
		}
	}
}