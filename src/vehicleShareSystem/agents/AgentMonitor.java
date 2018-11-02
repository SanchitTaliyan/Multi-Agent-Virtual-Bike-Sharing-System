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
	public ArrayList<String> destinationStation;
	public ArrayList<String> currentStation;
	
	public ArrayList<String> userPlaceOnStation;
	
	
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
		
		userVehicle = new ArrayList<String>();
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		userVehicle.add("caminando");
		userVehicle.add("caminando");

		currentStation = new ArrayList<String>();
		currentStation.add("");
		currentStation.add("");
		currentStation.add("");
		currentStation.add("");
		currentStation.add("");
		
		destinationStation = new ArrayList<String>();
		destinationStation.add("");
		destinationStation.add("");
		destinationStation.add("");
		destinationStation.add("");
		destinationStation.add("");
		
		userPlaceOnStation = new ArrayList<String>();
		userPlaceOnStation.add("");
		userPlaceOnStation.add("");
		userPlaceOnStation.add("");
		userPlaceOnStation.add("");
		userPlaceOnStation.add("");

		
		
		userSituation = new ArrayList<String>();
		userSituation.add("");
		userSituation.add("");
		userSituation.add("");
		userSituation.add("");
		userSituation.add("");
		
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

	public void printMonitorMap()
	{
		//Generamos los usuarios que hay en cada estación
		ArrayList<String> stationUsers;
		stationUsers = new ArrayList<String>();
		stationUsers.add(" ");
		stationUsers.add(" ");
		stationUsers.add(" ");
		stationUsers.add(" ");
		
		int i = 0;
		for (String e : this.stations) 
		{
			String actualStation = stationUsers.get(i);
			int j = 0;
			for (String u : this.userPlaceOnStation) 
			{
				if(u.equals(e)) {
					actualStation = actualStation + " " + users.get(j);
				}
				j++;
			}
			stationUsers.set(i, actualStation);
			i++;
		}

		String buffer = "\n" +
				stationVehicles.get(0)+" Users: " + stationUsers.get(0) + "\n"+
				"A------------------------------B "+ stationVehicles.get(1)+" Users: "+stationUsers.get(1)+"\n"+
				"|\\ _                       _ / |\n"+
				"|    \\ _               _ /     |\n"+
				"|        \\ _       _ /         |\n"+
				"|            \\ _ /             |\n"+
				"|          _ /   \\ _           |\n"+
				"|       _/           \\ _       |\n"+
				"|   _ /                  \\ _   |\n"+
				"| /                          \\ |\n"+
				"C------------------------------D " + stationVehicles.get(3)+" Users: "+stationUsers.get(3)+"\n"+
				stationVehicles.get(2)+" Users: "+stationUsers.get(2)+"\n";
		
		System.out.println(buffer);
	}
	
	public void printMonitorText()
	{
		String buffer = "\n\n\n\n";
		
		int i = 0;
		for (String e : this.stations) 
		{
			buffer = buffer + e + ": " +stationVehicles.get(i) + "\n";
			i++;
		}
		
		i = 0;
		for (String u : this.users) 
		{
			buffer = buffer + u + ": " +userSituation.get(i)+ "\n";
			i++;
		}
		System.out.println(buffer);
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

	/*public ArrayList<String> stationUsers;
	
	public ArrayList<String> users;
	public ArrayList<String> userVehicle;
	public ArrayList<String> currentStation;
	public ArrayList<String> destinationStation;*/
	public void placeUserOnStation(String user, String currentStation)
	{
		int i = 0;
		for (String u : this.users) 
		{
			if(u.equals(user)) {
				this.userVehicle.set(i, "none");
				this.currentStation.set(i, currentStation);
				this.destinationStation.set(i, "none");
				
				this.userPlaceOnStation.set(i, currentStation);
				break;
			}
			i++;
		}
	}
	
	public void refreshUser(String user, String situation)
	{
		String currentStation;
		String destinationStation;
		String vehicleType;
		String userName = user;
		
		String[] userInfo = situation.split(" ");
		for (int i = 0; i<userInfo.length;i++) 
		{
			System.out.println(userInfo[i]);
		}
		
		if(userInfo[0].equals("waiting")) {
			currentStation = userInfo[1];
			this.placeUserOnStation(user,currentStation);
			situation = currentStation + "\t\t" + "waiting";
		} 
		
		if(userInfo[0].equals("moving")) {
			currentStation = userInfo[1];
			destinationStation = userInfo[2];
			vehicleType = userInfo[3];
			this.placeUserOnStation(user,"");

			situation = currentStation + " --> " + destinationStation + "\t" + vehicleType;
		}
		
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