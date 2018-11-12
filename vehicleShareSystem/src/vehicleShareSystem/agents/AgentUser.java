package vehicleShareSystem.agents;

import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.behaviours.CyclicBehaviourUser;

import java.util.ArrayList;
import java.util.Random;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

//Run configuration required:
//- monitor instance required.
// -main false -host 127.0.0.1 AgentUser:vehicleShareSystem.agents.AgentUser
@SuppressWarnings("serial")
public class AgentUser extends Agent {

	public String agentName; // "same as getLocalName"
	
	public Vehicle vehicle; //user's vehicle
	public String desiredVehicle; //user's desired vehicle
	
	//station variables
	public String currentStation; 
	public String destinationStation;
	public String desiredStation;
	
	public ArrayList<String> stations;//list of avaible stations
	public ArrayList<String> vehicles; //list of avaible vehicles
	
	protected CyclicBehaviourUser cyclicBehaviourUser;
	
	
	public void setup() 
	{
		System.out.println("SETUP!!\n\n");
		
		stations = new ArrayList<String>();
		stations.add("StationA");
		stations.add("StationB");
		stations.add("StationC");
		stations.add("StationD");
		
		vehicles = new ArrayList<String>();
		vehicles.add("bike");
		vehicles.add("scooter");
		
		this.vehicle = null;
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("user");
		sd.setType("user");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);

		/*
		 * Stablish configuration to a specific user:
			if(getLocalName().equals("USERNAME"))
			{
				1. Add name of the user as a service
				
				2. Stablish current, desired and destination station
	
				3. Stablish desired vehicle
			}
		*/
		
		if(getLocalName().equals("User1"))
		{
			sd = new ServiceDescription();
			sd.setName("User1");
			sd.setType("User1");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			this.stablishCurrentStation("StationA");
			this.stablishDesiredStation("StationB");
			this.stablishDestinationStation("StationB");
			this.stablishDesiredVehicle("bike");
		}
		
		if(getLocalName().equals("User2"))
		{
			sd = new ServiceDescription();
			sd.setName("User2");
			sd.setType("User2");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			this.stablishCurrentStation("StationB");
			this.stablishDesiredStation("StationA");
			this.stablishDestinationStation("StationA");
			this.stablishDesiredVehicle("scooter");
		}
		
		if(getLocalName().equals("User3"))
		{
			sd = new ServiceDescription();
			sd.setName("User3");
			sd.setType("User3");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			this.stablishCurrentStation("StationD");
			this.stablishDesiredStation("StationB");
			this.stablishDestinationStation("StationB");
			this.stablishDesiredVehicle("scooter");
		}
		
		if(getLocalName().equals("User4"))
		{
			sd = new ServiceDescription();
			sd.setName("User4");
			sd.setType("User4");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			this.stablishCurrentStation("StationB");
			this.stablishDesiredStation("StationC");
			this.stablishDestinationStation("StationC");
			this.stablishDesiredVehicle("bike");
		}
		
		if(getLocalName().equals("User5"))
		{
			sd = new ServiceDescription();
			sd.setName("User5");
			sd.setType("User5");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			this.stablishCurrentStation("StationA");
			this.stablishDesiredStation("StationC");
			this.stablishDestinationStation("StationC");
			this.stablishDesiredVehicle("bike");
		}
		
		
		try
		{
			DFService.register(this,dfd);
		}
		catch(FIPAException e){
			System.err.println("Agent "+getLocalName() +": " + e.getMessage());
		}

		cyclicBehaviourUser = new CyclicBehaviourUser(this);
		this.addBehaviour(cyclicBehaviourUser);
	}
// Setup
	
