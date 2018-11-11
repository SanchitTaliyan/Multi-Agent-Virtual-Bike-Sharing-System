# Multi-Agent Virtual Bike Sharing System

A multi-agent system that allows the instance of a network of vehicle stations and autonomous users that navigate freely through it.

Running test video example:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/zJhrfXbQmQU/0.jpg)](https://www.youtube.com/watch?v=zJhrfXbQmQU)

## Index

  - [Architecture](#Architecture)
  - [Prerequisites](#Prerequisites)
  - [Authors](#Authors)

## Architecture ##

#### Objects and agents classes:
![Alt text](doc/classDiagram.png?raw=true "ClassDiagram")

#### Communication:
![Alt text](doc/communicationUserStation.png?raw=true "Comunication between agents")
![Alt text](doc/communicationMonitor.png?raw=true "Comunication with monitor")

#### Agents Behaviour:
![Alt text](doc/userBehaviour.png?raw=true "User Behaviour")
![Alt text](doc/stationBehaviour.png?raw=true "Station Behaviour")

## Prerequisites ##
This project has been carried out through the platform for the development of JADE agents. This software is contained in this repository. The development and implementation was carried out on the Eclipse platform, this guide assumes its use.

## Using Jade on Eclipse ##

#### How to compile and run tests:

First of all you should run JADE within the main agent, on Eclipse go to "run configurations" -> "choose a java application" -> "pick jade.Boot as main class", after that go to Arguments and insert:
```
   -gui Monitor:vehicleShareSystem.agents.AgentMonitor 
```

Once this is done, we can instantiate the other agents: 
```
   -main false -host 127.0.0.1 **InstanceName**:vehicleShareSystem.agents.**AgentFile**
```

### Proposed instance
The current configuration allows the instantiation of 4 stations and up to 5 different users, the code can be easily modified to add or modify the agents' disposition.
**Stations**
```
    -main false -host 127.0.0.1 StationA:vehicleShareSystem.agents.AgentStation
    -main false -host 127.0.0.1 StationB:vehicleShareSystem.agents.AgentStation
    -main false -host 127.0.0.1 StationC:vehicleShareSystem.agents.AgentStation
    -main false -host 127.0.0.1 StationD:vehicleShareSystem.agents.AgentStation
```
**Users**
```
    -main false -host 127.0.0.1 User1:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User2:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User3:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User4:vehicleShareSystem.agents.AgentUser
    -main false -host 127.0.0.1 User5:vehicleShareSystem.agents.AgentUser
```


## Authors ## 

* **Adrián Valera Román** - *Architecture designer and main programmer* - [Adrixo](https://github.com/adrixo)
* **Diego Mateos Matilla** - *Architecture designer, bug resolver and tester* 

## Acknowledgments

We would like to thank our programming teacher who introduced us to the world of agents:
* Dr. Juan Manuel Corchado - [website](https://corchado.net/)
