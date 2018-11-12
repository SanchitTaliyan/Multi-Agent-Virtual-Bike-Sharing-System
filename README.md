# Multi-Agent Virtual Bike Sharing System

A multi-agent system that allows the instance of a network of vehicle stations and autonomous users that navigate freely through it.

Running test video example:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/zJhrfXbQmQU/0.jpg)](https://www.youtube.com/watch?v=zJhrfXbQmQU)


## Index

  - [Architecture](#Architecture)
  - [Prerequisites](#Prerequisites)
  - [Problems](#Problems)
  - [Authors](#Authors)


## Architecture ##

Proposed map:

![Alt text](doc/Map.png?raw=true "ClassDiagram")

The vehicle sharing service in a city consists of stops and vehicles in these. A system is developed in which several stations will form a network in a hypothetical city; each stop will be an autonomous agent.

On the other hand we have users who use the service, who want to use the vehicles. These are also independent agents, who take the desired vehicles from the stations and move between them.

### Objects and agents classes:

The main objective of the system is the handling of the object vehicle, by which the service that we raised exists; this one is sent between agents by means of messages. In the following image you can see the main classes that make up the system.

![Alt text](doc/classDiagram.png?raw=true "ClassDiagram")


in addition there is a **capsule class** that packs both the vehicle and the various information transmitted by the agents (desired stations, users who wish to reserve a type of vehicle ...)

As we will explain in the [problems section](#Problems), the monitor has a poor design and has not been included in this diagram because I don't want to give too much importance.


## Communication:

**Comunication between user/stations:**
Users move freely on the map made up of stations, therefore they only communicate individually with the station in which they are. The station asked, trying to satisfy the request of a user, may try to communicate with nearby stations to reserve a vehicle.

![Alt text](doc/communicationUserStation.png?raw=true "Comunication between agents")

**Notifications to the monitor:**
Each time an agent performs an action, it notifies the central monitor that manages information about all the agents that make up the ecosystem.

![Alt text](doc/communicationMonitor.png?raw=true "Comunication with monitor")


## Agents Behaviour:
The agents, autonomous programmes of flexible, reactive, proactive and social behaviour, behave according to the cyclic behaviour described in the folder *./VirtualBikeSharingSystem/bin/vehicleShareSystem/behaviours/*
The two main agents involved in the system have been programmed following the structure described in the following diagrams: 

- **User behaviour:**

![Alt text](doc/userBehaviour.png?raw=true "User Behaviour")

- **Station behaviour:**

![Alt text](doc/stationBehaviour.png?raw=true "Station Behaviour")

As we see the redirection of messages is quite simple, a someone wanted to add a new functionality or behavior to an agent he could add a new case type, a new column in the diagram.


## Prerequisites ##
This project has been carried out through the platform for the development of JADE agents. This software is contained in this repository. The development and implementation was carried out on the Eclipse platform, this guide assumes its use.

## Using Jade on Eclipse ##

#### How to compile and run tests:

First of all you should run JADE within the main agent, on Eclipse go to "run configurations" -> "choose a java application" -> "pick jade.Boot as main class", after that insert as an argument this line:
```
   -gui Monitor:vehicleShareSystem.agents.AgentMonitor 
```

Once this is done, we can instantiate the other agents creating new configurations with diferent arguments: 
```
   -main false -host 127.0.0.1 **InstanceName**:vehicleShareSystem.agents.**AgentFile**
```

### Proposed instance
The current configuration allows the instantiation of 4 stations and up to 5 different users, the code can be easily modified to add stations/users or edit the agents' disposition.

**Stations:**
```
    -main false -host 127.0.0.1 StationA:vehicleShareSystem.agents.AgentStation
    -main false -host 127.0.0.1 StationB:vehicleShareSystem.agents.AgentStation
    -main false -host 127.0.0.1 StationC:vehicleShareSystem.agents.AgentStation
    -main false -host 127.0.0.1 StationD:vehicleShareSystem.agents.AgentStation
```
**Users:**
```
    -main false -host 127.0.0.1 User1:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User2:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User3:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User4:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User5:vehicleShareSystem.agents.AgentUser
```

If all the agents run correctly on the main monitor we will see an operation like this:

![Alt text](doc/monitorExample.png?raw=true "ClassDiagram")

In the same way each agent has its own internal dialogue, prompted in its own terminal:

**StationA:**

![Alt text](doc/stationExample.png?raw=true "ClassDiagram")

**User1:**

![Alt text](doc/userExample.png?raw=true "ClassDiagram")


## Problems ## 

the monitor agent has a bad design. It was not conceived as something fundamental in the bicycle sharing system but was added later to facilitate the understanding of the operation of the agents.

Although it works, the code is a western spaghetti and does not have to be taken too seriously. A modification or an attempt to add some new functionality can be an arduous task, the best would be to change the system with which the information is displayed.


## Authors ## 

* **Adrián Valera Román** - *Architecture designer and main programmer* - [Adrixo](https://github.com/adrixo)
* **Diego Mateos Matilla** - *Architecture designer, bug resolver and tester* 

## Acknowledgments

We would like to thank our programming teacher who introduced us to the world of agents:
* Dr. Juan Manuel Corchado - [website](https://corchado.net/)




Developed with java language in eclipse.
