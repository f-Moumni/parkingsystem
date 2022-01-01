package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import nl.altindag.log.LogCaptor;
@ExtendWith(MockitoExtension.class)
class TicketDAOTest {
	TicketDAO ticketDAO;
	Ticket ticket = new Ticket();
	private static LogCaptor logCaptor;
	@Mock
	PreparedStatement preparedStatement;
	@Mock
	DataBaseConfig dataBaseConfig;
	@BeforeEach
	private void setUpTest() {
		ticketDAO = new TicketDAO();
		ticket = new Ticket();
		ticket.setVehicleRegNumber("ABCD");
		ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
		ticket.setPrice(1);
		ticket.setInTime(LocalDateTime.now().minusHours(1));
		ticket.setOutTime(null);
		logCaptor = LogCaptor.forName("TicketDAO");
		logCaptor.setLogLevelToInfo();
	}
	@Test
	void saveTicketTest_shouldReturnTrue() {
		// When
		boolean result = ticketDAO.saveTicket(ticket);
		// Then
		assertTrue(result);
	}
	@Test
	void saveTicket_withOutTimeNotNull_shouldReturnTrue() {
		// Given
		ticket.setOutTime(LocalDateTime.now());
		// When
		boolean result = ticketDAO.saveTicket(ticket);
		// Then
		assertTrue(result);
	}
	@Test
	void saveTicket_withNullTicket_shouldReturnFalse() {
		// Given
		ticket = null;
		// When
		boolean result = ticketDAO.saveTicket(ticket);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available slot");
		assertFalse(result);
	}

	@Test
	void getTicket_shouldGetTicket_withvehicleRegNumberOutOffParking() {
		// Given
		String vehicleRegNumber = "ABCD";
		// When
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		// Then
		assertEquals("ABCD", resultTicket.getVehicleRegNumber());
	}
	@Test
	void getTicket_shouldGetTicket_withvehicleRegNumberInParking() {
		// Given
		String vehicleRegNumber = "AAA";
		// When
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		// Then
		assertThat(resultTicket.getVehicleRegNumber()).isEqualTo("AAA");
		assertThat(resultTicket.getOutTime()).isNull();
	}
	// @Test
	// void getTicket_shouldGetNullTicket_withNullvehicleRegNumber()
	// throws ClassNotFoundException, SQLException {
	//
	// String vehicleRegNumber = "00";
	// doThrow(SQLException.class).when(preparedStatement).close();
	// // .closePreparedStatement();
	// // given(dataBaseConfig.getConnection()).willAnswer( invocation -> {
	// // throw new Exception("Error fetching next available slot"); });
	//
	// // doAnswer(invocation -> {
	// // throw new SQLException();
	// // }).when(dataBaseConfig.getConnection());
	// // Connection con = null;
	// // when(dataBaseConfig.getConnection()).then(invocation -> {
	// // throw new Exception();
	// // });
	// Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
	// // assertThat(logCaptor.getErrorLogs())
	// // .contains("Error fetching next available slot");
	// assertThat(resultTicket).isNull();
	//
	// }
	@Test
	void updateTicket_shouldRetrunTrue() {
		ticket.setOutTime(LocalDateTime.now());
		boolean result = ticketDAO.updateTicket(ticket);
		assertTrue(result);
	}
	@Test
	void updateTicket_with_NullTicket_shouldRetrunFalse() {
		ticket = null;
		// ticket.setOutTime(LocalDateTime.now().minusHours(2));
		boolean result = ticketDAO.updateTicket(ticket);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error saving ticket info");
		assertFalse(result);
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
	@Test
	void recurrentUser_WithNullvehicleRegNumber_shouldRetrunTrue() {
		String vehicleRegNumber = null;
		boolean result = ticketDAO.recurrentUser(vehicleRegNumber);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching recurrent User ");
		assertFalse(result);
	}

}
