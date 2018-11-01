package vehicleShareSystem.agents;

import vehicleShareSystem.Utils;
import vehicleShareSystem.behaviours.CyclicBehaviourMonitor;
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

public class AgentMonitor extends Agent
{
	
	protected CyclicBehaviourMonitor cyclicBehaviourMonitor;
	
	public ArrayList<String> stations;
	public ArrayList<String> stationVehicles;
	public ArrayList<String> users;
	public ArrayList<String> userVehicle;
	public ArrayList<String> userSituation;
	
	public void setup() 
	{
		System.out.println("SETUP!!\n\n");
		
		stations = new ArrayList<String>();
		stations.add("StationA");
		stations.add("StationB");
		stations.add("StationC");
		stations.add("StationD");
		
		stationVehicles = new ArrayList<String>();
		stationVehicles.add(" [ ] ");
		stationVehicles.add(" [ ] ");
		stationVehicles.add(" [ ] ");
		stationVehicles.add(" [ ] ");
		
		users = new ArrayList<String>();
		users.add("User1");
		users.add("User2");
		users.add("User3");
		users.add("User4");
		users.add("User5");
		
		userSituation = new ArrayList<String>();
		userSituation.add("");
		userSituation.add("");
		userSituation.add("");
		userSituation.add("");
		userSituation.add("");
		
		userVehicle = new ArrayList<String>();
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		
		if(getLocalName().equals("Monitor" ))
		{
			sd = new ServiceDescription();
			sd.setName("Monitor");
			sd.setType("Monitor");
			sd.addOntologies("ontologia");
			sd.addLanguages(new SLCodec().getName());
			dfd.addServices(sd);
			
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
		
		cyclicBehaviourMonitor = new CyclicBehaviourMonitor(this);
		this.addBehaviour(cyclicBehaviourMonitor);
	}
	
	public void printMonitorText()
	{
		int i = 0;
		for (String e : this.stations) 
		{
			System.out.println(e + ": " +stationVehicles.get(i));
			i++;
		}
		
		i = 0;
		for (String u : this.users) 
		{
			System.out.println(u + ": " +userSituation.get(i));
			i++;
		}
	}
	
	public void refreshStation(String station, String vehicles)
	{
		int i = 0;
		for (String e : this.stations) 
		{
			if(e.equals(station)) {
				this.stationVehicles.set(i, vehicles);
				break;
			}
			i++;
		}
	}
	
	public void refreshUser(String user, String situation)
	{
		int i = 0;
		for (String u : this.users) 
		{
			if(u.equals(user)) {
				this.userSituation.set(i, situation);
				break;
			}
			i++;
		}
	}
}