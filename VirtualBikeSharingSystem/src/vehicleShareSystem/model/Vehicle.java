package vehicleShareSystem.model;

import java.io.Serializable;

public class Vehicle implements Serializable {

	String type;
	String reserve;
	
	public Vehicle(String type)
	{
		this.type=type;
		this.reserve = "none";
	}

	public String getType()
	{
		return this.type;
	}
	
	public Boolean isType(String type)
	{
		if(this.type.equals(type))
			return true;
		else
			return false;
	}
	
	public Boolean isReserved()
	{
		if(this.reserve.equals("none"))
			return false;
		else 
			return true;
	}
	
	public Boolean isReserved(String user)
	{
		if(this.reserve.equals(user))
			return true;
		else
			return false;
	}
	
	public void stablishReserve(String user)
	{
		this.reserve = user;
	}
}
