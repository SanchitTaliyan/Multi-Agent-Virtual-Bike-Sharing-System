package vehicleShareSystem.model;

import java.io.Serializable;

public class Capsule implements Serializable {
	
	private Vehicle vehicle;
	private String vehicleType;
	
	private String station;
	private String user;
	
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
