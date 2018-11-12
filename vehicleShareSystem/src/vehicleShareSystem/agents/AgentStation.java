package vehicleShareSystem.agents;

import vehicleShareSystem.behaviours.CyclicBehaviourStation;
import vehicleShareSystem.model.Vehicle;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;


import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

// Run configuration required:
// - monitor instance required.
// -main false -host 127.0.0.1 StationA:vehicleShareSystem.agents.AgentStation
@SuppressWarnings("serial")
public class AgentStation extends Agent
{
	//station vehicles 
	public ArrayList<Vehicle> vehicles;
	
	//Hashtable stations <stations, distance>
	public Hashtable<String, Integer> stations;
	//auxiliar enumeration
	public Enumeration<String> stationNames;
	
	protected CyclicBehaviourStation cyclicBehaviourStation;
	
	public void setup() 
	{		
		vehicles = new ArrayList<Vehicle>();
		stations = new Hashtable<String, Integer>();
				
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setName("station");
		sd.setType("station");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);
		
		/*
		 * Stablish configuration to a specific station:
			 if(getLocalName().equals("NAME_OF_THE_STATION" )) 
			 {
				1. Add name of the station as a service
				
				2. Add the vehicles to the vehicle list
	
				3. List on the hashtable the others stations and their distance
			 }
		 */
		if(getLocalName().equals("StationA" ))
		{
			sd = new ServiceDescription();
			sd.setName("StationA");
			sd.setType("StationA");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			Vehicle temp = new Vehicle("scooter");
			vehicles.add(temp);
			//temp = new Vehicle("bike");
			//vehicles.add(temp);

			stations.put("StationB", 20);
			stations.put("StationC", 22);
			stations.put("StationD", 5); //5
			
			stationNames = stations.keys();
		}
		
		if(getLocalName().equals("StationB" ))
		{
			sd = new ServiceDescription();
			sd.setName("StationB");
			sd.setType("StationB");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			Vehicle temp = new Vehicle("bike");
			vehicles.add(temp);

			stations.put("StationA", 20);
			stations.put("StationC", 5);
			stations.put("StationD", 22);
			
			stationNames = stations.keys();
		}
		
		if(getLocalName().equals("StationC" ))
		{
			sd = new ServiceDescription();
			sd.setName("StationC");
			sd.setType("StationC");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			Vehicle temp = new Vehicle("scooter");
			vehicles.add(temp);

			stations.put("StationA", 22);
			stations.put("StationB", 5);
			stations.put("StationD", 20);
			
			stationNames = stations.keys();
		}
		
		if(getLocalName().equals("StationD" ))
		{
			sd = new ServiceDescription();
			sd.setName("StationD");
			sd.setType("StationD");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			Vehicle temp = new Vehicle("bike");
			vehicles.add(temp);
			
			stations.put("StationA", 10);
			stations.put("StationB", 22);
			stations.put("StationC", 20);
			
			stationNames = stations.keys();
		}
		try
		{
			//Finally we register the services
			DFService.register(this,dfd);
		}
		catch(FIPAException e){
			System.err.println("Agent "+getLocalName() +": " + e.getMessage());
		}
		
		cyclicBehaviourStation = new CyclicBehaviourStation(this);
		this.addBehaviour(cyclicBehaviourStation);
	}
	
// Vehicles methods
	/*
	 * @param: vehicleRequestType
	 * returns the vehicle if exists or null if doesnt
	 */
	public Vehicle getVehicle(String vehicleRequestType)
	{
		boolean found = false;
		Vehicle vehicleToRemove = null;
		
		for (Vehicle v : this.vehicles) 
		{
			if(v.isReserved() == false && v.getType().equals(vehicleRequestType))
			{
				vehicleToRemove = v;
				found = true;
				break;
			}
		}
		if(found) {
			//If we found it, we quit the vehicle from the list
			this.vehicles.remove(vehicleToRemove);
			return vehicleToRemove;
		}
		return null;
	}
	
	/*
	 * @param: userRequest
	 * returns the vehicle if its reserved by userRequest
	 */
	public Vehicle getVehicleReserved(String userRequest)
	{
		Vehicle vehicleToRemove = null;
		boolean found = false;
		for (Vehicle v : this.vehicles) 
		{
			if(v.isReserved(userRequest) == true)
			{
				vehicleToRemove = v;
				found = true;
				break;
			}
		}
		if(found) {
			//If we found it, we quit the vehicle from the list
			this.vehicles.remove(vehicleToRemove);
			return vehicleToRemove;
		}
		return null;
	}
	
	/*
	 * Returns a non reserved vehicle
	 */
	public Vehicle getAlternativeVehicle()
	{
		Vehicle vehicleToRemove = null;
		boolean found = false;
		for (Vehicle v : this.vehicles) 
		{
			if(v.isReserved() == false)
			{
				vehicleToRemove = v;
				found = true;
				break;
			}
		}
		if(found) {
			this.vehicles.remove(vehicleToRemove);
			return vehicleToRemove;
		}
		return null;
	}
	
	/*
	 * @param: Vehicle
	 * Adds an external vehicle to the list
	 */
	public void receiveVehicle(Vehicle vehicle)
	{
		vehicle.stablishReserve("none");
		this.vehicles.add(vehicle);
	}
	
	/*
	 * Returns the list of vehicles avaible on the station: e.g. [ bike, scooter]
	 */
	public String listOfVehicles()
	{
		String vehicleString = "[";
		
		for (Vehicle v : this.vehicles) 
		{
			vehicleString = vehicleString + " " + v.getType() + " ";
		}
		vehicleString = vehicleString + "]";
		
		return vehicleString;
	}
	
	/*
	 * @param: userName, vehicleType
	 * Tries to reserve a vehicle, returns true after a successful resolution
	 */
	public boolean reserveVehicle(String user, String vehicleType)
	{
		boolean isReserved = false;
		for (Vehicle v : this.vehicles) 
		{
			if(v.isReserved() == false && v.getType().equals(vehicleType))
			{
				isReserved = true;
				v.stablishReserve(user);
			}
		}	
		return isReserved;
	}
// Vehicles methods
	
}