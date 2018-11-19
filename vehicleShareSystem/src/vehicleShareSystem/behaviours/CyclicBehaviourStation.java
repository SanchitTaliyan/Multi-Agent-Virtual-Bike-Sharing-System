package vehicleShareSystem.behaviours;

import vehicleShareSystem.Utils;
import vehicleShareSystem.agents.AgentStation;
import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.model.Capsule;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

//Run configuration required:
//- monitor instance required.
//-main false -host 127.0.0.1 StationA:vehicleShareSystem.agents.AgentStation
@SuppressWarnings("serial")
public class CyclicBehaviourStation extends CyclicBehaviour 
{
	AgentStation station;
	
	ACLMessage msg;
	Capsule msgObject;
	Vehicle vTemp;
	String comment;
	
	public CyclicBehaviourStation(AgentStation station)
	{
		super();
		
		this.station = station;
	}
	
	
	@Override
	public void action() 
	{
		System.out.println("\n" + this.station.getLocalName() + ": " + this.station.listOfVehicles());
		this.notifyMonitor();

		// Receiving messages from users or other stations
		msg=this.myAgent.blockingReceive(
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
						MessageTemplate.MatchOntology("ontologia"))
				);
				
		try
		{
			this.comment = msg.getEnvelope().getComments();
			String sender =(String) msg.getSender().getLocalName();
			
			this.msgObject = (Capsule) msg.getContentObject();
			System.out.println("[ " + sender + "] " + this.comment + ": ");
			
			switch(this.comment)
			{
			case "VEHICLE_REQUEST":	//User request a vehicle: //object -> Capsule : string with vehicle type request and string desiredStation				
				Boolean foundAlternativeStation;

				String userRequest = (String) msg.getSender().getLocalName();
				String vehicleRequestType = msgObject.getVehicleType();
				String desiredStation = msgObject.getStation();
				System.out.println("\tVehicle request: " + userRequest + " wants to go to " + desiredStation + " by " + vehicleRequestType);

				Vehicle vehicle;
				String alternativeStation=null;
				
			//1. Checking if the user has a reserved vehicle
				vehicle = this.station.getVehicleReserved(userRequest);
				if(vehicle != null)
				{
					
					//if found, send the vehicle
					msgObject = new Capsule(vehicle,null,null,null);
					System.out.println("\tGiving reserved "+vehicle.getType() +" to "+userRequest);
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "VEHICLE_DELIVERY");
					break;
				}
				
			//2. Checking if there's a free desired type vehicle 
				vehicle = this.station.getVehicle(vehicleRequestType);
				if(vehicle != null)
				{
					//if found, send the vehicle
					msgObject = new Capsule(vehicle,null,null,null);
					System.out.println("\tGiving "+vehicle.getType() +" to "+userRequest);
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "VEHICLE_DELIVERY");
					break;
				}
				System.out.println("\tRequested vehicle not avaible: " + vehicleRequestType);

			//3.Checking if there is an alternative vehicle, otherwise vehicle is going to be null
				vehicle = this.station.getAlternativeVehicle();
				if (vehicle != null) System.out.println("\tThere is an alternative: "+vehicle.getType());
				
			//4. checking if there is an alternative station close to the current one
			//   for this we look if there is a stop at a radius of 1/FAR 
			//	 with respect to the desired destination stop. 
				int FAR = 3;
				alternativeStation = null; // Maybe it will contain the alternative station 
				foundAlternativeStation = false;
				this.station.stationNames = this.station.stations.keys(); //we use an enumeration to go through station names
				while(this.station.stationNames.hasMoreElements()) {
					alternativeStation = (String) this.station.stationNames.nextElement();
					
					if(alternativeStation.equals(desiredStation))
						continue;
					else
					{
						//If the station is close (distance to desired station)/FAR < alternativeStation
						if(this.station.stations.get(desiredStation)/FAR > this.station.stations.get(alternativeStation) )
						{
							System.out.println("\tAsking " +alternativeStation+ " (distance " + this.station.stations.get(alternativeStation) + ") for a vehicle reservation:");
							//The station sends a vehicle reservation Request (desired vehicle type, current station, user who request)
							msgObject = new Capsule(null, vehicleRequestType, this.station.getLocalName() , userRequest);
							Utils.enviarMensaje(myAgent, alternativeStation, this.msgObject, "VEHICLE_RESERVATION_REQUEST");
							//Waiting for response
							msg=this.myAgent.blockingReceive(
									MessageTemplate.and(
											MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
											MessageTemplate.MatchSender( new AID(alternativeStation, AID.ISLOCALNAME) ))
									);
							
							this.comment = msg.getEnvelope().getComments();
							System.out.println("\t\tresult : " + msg.getEnvelope().getComments() );
							if(this.comment.equals("VEHICLE_RESERVED")) {
								System.out.println("!\t" + alternativeStation + " reserved requested vehicle.");
								foundAlternativeStation = true;
								break;
							}
							else //if(this.comment.equals("VEHICLE_NON_RESERVED"))
								continue;
						}
						else
							//if the station is far the station doesnt do nothing
							continue;
					}
				}
				if(!foundAlternativeStation) {
					alternativeStation = null;
					System.out.println("\tThere is not alternative station.");
				}

			//5. The station send a capsule message with the info obtained in 3. and 4.
			//   	maybe it contains alternative station and alternative vehicle
				msgObject = new Capsule(vehicle,null,alternativeStation,null);
				if(alternativeStation == null) { //if there's no alternative route
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "UNSATISFACTORY_REQUEST"); 
				}
				else { //if there's a reserved alternative station
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "UNSATISFACTORY_REQUEST");
					System.out.println("\tReporting to "+ userRequest + " that has an alternative route: " +alternativeStation);
				}
				
				//we prompt the remaining info on station's terminal
				if (vehicle != null)
					System.out.println("\tGiving alternative vehicle: "+vehicle.getType() +" to "+userRequest);
				else 
					System.out.println("\tReporting to "+userRequest+" that has to go walking. ");
				break;
				
			case "VEHICLE_RETURN":
				//A user leaves his vehicle: Object -> vehicle
				System.out.println("\t"+sender+" leaves vehicle: "+msgObject.getVehicle().getType()+".\n");
				this.station.receiveVehicle(msgObject.getVehicle());
				break;
				
			case "VEHICLE_RESERVATION_REQUEST": 
				//Another station request for a vehicle reservation: 
				//	Object -> String with user requesting - String with vehicle desired type
				System.out.println("\tResolving vehicle reservation: "+msgObject.getVehicleType()+" for "+msgObject.getUser());
				boolean isReserved = this.station.reserveVehicle(msgObject.getUser(), msgObject.getVehicleType());
				if(isReserved) {
					System.out.println("\tVehicle reserved.\n");
					Utils.enviarMensaje(myAgent, msgObject.getStation(), this.msgObject, "VEHICLE_RESERVED");
				}
				else {
					Utils.enviarMensaje(myAgent, msgObject.getStation(), this.msgObject, "VEHICLE_NOT_RESERVED");
					System.out.println("\tThere's no reservable vehicle.\n");
				}
				break;
			}
		}
		catch (UnreadableException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * This method sends a message to the monitor with station information
	 */
	public void notifyMonitor()
	{
		Utils.enviarMensaje(myAgent, "Monitor", this.station.listOfVehicles(), "STATIONSTATUS_NOTIFICATION");

	}
}
