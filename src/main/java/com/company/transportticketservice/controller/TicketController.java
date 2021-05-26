package com.company.transportticketservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import com.company.transportticketservice.config.MapperConfig;
import com.company.transportticketservice.model.ErrorResponse;
import com.company.transportticketservice.model.Request;
import com.company.transportticketservice.service.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketController {

	private static final Logger LOGGER = Logger.getLogger(TicketController.class);
	private static ObjectMapper mapper = MapperConfig.getMapper();
	private static TicketService ticketService = TicketService.getTicketService();

	@POST
	public Response addTicket(String payload, @Context HttpHeaders headers) {
		LOGGER.info("Request received for ticket generation :: payload : " + payload);
		Request request = new Request();
		try {
			request = mapper.readValue(payload, Request.class);
			ticketService.addTicket(request, headers);
			LOGGER.info("Request Processed Successfull");
		} catch (Exception exception) {
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setStatus("Error");
			errorResponse.setMessage(exception.getMessage());
			LOGGER.error("Error response :: " + errorResponse);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
		}
		return Response.status(Status.CREATED).build();

	}

}
