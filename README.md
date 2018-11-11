# Multi-Agent-Virtual-Bike-Sharing-System

### Prerequisites
This project has been carried out through the platform for the development of JADE agents. This software is contained in this repository. The development and implementation was carried out on the Eclipse platform, this guide assumes its use.

## Using Jade on Eclipse ##

#### How to compile and run tests:

First of all you should run JADE within the main agent, on Eclipse go to "run configurations" -> "choose a java application" -> "pick jade.Boot as main class", after that go to Arguments and insert:
```
   -gui Monitor:vehicleShareSystem.agents.AgentMonitor |
```

Once this is done, we can instantiate the other agents: 
```
-main false -host 127.0.0.1 **InstanceName**:vehicleShareSystem.agents.**AgentFile**
```

### Proposed instance 
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
