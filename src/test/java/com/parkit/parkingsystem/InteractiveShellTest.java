package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.util.InputReaderUtil;

import nl.altindag.log.LogCaptor;
@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {

	InteractiveShell interactiveShell;
	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	private static LogCaptor logCaptor;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	private void setUpPerTest() {

		logCaptor = LogCaptor.forName("InteractiveShell");
		logCaptor.setLogLevelToInfo();
		System.setOut(new PrintStream(outContent));

	}
	@Disabled
	@Test
	void loadInterfaceTest_processIncomingVehicleChoose() {

		// Given
		System.setIn(System.in);
		String in = "1";

		ByteArrayInputStream stream = new ByteArrayInputStream(
				in.getBytes(StandardCharsets.UTF_8));
		System.setIn(stream);

		when(inputReaderUtil.readSelection()).thenReturn(1);

		InteractiveShell.loadInterface();
		assertThat(outContent.toString()).contains(
				"Please select an option. Simply enter the number to choose an action");
		assertThat(outContent.toString())
				.contains("1 New Vehicle Entering - Allocate Parking Space");
		assertThat(outContent.toString())
				.contains("2 Vehicle Exiting - Generate Ticket Price");
		assertThat(outContent.toString()).contains("3 Shutdown System");

	}

}
