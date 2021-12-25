package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

class TicketDAOTest {
	TicketDAO ticketDAO = new TicketDAO();
	Ticket ticket = new Ticket();
	@BeforeEach
	private void setUpTest() {
		ticketDAO = new TicketDAO();
		ticket = new Ticket();
		ticket.setVehicleRegNumber("ABCD");
		ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
		ticket.setPrice(1);
		ticket.setInTime(LocalDateTime.now().minusHours(1));
		ticket.setOutTime(null);
	}
	@Test
	void saveTicket_shouldReturnTrue() {
		boolean result = ticketDAO.saveTicket(ticket);
		assertTrue(result);
	}
	@Test
	void getTicket_shouldGetTicket_withABCDInvehicleRegNumber() {

		String vehicleRegNumber = "ABCD";
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		assertEquals("ABCD", resultTicket.getVehicleRegNumber());
	}
	@Test
	void updateTicket_shouldRetrunTrue() {
		ticket.setOutTime(LocalDateTime.now());
		boolean result = ticketDAO.updateTicket(ticket);
		assertTrue(result);
	}
	@Test
	void recurrentUser_WithARecurringUser_shouldRetrunTrue() {
		String vehicleRegNumber = "ABCD";
		boolean result = ticketDAO.recurrentUser(vehicleRegNumber);
		assertTrue(result);
	}
	@Test
	void recurrentUser_WithANewUser_shouldRetrunFalse() {
		String vehicleRegNumber = "TOTO";
		boolean result = ticketDAO.recurrentUser(vehicleRegNumber);
		assertFalse(result);
	}

}
