package com.company.transportticketservice.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.company.transportticketservice.config.DbConfig;
import com.company.transportticketservice.model.Tickets;
import com.company.transportticketservice.model.VehicleRouteMap;

public class TicketDaoImplementation implements TicketDao {

	private static Connection connection = null;
	private static final Logger LOGGER = Logger.getLogger(TicketDaoImplementation.class);
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public TicketDaoImplementation() throws Exception {
		if (connection == null) {
			connection = DbConfig.getConnection();

		}
	}

	public void addTicket(Tickets tickets, Date date) throws Exception {
		LOGGER.info("Adding ticket");
		LOGGER.info("Checking vehicle exist for route : " + tickets.getVehicleRouteMap().getRoute().getRouteId()
				+ " at " + date);
		PreparedStatement statementCheckAvailability = connection
				.prepareStatement("select vehicle_route_id,seat_available from vehicle_route_map\r\n"
						+ "where vehicle_id=? and route_id=? and route_date=?");
		statementCheckAvailability.setInt(1, tickets.getVehicleRouteMap().getVehicle().getVehicleId());
		statementCheckAvailability.setInt(2, tickets.getVehicleRouteMap().getRoute().getRouteId());
		statementCheckAvailability.setDate(3, date);
		LOGGER.info("statementCheckAvailability" + statementCheckAvailability);
		VehicleRouteMap vehicleRouteMap = null;
		LOGGER.info("VehicleRouteMap received " + vehicleRouteMap);
		ResultSet rs = statementCheckAvailability.executeQuery();
		while (rs.next()) {
			vehicleRouteMap = new VehicleRouteMap();
			vehicleRouteMap.setVehicleRouteId(rs.getInt(1));
			vehicleRouteMap.setSeatAvailable(rs.getInt(2));
		}
		if (vehicleRouteMap != null) {
			if (vehicleRouteMap.getSeatAvailable() != 0) {
				LOGGER.info("inserting ticket");
				PreparedStatement statementInsertTicet = connection.prepareStatement(
						"INSERT INTO tickets ( vehicle_route_map_id,passenger_name, ticket_charge, created_date,created_by)\r\n"
								+ " VALUES (?,?,?,?,?)");
				java.util.Date createdDate = format.parse(tickets.getCreatedDate());
				statementInsertTicet.setInt(1, vehicleRouteMap.getVehicleRouteId());
				statementInsertTicet.setString(2, tickets.getPassengerName());
				statementInsertTicet.setInt(3, tickets.getTicketCharge());
				statementInsertTicet.setTimestamp(4, new Timestamp(createdDate.getTime()));
				statementInsertTicet.setString(5, tickets.getCreatedBy().getEmail());
				statementInsertTicet.executeUpdate();
				LOGGER.info("statementInsertTicet" + statementCheckAvailability);
				LOGGER.info("updating collection");
				PreparedStatement statementUpdateCollection = connection.prepareStatement(
						"UPDATE  vehicle_route_map SET total_collection = total_collection+?,seat_available=seat_available-1 \r\n"
								+ " WHERE (vehicle_route_id=?)");
				statementUpdateCollection.setInt(1, tickets.getTicketCharge());
				statementUpdateCollection.setInt(2, vehicleRouteMap.getVehicleRouteId());
				LOGGER.info("statementUpdateCollection" + statementUpdateCollection);
				int row = statementUpdateCollection.executeUpdate();
				if (row == 0) {
					LOGGER.info("Rollback ticket change");
					connection.rollback();
					LOGGER.error("Total collection updation failed");
					throw new Exception("Total Collection is not not updated check vehicle, route and route_date");
				}

			} else {
				LOGGER.error("Capacity is full");
				throw new Exception("Capacity is full. Seat not available");
			}
		} else {
			LOGGER.error("Route not assigned for selected vehicle on routedate");
			throw new Exception("Route not assigned for selected vehicle on routedate");
		}
		LOGGER.info("Commiting changes");
		connection.commit();
	}

}
