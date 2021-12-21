package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO,
					ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
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
	void processIncomingVehicleTest() throws Exception {
		// given
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingType parkingType = ParkingType.CAR;
		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
		when(parkingSpotDAO.vehicleIsInParking("ABCD")).thenReturn(true);
		ParkingSpot parkingSpot = new ParkingSpot(1, parkingType, true);
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCD");
		// When
		parkingService.processIncomingVehicle();
		// THEN
		verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
		verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
	}

	@Test
	void processExitingVehicleTest() throws Exception {
		// given

		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(
				new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class)))
				.thenReturn(true);
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

		// When
		parkingService.processExitingVehicle();
		// THEN
		verify(ticketDAO, times(1)).getTicket(anyString());
		verify(parkingSpotDAO, Mockito.times(1))
				.updateParking(any(ParkingSpot.class));
		verify(ticketDAO, times(1)).updateTicket(ticket);
	}
}
