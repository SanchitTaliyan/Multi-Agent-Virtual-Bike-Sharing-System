package vehicleShareSystem.agents;

import vehicleShareSystem.behaviours.CyclicBehaviourMonitor;

import java.util.ArrayList;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

//Run configuration required:
//-gui Monitor:vehicleShareSystem.agents.AgentMonitor
@SuppressWarnings("serial")
public class AgentMonitor extends Agent
{
	
	protected CyclicBehaviourMonitor cyclicBehaviourMonitor;
	

	/*
	 * lists declared subsequently use a common index. 
	 * e.g: 
	 * - stationVehicles(1) contains station(1) vehicles 
	 * - currentStation(2) contains the position of the user(2)
	 * 
	 * This is not the best way to program a monitor but we ran out of budget. 
	 */
	
//Station information
	public ArrayList<String> stations; // Stations names on the system
	public ArrayList<String> stationVehicles; // list of vehicles for each station
	
	public ArrayList<String> userPlaceOnStation; //Users placed on a station
	
	//Ways between stations
	public ArrayList<String> AB;
	public ArrayList<String> BD;
	public ArrayList<String> DC;
	public ArrayList<String> CA;
	public ArrayList<String> AD;
	public ArrayList<String> CB;
//Station information
	
//User information
	public ArrayList<String> users; //User's name list
	public ArrayList<String> userVehicle; // user's vehicle
	public ArrayList<String> destinationStation; //user's destinationStation: Only used when is on the way to another station
	public ArrayList<String> currentStation; //user's current station: if the user is travelling it means the origin station	
	public ArrayList<String> userSituation; //User's status
//User information

	public void setup() 
	{
		
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
		userVehicle.add("walking");
		userVehicle.add("walking");
		userVehicle.add("walking");
		userVehicle.add("walking");
		userVehicle.add("walking");

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
		cleanWays("User2");
		cleanWays("User3");
		cleanWays("User4");
		cleanWays("User5");

		
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
			DFService.register(this,dfd);
		}
		catch(FIPAException e){
			System.err.println("Agent "+getLocalName() +": " + e.getMessage());
		}
		
		System.out.print("SETUP!! : " + getLocalName());
		
		cyclicBehaviourMonitor = new CyclicBehaviourMonitor(this);
		this.addBehaviour(cyclicBehaviourMonitor);
	}

	/*
	 * Prints the stations' network map
	 */
	public void printMonitorMap()
	{
		//restart stations current users 
		ArrayList<String> stationUsers;
		stationUsers = new ArrayList<String>();
		stationUsers.add(" ");
		stationUsers.add(" ");
		stationUsers.add(" ");
		stationUsers.add(" ");
		
		//place the users on stations
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

		//buffering and printing the map
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
	
	/*
	 * Prints a text mode monitor with information of stations (vehicles and users on each one)
	 * 	and the users status
	 */
	public void printMonitorText()
	{
		
		String buffer = "\n\n\n\n";
		
		//stations info
		int i = 0;
		for (String e : this.stations) 
		{
			buffer = buffer + e + ": " +stationVehicles.get(i) + "\n";
			i++;
		}
		
		//users info
		i = 0;
		for (String u : this.users) 
		{
			buffer = buffer + u + ": " +userSituation.get(i)+ "\n";
			i++;
		}
		System.out.println(buffer);
	}
	

	/*
	 * stablish a list of vehicles to a given station
	 */
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

	/*
	 * Updates the information given to a user
	 */
	public void refreshUser(String user, String situation)
	{
		String currentStation;
		String destinationStation;
		String vehicleType;
		String userName = user;
		
		String[] userInfo = situation.split(" ");
		
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

	/*
	 * Places a user on a station, deleting "moving" information
	 */
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
	

	/*
	 * cleans up all the ways users travel on
	 */
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
	
	/*
	 * Places a user on a way that conects two stations
	 */
	public void placeUserOnTheWay(String user,String currentStation,String destinationStation,String vehicleType)
	{
		this.cleanWays(user);

		int i = 0;
		for (String u : this.users) 
		{
			if(u.equals(user)) {				
				String vehicle = " ";
				if(vehicleType.equals("bike")) vehicle = "b";
				if(vehicleType.equals("scooter")) vehicle = "s";

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