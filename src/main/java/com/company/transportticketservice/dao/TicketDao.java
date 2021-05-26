package com.company.transportticketservice.dao;

import java.sql.Date;

import com.company.transportticketservice.model.Tickets;

public interface TicketDao {

	public void addTicket(Tickets tickets, Date date) throws Exception;
}
