package com.company.transportticketservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Tickets {

	private Integer ticketId;
	private String passengerName;
	private Integer ticketCharge;
	private String createdDate;
	private VehicleRouteMap vehicleRouteMap;
	private User createdBy;

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public Integer getTicketCharge() {
		return ticketCharge;
	}

	public void setTicketCharge(Integer ticketCharge) {
		this.ticketCharge = ticketCharge;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public VehicleRouteMap getVehicleRouteMap() {
		return vehicleRouteMap;
	}

	public void setVehicleRouteMap(VehicleRouteMap vehicleRouteMap) {
		this.vehicleRouteMap = vehicleRouteMap;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "Tickets [ticketId=" + ticketId + ", passengerName=" + passengerName + ", ticketCharge=" + ticketCharge
				+ ", createdDate=" + createdDate + ", vehicleRouteMap=" + vehicleRouteMap + ", createdBy=" + createdBy
				+ "]";
	}

}
