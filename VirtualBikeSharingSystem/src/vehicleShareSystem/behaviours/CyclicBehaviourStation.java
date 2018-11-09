package vehicleShareSystem.behaviours;

import vehicleShareSystem.Utils;
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

		// Receive the message
		msg=this.myAgent.blockingReceive(
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
						MessageTemplate.MatchOntology("ontologia"))/*MessageTemplate.MatchSender( new AID("User1", AID.ISLOCALNAME) ))*/
				);
				
		try
		{
			this.comment = msg.getEnvelope().getComments();
			String sender =(String) msg.getSender().getLocalName();
			
			this.msgObject = (Capsule) msg.getContentObject();
			System.out.println("[ " + sender + "] " + this.comment + ": ");
			
			switch(this.comment)
			{
			case "pedirVehiculo":	//Un usuario pide: //object -> Capsule - string con tipo vehiculoPedido y string paradaDeseada				
				Boolean foundAlternativeStation;

				String userRequest = (String) msg.getSender().getLocalName();
				String vehicleRequestType = msgObject.getVehicleType();
				String desiredStation = msgObject.getStation();
				System.out.println("\tPeticion de vehiculo: " + userRequest + " wants to go to " + desiredStation + " by " + vehicleRequestType);

				Vehicle vehicle;
				String alternativeStation=null;
				
			//1. miramos a ver si hay algun vehiculo reservado para userRequest
				vehicle = this.station.getVehicleReserved(userRequest);
				if(vehicle != null)
				{
					
					//Enviamos el vehiculo
					msgObject = new Capsule(vehicle,null,null,null);
					System.out.println("\tGiving reserved "+vehicle.getType() +" to "+userRequest);
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "entregaVehiculo");
					break;
				}
					
				
			//2. Miramos a ver si hay un vehiculo libre que coincida con peticion
				vehicle = this.station.getVehicle(vehicleRequestType);
				if(vehicle != null)
				{
					//Enviamos el vehiculo
					msgObject = new Capsule(vehicle,null,null,null);
					System.out.println("\tGiving "+vehicle.getType() +" to "+userRequest);
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "entregaVehiculo");
					break;
				}
				System.out.println("\tRequested vehicle not avaible: " + vehicleRequestType);

			//3. Miramos si hay un vehiculo alternativo para que se pueda desplazar
				vehicle = this.station.getAlternativeVehicle();
				if (vehicle != null) System.out.println("\tThere is an alternative: "+vehicle.getType());
				
			//4. Miramos si hay una parada cercana respecto al destino a la que realizar petición
				alternativeStation = null;
				foundAlternativeStation = false;
				this.station.stationNames = this.station.stations.keys();
				while(this.station.stationNames.hasMoreElements()) {
					alternativeStation = (String) this.station.stationNames.nextElement();
					
					if(alternativeStation.equals(desiredStation))
						continue;
					else
					{
						//si esta cerca (distancia parada deseada) < distancia parada /3
						if(this.station.stations.get(desiredStation)/3 > this.station.stations.get(alternativeStation) )
						{
							System.out.println("\tAsking " +alternativeStation+ " (distance " + this.station.stations.get(alternativeStation) + ") for a vehicle reservation:");
							//Enviamos un mensaje de reserva (vehiculo deseado, parada actual, usuario que reserva)
							msgObject = new Capsule(null, vehicleRequestType, this.station.getLocalName() , userRequest);
							Utils.enviarMensaje(myAgent, alternativeStation, this.msgObject, "peticionReservaVehiculo");
							//Esperamos respuesta
							msg=this.myAgent.blockingReceive(
									MessageTemplate.and(
											MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
											MessageTemplate.MatchSender( new AID(alternativeStation, AID.ISLOCALNAME) ))
									);
							
							this.comment = msg.getEnvelope().getComments();
							System.out.println("\t\tresult : " + msg.getEnvelope().getComments() );
							if(this.comment.equals("vehiculoReservado")) {
								System.out.println("!\t" + alternativeStation + " reserved requested vehicle.");
								foundAlternativeStation = true;
								break;
							}
							else //if(this.comment.equals("vehiculoNoReservado"))
								continue;
						}
						else
							//si está lejos, no preguntamos
							continue;
					}
				}
				if(!foundAlternativeStation) {
					alternativeStation = null;
					System.out.println("\tThere is not alternative station.");
				}

			//5. enviamos un mensaje encapsulado con estacion / vehiculo
				msgObject = new Capsule(vehicle,null,alternativeStation,null);
				if(alternativeStation == null) {
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "entregaVehiculo");				//si vehiculo es null, no hay alternativo
				}
				else { //si estacion es null, no se ha realizado reserva
					Utils.enviarMensaje(myAgent, userRequest, this.msgObject, "pedidoNoSatisfacotrio");			//si vehiculo es null, no hay alternativo
					System.out.println("\tReporting to "+ userRequest + " that has an alternative route: " +alternativeStation);
				}
				
				if (vehicle != null)
					System.out.println("\tGiving alternative vehicle: "+vehicle.getType() +" to "+userRequest);
				else 
					System.out.println("\tReporting to "+userRequest+" that has to go walking. ");
				break;
				
			case "entregaVehiculo":	//Un usuario deja su vehiculo: Object -> vehiculo
				System.out.println("\t"+sender+" leaves vehicle: "+msgObject.getVehicle().getType()+".\n");
				this.station.receiveVehicle(msgObject.getVehicle());
				break;
				
			case "peticionReservaVehiculo": //Otra estacion realiza peticion reserva: Object -> String usuario que pide
				System.out.println("\tResolving vehicle reservation: "+msgObject.getVehicleType()+" for "+msgObject.getUser());
				boolean isReserved = this.station.reserveVehicle(msgObject.getUser(), msgObject.getVehicleType());
				//TODO
				if(isReserved) {
					System.out.println("\tVehicle reserved.\n");
					Utils.enviarMensaje(myAgent, msgObject.getStation(), this.msgObject, "vehiculoReservado");
				}
				else {
					Utils.enviarMensaje(myAgent, msgObject.getStation(), this.msgObject, "vehiculoNoReservado");
					System.out.println("\tThere's no reservable vehicle.\n");
				}
				break;
			}
		}
		catch (UnreadableException e)
		{
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notifyMonitor()
	{
		Utils.enviarMensaje(myAgent, "Monitor", this.station.listOfVehicles(), "stationStatusNotification");

	}
}