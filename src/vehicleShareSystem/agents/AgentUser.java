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
	
	public String currentStation;
	public String destinationStation;
	public String desiredStation;
	
	public ArrayList<String> stations;
	public ArrayList<String> vehicles;
	
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
		vehicles.add("bici");
		vehicles.add("patin");
		
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
			this.stablishDesiredVehicle("bici");
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
			//doDelete();
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
	
	public void stablishDestinationStation(String destinationStation)
	{
		this.destinationStation = destinationStation;
	}
	
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
	
	public String getDestinationStation()
	{
		return this.destinationStation;
	}
	
	public boolean arrivedToFinalStation()
	{
		if(this.desiredStation.equals(this.currentStation)){
			System.out.println("!#!\tI arrived to Final station: " + this.getCurrentStation()+ ". \n");
			return true;
		}
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
	
	public String getDesiredVehicle()
	{
		return this.desiredVehicle;
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
		System.out.println("\tI recived " + this.vehicle.getType());
	}
	
	public String getVehicleName()
	{
		if(this.vehicle != null)
			return this.vehicle.getType();
		else
			return "walking";
	}
	
	public Vehicle leaveVehicle()
	{
		System.out.println("\tI'm at "+ this.getCurrentStation()  +" and I leave the "+ this.vehicle.getType()+ ". ");
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
		if(this.hasVehicle())
			System.out.println("\t"+this.getCurrentStation() + " -------"+this.getVehicleName()+"--------> " + this.getDestinationStation()  + ". ");
		else
			System.out.println("\t"+this.getCurrentStation() + " -------walking--------> " + this.getDestinationStation()  + ". ");

		this.waitSomeTime(12000);
		this.currentStation = this.destinationStation;
		this.destinationStation = this.desiredStation;
	}
//

}