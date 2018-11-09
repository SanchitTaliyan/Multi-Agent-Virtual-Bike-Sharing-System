package vehicleShareSystem.agents;

import vehicleShareSystem.Utils;
import vehicleShareSystem.behaviours.CyclicBehaviourStation;
import vehicleShareSystem.model.Capsule;
import vehicleShareSystem.model.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;


import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentStation extends Agent
{
	public ArrayList<Vehicle> vehicles;
	
	//Hashtable estaciones <estacion, distancia>
	public Hashtable<String, Integer> stations;
	public Enumeration stationNames;
	
	protected CyclicBehaviourStation cyclicBehaviourStation;
	
	public void setup() 
	{		
		vehicles = new ArrayList<Vehicle>();
		stations = new Hashtable<String, Integer>();
				
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setName("station");
		//establezco el tipo del servicio para poder localizarlo cuando se haga una busqueda
		sd.setType("station");
		//agentes que quieren usar este servicio tienen que tener un vocabulario común
		sd.addOntologies("ontologia");
		//agentes necesitan hablar FIPA-SL lenguaje
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);
		
		
		sd = new ServiceDescription();
		sd.setName("user");
		sd.setType("user");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);
		
		if(getLocalName().equals("StationA" ))
		{
			sd = new ServiceDescription();
			sd.setName("StationA");
			sd.setType("StationA");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
			//Añadimos los vehiculos
			Vehicle temp = new Vehicle("patin");
			vehicles.add(temp);
			//temp = new Vehicle("bici");
			//vehicles.add(temp);

			//Creamos el mapa de estaciones
			stations.put("StationB", 20);
			stations.put("StationC", 22);
			stations.put("StationD", 5); //5
			
			//Podemos obtener el mapa de estaciones, pero es dinámico
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
			
			//Añadimos los vehiculos
			Vehicle temp = new Vehicle("bici");
			vehicles.add(temp);

			//Creamos el mapa de estaciones
			stations.put("StationA", 20);
			stations.put("StationC", 5);
			stations.put("StationD", 22);
			
			//Podemos obtener el mapa de estaciones, pero es dinámico
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
			
			//Añadimos los vehiculos
			Vehicle temp = new Vehicle("patin");
			vehicles.add(temp);

			//Creamos el mapa de estaciones
			stations.put("StationA", 22);
			stations.put("StationB", 5);
			stations.put("StationD", 20);
			
			//Podemos obtener el mapa de estaciones, pero es dinámico
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
			
			//Añadimos los vehiculos
			Vehicle temp = new Vehicle("bici");
			vehicles.add(temp);
			
			//Creamos el mapa de estaciones
			stations.put("StationA", 10);
			stations.put("StationB", 22);
			stations.put("StationC", 20);
			
			//Podemos obtener el mapa de estaciones, pero es dinámico
			stationNames = stations.keys();
		}
		try
		{
			//registro los servicios
			DFService.register(this,dfd);
		}
		catch(FIPAException e){
			System.err.println("Agent "+getLocalName() +": " + e.getMessage());
		}
		
		System.out.print(getLocalName());
		
		cyclicBehaviourStation = new CyclicBehaviourStation(this);
		this.addBehaviour(cyclicBehaviourStation);
	}
	
// Operaciones vehiculos
	/*
	 * @param: vehicleRequestType
	 * vehiculo si existe, null si no hay
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
			//si se ha cumplido eliminamos y terminamos
			this.vehicles.remove(vehicleToRemove);
			return vehicleToRemove;
		}
		return null;
	}
	
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
			//si se ha cumplido eliminamos y terminamos
			this.vehicles.remove(vehicleToRemove);
			return vehicleToRemove;
		}
		return null;
	}
	
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
	
	public void receiveVehicle(Vehicle vehicle)
	{
		vehicle.stablishReserve("none");
		this.vehicles.add(vehicle);
	}
	
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
// Operaciones Vehiculos
	
}