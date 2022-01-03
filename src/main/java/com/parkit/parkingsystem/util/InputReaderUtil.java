package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {

	private Scanner scan = new Scanner(System.in, "UTF-8");
	private static final Logger LOGGER = LogManager.getLogger("" + "");

	public int readSelection() {
		try {
			int input = Integer.parseInt(scan.nextLine());
			return input;
		} catch (Exception e) {
			LOGGER.error("Error while reading user input from Shell", e);
			System.out.println(
					"Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null
					|| vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			LOGGER.error("Error while reading user input from Shell", e);
			System.out.println(
					"Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}

}
