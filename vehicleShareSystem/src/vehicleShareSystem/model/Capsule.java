package vehicleShareSystem.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Capsule implements Serializable {
	
	//This capsule contains the information and objects that the agents use for the communication
	private Vehicle vehicle;
	private String vehicleType;
	private String station;
	private String user;
	
	/*
	 * Messages:
	 * 		User: 
	 * 			- VEHICLE_REQUEST:  Capsule(null, desiredVehicle, currentStation, null)
	 * 			- VEHICLE_RETURN:	Capsule(Vehicle, null, null, null)
	 * 
	 * 		station:
	 * 			- VEHICLE_DELIVERY:				Capsule(Vehicle, null, null, null)
	 * 			- UNSATISFACTORY_REQUEST:		Capsule(vehicle,null,alternativeStation,null) //alternativeStation and/or vehicle can be null
	 * 			- VEHICLE_RESERVATION_REQUEST:	Capsule(null, desiredVehicle, askingStation , user requesting for a reservation)
	 * 			- VEHICLE_RESERVED:				void
	 * 			- VEHICLE_NOT_RESERVED:			void
	 */
	
	public Capsule(Vehicle vehicle, String vehicleType, String station, String user)
	{
		this.vehicle = vehicle;
		this.vehicleType = vehicleType;
		this.station = station;
		this.user = user;
	}
	
	public Vehicle getVehicle()
	{
		return this.vehicle;
	}

	public String getVehicleType()
	{
		return this.vehicleType;
	}

	public String getStation()
	{
		return this.station;
	}

	public String getUser()
	{
		return this.user;
	}
	
}
