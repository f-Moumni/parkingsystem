package com.parkit.parkingsystem.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.altindag.log.LogCaptor;

public class InputReaderUtilTest {
	
	private InputReaderUtil inputReaderUtil;
	private static LogCaptor logCaptor;

	@BeforeAll
	static void setUp() {

	}
	@BeforeEach
	private void setUpPerTest() {

		logCaptor = LogCaptor.forName("InputReaderUtil");
		logCaptor.setLogLevelToInfo();

	}

	@Test
	void readSelectionTest_withValidInput() throws IOException {

		// Given
		System.setIn(System.in);
		String in = "1";

		ByteArrayInputStream stream = new ByteArrayInputStream(
				in.getBytes(StandardCharsets.UTF_8));
		System.setIn(stream);
		inputReaderUtil = new InputReaderUtil();
		// When

		// then
		assertThat(inputReaderUtil.readSelection()).isEqualTo(1);
		System.setIn(System.in);
		stream.close();
	}

	@Test
	void readSelectionTest_WithInvalidInput() throws IOException {
		System.setIn(System.in);
		String input = "m";
		ByteArrayInputStream stream = new ByteArrayInputStream(
				input.getBytes());
		System.setIn(stream);
		inputReaderUtil = new InputReaderUtil();
		assertThat(inputReaderUtil.readSelection()).isEqualTo(-1);
		// assertThat(logCaptor.getErrorLogs())
		// .contains("Error while reading user input from Shell");
		// System.setIn(System.in);
		stream.close();
	}

}
