package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.service.InteractiveShell;

import nl.altindag.log.LogCaptor;
@ExtendWith(MockitoExtension.class)
class AppTest {

	App app;
	@Mock
	InteractiveShell interactiveShell;

	private static LogCaptor logCaptor;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	private void setUpPerTest() throws RuntimeException {

		app = new App();
		logCaptor = LogCaptor.forName("ParkingService");
		logCaptor.setLogLevelToInfo();
		System.setOut(new PrintStream(outContent));
	}
	// @Test
	void mainTest() {
		App.main(any());
		assertThat(outContent.toString()).contains(
				"Please select an option. Simply enter the number to choose an action");
		assertThat(outContent.toString())
				.contains("1 New Vehicle Entering - Allocate Parking Space");
		assertThat(outContent.toString())
				.contains("2 Vehicle Exiting - Generate Ticket Price");
		assertThat(outContent.toString()).contains("3 Shutdown System");

	}

}
