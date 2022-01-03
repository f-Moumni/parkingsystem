package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
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
		interactiveShell = new InteractiveShell();
		logCaptor = LogCaptor.forName("InteractiveShell");
		logCaptor.setLogLevelToInfo();
		System.setOut(new PrintStream(outContent));

	}
	// public int getInput(String input) {
	//
	// Scanner scan = new Scanner(new ByteArrayInputStream(input.getBytes()));
	// return Integer.parseInt(scan.nextLine());
	// }
	// @Test
	void loadInterfaceTest_processIncomingVehicleChoose() {

		Scanner scan = new Scanner(new ByteArrayInputStream("1".getBytes()));
		// return Integer.parseInt(scan.nextLine());
		int input = Integer.parseInt(scan.nextLine());;
		// InputStream in = new ByteArrayInputStream(input.getBytes());
		// System.setIn(in);
		when(inputReaderUtil.readSelection()).thenReturn(input);
		// interactiveShell = new InteractiveShell();
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
