package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)

public class ParkingServiceTest {

	private static ParkingService parkingService;

	private static LogCaptor logCaptor;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	private void setUpPerTest() throws RuntimeException {

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO,
				ticketDAO);

		logCaptor = LogCaptor.forName("ParkingService");
		logCaptor.setLogLevelToInfo();
		System.setOut(new PrintStream(outContent));

	}
	@Test
	void getNextParkingNumberIfAvailableTest() {
		ParkingType parkingType = ParkingType.CAR;
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
		ParkingSpot parkingSpot = parkingService
				.getNextParkingNumberIfAvailable();

		verify(parkingSpotDAO, times(1))
				.getNextAvailableSlot(any(ParkingType.class));
		verify(inputReaderUtil, times(1)).readSelection();
		assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
	}
	@Test
	void getNextParkingNumberIfAvailableTest_withNullParkingType() {

		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class)))
				.thenThrow(NullPointerException.class);
		ParkingSpot parkingSpot = parkingService
				.getNextParkingNumberIfAvailable();
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available parking slot");
		verify(parkingSpotDAO, times(1))
				.getNextAvailableSlot(any(ParkingType.class));
		verify(inputReaderUtil, times(1)).readSelection();
		assertThat(parkingSpot).isNull();
	}
	@Test
	void getNextParkingNumberIfAvailableTest_ForFullParkingSolts() {

		when(inputReaderUtil.readSelection()).thenReturn(2);

		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class)))
				.thenReturn(0);

		ParkingSpot parkingSpot = parkingService
				.getNextParkingNumberIfAvailable();

		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available parking slot");
		verify(parkingSpotDAO, times(1))
				.getNextAvailableSlot(any(ParkingType.class));
		verify(inputReaderUtil, times(1)).readSelection();
		assertThat(parkingSpot).isNull();
	}

	// @Test
	//
	// void
	// getNextParkingNumberIfAvailableTest_withIllegalArgumentExceptionInGetVehichleType()
	// {
	//
	// // Given
	//
	// when(inputReaderUtil.readSelection())
	// .thenThrow(IllegalArgumentException.class);
	//
	// // When
	// parkingService.getNextParkingNumberIfAvailable();
	//
	// // then
	//
	// assertThat(logCaptor.getErrorLogs())
	// .contains("Error parsing user input for type of vehicle");
	// }
	@Test
	void processIncomingVehicleTest_forCar() throws Exception {
		// given
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingType parkingType = ParkingType.CAR;
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCD");
		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
		when(parkingSpotDAO.vehicleIsInParking("ABCD")).thenReturn(true);
		when(ticketDAO.recurrentUser("ABCD")).thenReturn(true);
		ParkingSpot parkingSpot = new ParkingSpot(1, parkingType, true);

		// When
		parkingService.processIncomingVehicle();
		// THEN
		verify(ticketDAO, times(1)).recurrentUser("ABCD");
		verify(parkingSpotDAO, times(1)).getNextAvailableSlot(parkingType);
		verify(parkingSpotDAO, times(1)).vehicleIsInParking("ABCD");
		verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
		verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
	}
	@Test
	void processIncomingVehicleTest_forBike() throws Exception {
		// given
		when(inputReaderUtil.readSelection()).thenReturn(2);
		ParkingType parkingType = ParkingType.BIKE;
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCD");
		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
		when(parkingSpotDAO.vehicleIsInParking("ABCD")).thenReturn(true);
		when(ticketDAO.recurrentUser("ABCD")).thenReturn(false);
		ParkingSpot parkingSpot = new ParkingSpot(1, parkingType, true);

		// When
		parkingService.processIncomingVehicle();
		// THEN
		verify(ticketDAO, times(1)).recurrentUser("ABCD");
		verify(parkingSpotDAO, times(1)).getNextAvailableSlot(parkingType);
		verify(parkingSpotDAO, times(1)).vehicleIsInParking("ABCD");
		verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
		verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
	}
	@Test
	void processIncomingVehicleTest_forOthreParkingType() throws Exception {
		// given
		when(inputReaderUtil.readSelection()).thenReturn(3);

		// When
		parkingService.processIncomingVehicle();

		// THEN
		assertThat(outContent.toString()).contains("Incorrect input provided");
		assertThat(logCaptor.getErrorLogs())
				.contains("Error parsing user input for type of vehicle");
		verify(inputReaderUtil, times(1)).readSelection();

	}
	@Test
	void processIncomingVehicle_forAVehicleAlreadyInTheParking()
			throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingType parkingType = ParkingType.CAR;
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCD");
		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
		when(parkingSpotDAO.vehicleIsInParking("ABCD")).thenReturn(false);

		// When
		parkingService.processIncomingVehicle();
		// THEN
		assertThat(outContent.toString())
				.contains("this vehicle is already in the parking");
		assertThat(logCaptor.getErrorLogs())
				.contains("Unable to process incoming vehicle");
		verify(parkingSpotDAO, times(1)).getNextAvailableSlot(parkingType);
		verify(parkingSpotDAO, times(1)).vehicleIsInParking("ABCD");

	}
	// @Test
	// void processIncomingVehicleTest_forNullParkingSpot() throws Exception {
	// // given
	// when(inputReaderUtil.readSelection()).thenReturn(1);
	// when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class)))
	// .thenReturn(0);
	// // when(inputReaderUtil.readVehicleRegistrationNumber())
	// // .thenThrow(Exception.class);
	// //
	// // ICI////////////
	// // When
	// parkingService.processIncomingVehicle();
	// // THEN
	// assertThat(logCaptor.getErrorLogs())
	// .contains("Error fetching next available parking slot");
	// verify(parkingSpotDAO, times(1))
	// .getNextAvailableSlot(any(ParkingType.class));
	//
	// }

	@Test
	void processExitingVehicleTest_forANewUser() throws Exception {
		// given

		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(LocalDateTime.now().minusHours(1));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.recurrentUser("ABCDEF")).thenReturn(false);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class)))
				.thenReturn(true);
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

		// When
		parkingService.processExitingVehicle();

		// THEN
		assertThat(outContent.toString())
				.contains("Please pay the parking fare:" + ticket.getPrice());
		assertThat(outContent.toString())
				.contains("Recorded out-time for vehicle number:"
						+ ticket.getVehicleRegNumber() + " is:"
						+ ticket.getOutTime());
		verify(ticketDAO, times(1)).recurrentUser("ABCDEF");
		verify(ticketDAO, times(1)).getTicket(anyString());
		verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
		verify(ticketDAO, times(1)).updateTicket(ticket);
	}

	@Test
	void processExitingVehicle_forARecurringUser() throws Exception {
		// given

		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(LocalDateTime.now().minusHours(1));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.recurrentUser("ABCDEF")).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class)))
				.thenReturn(true);
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

		// When
		parkingService.processExitingVehicle();

		// THEN
		assertThat(outContent.toString())
				.contains("Please pay the parking fare:" + ticket.getPrice());
		assertThat(outContent.toString())
				.contains("Recorded out-time for vehicle number:"
						+ ticket.getVehicleRegNumber() + " is:"
						+ ticket.getOutTime());
		verify(ticketDAO, times(1)).recurrentUser("ABCDEF");
		verify(ticketDAO, times(1)).getTicket(anyString());
		verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
		verify(ticketDAO, times(1)).updateTicket(ticket);
	}
	@Test
	void processExitingVehicle_WithFailedTicketUpDate() throws Exception {
		// given

		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		Ticket ticket = new Ticket();
		ticket.setInTime(LocalDateTime.now().minusHours(1));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");

		when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
		when(ticketDAO.updateTicket(ticket)).thenReturn(false);
		// When
		parkingService.processExitingVehicle();
		// THEN
		assertThat(outContent.toString()).contains(
				"Unable to update ticket information. Error occurred");
		verify(ticketDAO, times(1)).getTicket(any(String.class));
		verify(ticketDAO, times(1)).updateTicket(ticket);
	}
	@Test
	void processExitingVehicle_WithFailedGetTicket() throws Exception {
		// given
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");

		when(ticketDAO.getTicket(anyString()))
				.thenThrow(NullPointerException.class);
		// When
		parkingService.processExitingVehicle();
		// THEN
		verify(ticketDAO, times(1)).getTicket(anyString());
		assertThat(logCaptor.getErrorLogs())
				.contains("Unable to process exiting vehicle");

	}

}
