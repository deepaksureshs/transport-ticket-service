package com.company.transportticketservice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;

import com.company.transportticketservice.dao.TicketDao;
import com.company.transportticketservice.dao.TicketDaoImplementation;
import com.company.transportticketservice.model.Request;

public class TicketService {

	private static final Logger LOGGER = Logger.getLogger(TicketService.class);
	private static TicketService ticketService = null;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static TicketDao ticketDao;

	private TicketService() {
	}

	public static TicketService getTicketService() {
		if (ticketService != null) {
			return ticketService;
		} else {
			ticketService = new TicketService();
		}
		return ticketService;
	}

	public void addTicket(Request request, HttpHeaders headers) throws Exception {
		String token = headers.getHeaderString("token");
		if (token.contentEquals("admintoken")) {
			Date date = null;
			try {
				ticketDao = new TicketDaoImplementation();
				date = format.parse(request.getRouteDate());
				ticketDao.addTicket(request.getTickets(), new java.sql.Date(date.getTime()));
			} catch (ParseException e) {
				LOGGER.error("ParseException :: " + e);
				throw new Exception("recived incorrect date format, verify and try again");
			} catch (NullPointerException e) {
				LOGGER.error("NullPointerException :: " + e);
				throw new Exception("[ticket, vehicle , route] details are mandatory, verify and try again");
			} catch (Exception e) {
				LOGGER.error("Exception while adding ticket:: " + e);
				throw new Exception(e.getMessage());
			}
		} else {
			throw new Exception("You are not authorized");

		}
	}
}
