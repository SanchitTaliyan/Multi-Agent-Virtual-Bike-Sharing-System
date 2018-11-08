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
	
	public ArrayList<String> AB;
	public ArrayList<String> BD;
	public ArrayList<String> DC;
	public ArrayList<String> CA;
	public ArrayList<String> AD;
	public ArrayList<String> CB;

	
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

		AB = new ArrayList<String>();
		AB.add("--");
		AB.add("--");
		AB.add("--");
		AB.add("--");
		AB.add("--");

		BD = new ArrayList<String>();
		BD.add("| ");
		BD.add("| ");
		BD.add("| ");
		BD.add("| ");
		BD.add("| ");
		
		DC = new ArrayList<String>();
		DC.add("--");
		DC.add("--");
		DC.add("--");
		DC.add("--");
		DC.add("--");
		
		CA = new ArrayList<String>();
		CA.add("| ");
		CA.add("| ");
		CA.add("| ");
		CA.add("| ");
		CA.add("| ");
		
		AD = new ArrayList<String>();
		AD.add(" _");
		AD.add(" _");
		AD.add(" _");
		AD.add(" _");
		AD.add(" _");
		
		CB = new ArrayList<String>();
		CB.add(" _");
		CB.add(" _");
		CB.add(" _");
		CB.add(" _");
		CB.add(" _");
				
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
		
		cleanWays("User1");
		cleanWays("User1");
		cleanWays("User1");
		cleanWays("User1");
		cleanWays("User1");

		
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
				"A---------"+AB.get(0)+"-"+AB.get(1)+"-"+AB.get(2)+"-"+AB.get(3)+"-"+AB.get(4)+"-------B "+ stationVehicles.get(1)+" Users: "+stationUsers.get(1)+"\n"+
				"|\\ _                       _ / |\n"+
				"|    \\"+AD.get(0)+"               "+CB.get(4)+"/     |\n"+
				CA.get(0)+"       \\"+AD.get(1)+"       "+CB.get(3)+"/         "+BD.get(0)+"\n"+
				CA.get(1)+"           \\ _ /             "+BD.get(1)+"\n"+
				CA.get(2)+"        "+CB.get(2)+" /   \\"+AD.get(2)+"           "+BD.get(2)+"\n"+
				CA.get(3)+"     "+CB.get(1)+"/           \\"+AD.get(3)+"       "+BD.get(3)+"\n"+
				CA.get(4)+"  "+CB.get(0)+"/                  \\"+AD.get(4)+"   "+BD.get(4)+"\n"+
				"| /                          \\ |\n"+
				"C--------"+DC.get(0)+"-"+DC.get(1)+"-"+DC.get(2)+"-"+DC.get(3)+"-"+DC.get(4)+"--------D " + stationVehicles.get(3)+" Users: "+stationUsers.get(3)+"\n"+
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
		
		if(userInfo[0].equals("waiting") || userInfo[0].equals("final")) {
			currentStation = userInfo[1];
			this.placeUserOnStation(user,currentStation);
			situation = currentStation + "\t\t" + userInfo[0];
		} 
		
		if(userInfo[0].equals("moving")) {
			currentStation = userInfo[1];
			destinationStation = userInfo[2];
			vehicleType = userInfo[3];
			this.placeUserOnStation(user,"");
			this.placeUserOnTheWay(userName, currentStation, destinationStation, vehicleType);
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

	public void placeUserOnStation(String user, String currentStation)
	{
		this.cleanWays(user);
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
	
	public void cleanWays(String user)
	{
		int i = 0;
		for (String u : this.users) 
		{
			if(u.equals(user)) {
				this.AB.set(i, "--");
				this.BD.set(i, "| ");
				this.DC.set(i, "--");
				this.CA.set(i, "| ");
				this.AD.set(i, " _");
				this.CB.set(i, " _");
				break;
			}
			i++;
		}
	}
	public void placeUserOnTheWay(String user,String currentStation,String destinationStation,String vehicleType)
	{
		this.cleanWays(user);

		int i = 0;
		for (String u : this.users) 
		{
			if(u.equals(user)) {				
				String vehicle = " ";
				if(vehicleType.equals("bici")) vehicle = "b";
				if(vehicleType.equals("patin")) vehicle = "p";

				switch(currentStation+destinationStation) {
					case "StationAStationB":
					case "StationBStationA":
						this.AB.set(i, Integer.toString(i+1)+vehicle );
						break;
					case "StationBStationD":
					case "StationDStationB":
						this.BD.set(i, Integer.toString(i+1)+vehicle );
						break;
					case "StationDStationC":
					case "StationCStationD":
						this.DC.set(i, Integer.toString(i+1)+vehicle );
						break;
					case "StationCStationA":
					case "StationAStationC":
						this.CA.set(i, Integer.toString(i+1)+vehicle );
						break;
					case "StationAStationD":
					case "StationDStationA":
						this.AD.set(i, Integer.toString(i+1)+vehicle );
						break;
					case "StationCStationB":
					case "StationBStationC":
						this.CB.set(i, Integer.toString(i+1)+vehicle );
						break;
				}
				break;
			}
			i++;
		}
	}
}