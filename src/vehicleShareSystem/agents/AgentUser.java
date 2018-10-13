package vehicleShareSystem.agents;

import vehicleShareSystem.model.Vehicle;
import vehicleShareSystem.behaviours.CyclicBehaviourUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentUser extends Agent {

	public String agentName;
	
	public Vehicle vehicle;
	public String desiredVehicle;
	public ArrayList<String> vehicles;
	
	public String currentStation;
	public String destinationStation;
	public String desiredStation;
	
	public ArrayList<String> stations;
	
	protected CyclicBehaviourUser cyclicBehaviourUser;
	
	
	public void setup() 
	{
		vehicles = new ArrayList<String>();
		vehicles.add("bici");
		vehicles.add("patin");
		
		stations = new ArrayList<String>();
		stations.add("StationA");
		stations.add("StationB");
		stations.add("StationC");
		stations.add("StationD");
		
		
		this.vehicle = null;
		
		// DF debe registrar al usuario
		// DF es el que se encarga de que sea encontradohayVehiculo
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		
		//Registramos uno o varios servicios
		ServiceDescription sd = new ServiceDescription();
		sd.setName("user");
		//establecer tipo de servicio para poder localizarlo cuando se haga una busqueda
		sd.setType("user");
		
		//agentes que quieren usar este servicio tienen que tener un vocabulario común
		sd.addOntologies("ontologia");
		//agentes necesitan hablar FIPA-SL lenguaje
		sd.addLanguages(new SLCodec().getName());
		
		//Anadimos los servicios registrados
		dfd.addServices(sd);
		
	
		
		try
		{
			//registro los servicios
			DFService.register(this,dfd);
		}
		catch(FIPAException e){
			System.err.println("Agent "+getLocalName() +": " + e.getMessage());
			//doDelete();
		}

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
			this.stablishDesiredVehicle("patin");
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
			this.stablishDesiredVehicle("patin");
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
			this.stablishDesiredVehicle("patin");
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
			this.stablishDesiredVehicle("bici");
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
			this.stablishDesiredVehicle("bici");
		}
		
		try
		{
			//registro los servicios
			DFService.register(this,dfd);
		}
		catch(FIPAException e){
			System.err.println("Agent "+getLocalName() +": " + e.getMessage());
		}
		
		cyclicBehaviourUser = new CyclicBehaviourUser(this);
		this.addBehaviour(cyclicBehaviourUser);
	}
// Setup
	
// Manejo de estaciones
	public void stablishCurrentStation(String currentStation)
	{
		this.currentStation = currentStation;
	}
	
	public void stablishCurrentStation()
	{
		Random rand = new Random();
		int index = rand.nextInt(stations.size());
		
		this.currentStation = stations.get(index);
	}
	
	public void stablishDestinationStation(String destinationStation)
	{
		this.destinationStation = destinationStation;
	}
	
	public void stablishDesiredStation()
	{
		Random rand = new Random();
		int index = rand.nextInt(stations.size());
		
		this.desiredStation = stations.get(index);
	}
	
	public void stablishDesiredStation(String desiredStation)
	{
		this.desiredStation = desiredStation;
	}
	
	public String getDesiredStation()
	{
		return this.desiredStation;
	}
	
	public String getCurrentStation()
	{
		return this.currentStation;
	}
	
	public boolean arrivedToFinalStation()
	{
		if(this.desiredStation.equals(this.currentStation))
			return true;
		else
			return false;
	}
// Manejo de estaciones
	
// Manejo de vehiculo
	public void stablishDesiredVehicle(String vehiculoDeseado)
	{
		this.desiredVehicle = vehiculoDeseado;
	}
	
	public void stablishDesiredVehicle()
	{
		Random rand = new Random();
		int index = rand.nextInt(vehicles.size());
		
		this.desiredStation = vehicles.get(index);
	}
	
	public boolean hasVehicle()
	{
		if(this.vehicle == null)
			return false;
		else
			return true;
	}
	
	public void takeVehicle(Vehicle vehicle)
	{
		this.vehicle = vehicle;
		this.vehicle.stablishReserve("none");
	}
	
	public Vehicle leaveVehicle()
	{
		Vehicle aux = this.vehicle;
		this.vehicle = null;
		return aux;
	}
// Manejo de vehiculo
	
// Acciones
	public void waitSomeTime(int miliSeconds)
	{
		try{
		    Thread.sleep(miliSeconds);
		}
		catch(InterruptedException ex){
		    Thread.currentThread().interrupt();
		}
	}
	
	public void goToStation()
	{
		this.waitSomeTime(2000);
		this.currentStation = this.destinationStation;
		//this.destinationStation = null;
	}
//

}