// Station methods
	/*
	 * @param: station
	 * stablish the given station as current station
	 */
	public void stablishCurrentStation(String currentStation)
	{
		this.currentStation = currentStation;
	}

	/*
	 * @param: station
	 * stablish the given station as destination station
	 */
	public void stablishDestinationStation(String destinationStation)
	{
		this.destinationStation = destinationStation;
	}

	/*
	 * stablish desired station randomly
	 */
	public void stablishDesiredStation()
	{
		int actualNStation = 0;
		for (String e : this.stations) 
		{
			if(e.equals(this.currentStation))
				break;
			actualNStation++;
		}
		
		Random r = new Random(System.currentTimeMillis());
		int nStation;
		
		do {
			nStation = r.nextInt(this.stations.size());
		} while (nStation == actualNStation);
		
		this.desiredStation = this.stations.get(nStation);
		this.destinationStation = this.desiredStation;
	}
	
	/*
	 * @param: station
	 * stablish the given station as desired station
	 */
	public void stablishDesiredStation(String desiredStation)
	{
		this.desiredStation = desiredStation;
	}
	
	/*
	 * Returns user desired station
	 */
	public String getDesiredStation()
	{
		return this.desiredStation;
	}
	
	/*
	 * Returns user's current station
	 */
	public String getCurrentStation()
	{
		return this.currentStation;
	}

	/*
	 * Returns user's destination station
	 */
	public String getDestinationStation()
	{
		return this.destinationStation;
	}
	
	/*
	 * Returns true and prints user's info if the user arrived to final station 
	 * otherwise returns false
	 */
	public boolean arrivedToFinalStation()
	{
		if(this.desiredStation.equals(this.currentStation)){
			System.out.println("!#!\tI arrived to Final station: " + this.getCurrentStation()+ ". \n");
			return true;
		}
		else
			return false;
	}
	
	/*
	 * Returns true if the user arrived to final station 
	 * otherwise returns false
	 */
	public boolean arrivedToFinalStationVerboseOff()
	{
		if(this.desiredStation.equals(this.currentStation))
			return true;
		else
			return false;
	}
// Station methods
	
// Vehicle methods
	/*
	 * @param: vehicle type e.g. "bike"
	 * stablish the given vehicle type as desired vehicle 
	 */
	public void stablishDesiredVehicle(String vehiculoDeseado)
	{
		this.desiredVehicle = vehiculoDeseado;
	}

	/*
	 * stablish the desired vehicle randomly
	 */
	public void stablishDesiredVehicle()
	{
		int actualNVehicle = 0;
		if(this.vehicle == null) {
			actualNVehicle = this.vehicles.size() + 1; 
		} else {
			for (String v : this.vehicles) 
			{
				if( v.equals(this.vehicle.getType()))
					break;
				actualNVehicle++;
			}
		}

		Random r = new Random(System.currentTimeMillis());
		int nVehicle;
		
		do {
			nVehicle = r.nextInt(this.vehicles.size());
		} while (nVehicle == actualNVehicle);

		this.desiredVehicle = this.vehicles.get(nVehicle);
	}

	/*
	 * returns user's desired vehicle
	 */
	public String getDesiredVehicle()
	{
		return this.desiredVehicle;
	}

	/*
	 * returns true if the user has a vehicle right now
	 * otherwise returns false
	 */
	public boolean hasVehicle()
	{
		if(this.vehicle == null)
			return false;
		else
			return true;
	}

	/*
	 * @param: vehicle 
	 * stablish the given vehicle as user vehicle 
	 */
	public void takeVehicle(Vehicle vehicle)
	{
		this.vehicle = vehicle;
		this.vehicle.stablishReserve("none");
		System.out.println("\tI received " + this.vehicle.getType() + ". ");
	}

	/*
	 * returns current vehicle type
	 */
	public String getVehicleName()
	{
		if(this.vehicle != null)
			return this.vehicle.getType();
		else
			return "walking";
	}
	
	/*
	 * returns current vehicle, unassigning it
	 */
	public Vehicle leaveVehicle()
	{
		System.out.println("\tI'm at "+ this.getCurrentStation()  +" and I leave the "+ this.vehicle.getType()+ ". ");
		Vehicle aux = this.vehicle;
		this.vehicle = null;
		return aux;
	}
// Vehicle methods
	
// Actions
	/*
	 * @param: Int miliseconds 
	 * The user wait some time 
	 */
	public void waitSomeTime(int miliSeconds)
	{
		try{
		    Thread.sleep(miliSeconds);
		}
		catch(InterruptedException ex){
		    Thread.currentThread().interrupt();
		}
	}

	/*
	 * The user moves from current station to destination station
	 */
	public void goToStation()
	{
		if(this.hasVehicle())
			System.out.println("\t"+this.getCurrentStation() + " -------"+this.getVehicleName()+"--------> " + this.getDestinationStation()  + ". ");
		else
			System.out.println("\t"+this.getCurrentStation() + " -------walking--------> " + this.getDestinationStation()  + ". ");

		this.waitSomeTime(12000);
		this.currentStation = this.destinationStation;
		this.destinationStation = this.desiredStation;
	}
// Actions

}