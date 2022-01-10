package com.parkit.parkingsystem.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * ParkingService class is used to manage parking entry and exit process.
 * 
 * @author Tek , Fatima
 *
 */

public class ParkingService {
	/**
	 * Initialise a Logger used to send messages to the console.
	 */
	private static final Logger logger = LogManager.getLogger("ParkingService");
	/**
	 * Initialise a FareCalculatorService object
	 */
	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();
	/*
	 * Create a InputReaderUtil object
	 */
	private InputReaderUtil inputReaderUtil;
	/**
	 * Create a ParkingSpotDAO object via the interface.
	 */
	private ParkingSpotDAO parkingSpotDAO;
	/**
	 * Create a TicketDAO object via the interface.
	 */
	private TicketDAO ticketDAO;
	/**
	 * The Class constructor.
	 * 
	 * @param inputReaderUtil
	 * @param parkingSpotDAO
	 * @param ticketDAO
	 */
	public ParkingService(InputReaderUtil inputReaderUtil,
			ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}
	/**
	 * method manage the vehicule incoming process.
	 */
	public void processIncomingVehicle() {
		try {
			ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
			if (parkingSpot != null && parkingSpot.getId() > 0) {
				String vehicleRegNumber = getVehichleRegNumber();
				if (!parkingSpotDAO.vehicleIsInParking(vehicleRegNumber)) {
					if (ticketDAO.recurrentUser(vehicleRegNumber)) {
						System.out.println(
								"Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount");
					}

					parkingSpot.setAvailable(false);
					parkingSpotDAO.updateParking(parkingSpot);
					Ticket ticket = new Ticket();
					// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME,
					// OUT_TIME)
					// ticket.setId(ticketID);
					ticket.setParkingSpot(parkingSpot);
					ticket.setVehicleRegNumber(vehicleRegNumber);
					ticket.setPrice(0);
					ticket.setInTime(LocalDateTime.now());
					ticket.setOutTime(null);
					ticketDAO.saveTicket(ticket);
					System.out.println("Generated Ticket and saved in DB");
					System.out
							.println("Please park your vehicle in spot number:"
									+ parkingSpot.getId());
					System.out.println("Recorded in-time for vehicle number:"
							+ vehicleRegNumber + " is:" + LocalDateTime.now());
				} else {
					System.out
							.println("this vehicle is already in the parking");
					throw new Exception(
							"Error vehicle is already in the parking");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to process incoming vehicle");
		}
	}
	/**
	 * This method asks a user for his registering number and call the external
	 * method that will read his keyboard input.
	 *
	 * @return a String - the registering number or "ILLEGAL ARGUMENT"
	 * @throws IOException
	 */
	private String getVehichleRegNumber() throws Exception {
		System.out.println(
				"Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();
	}
	/**
	 * Method used to find a available parking spot for the incoming vehicule.
	 *
	 * @return a ParkingSpot object
	 */
	public ParkingSpot getNextParkingNumberIfAvailable() {
		int parkingNumber = 0;
		ParkingSpot parkingSpot = null;
		try {
			ParkingType parkingType = getVehichleType();
			parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
			if (parkingNumber > 0) {
				parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
			} else {
				throw new Exception(
						"Error fetching parking number from DB. Parking slots might be full");
			}
		} catch (IllegalArgumentException ie) {
			logger.error("Error parsing user input for type of vehicle");
		} catch (Exception e) {
			logger.error("Error fetching next available parking slot");
		}
		return parkingSpot;
	}
	/**
	 * This method ask the user for his vehicle type and call the external
	 * method that will read his keyboard input.
	 *
	 * @return a ParkingType object
	 */
	private ParkingType getVehichleType() {
		System.out.println("Please select vehicle type from menu");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
			case 1 : {
				return ParkingType.CAR;
			}
			case 2 : {
				return ParkingType.BIKE;
			}
			default : {
				System.out.println("Incorrect input provided");
				throw new IllegalArgumentException("Entered input is invalid");
			}
		}
	}
	/**
	 * This method manage the vehicule exit process.
	 */
	public void processExitingVehicle() {
		try {
			String vehicleRegNumber = getVehichleRegNumber();
			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			LocalDateTime outTime = LocalDateTime.now();
			ticket.setOutTime(outTime);
			fareCalculatorService.calculateFare(ticket);
			if (ticketDAO.recurrentUser(vehicleRegNumber)) {
				fareCalculatorService.fivePercentDiscount(ticket);
			}
			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);
				System.out.println(
						"Please pay the parking fare:" + ticket.getPrice());
				System.out.println("Recorded out-time for vehicle number:"
						+ ticket.getVehicleRegNumber() + " is:" + outTime);
			} else {
				System.out.println(
						"Unable to update ticket information. Error occurred");
			}
		} catch (Exception e) {
			logger.error("Unable to process exiting vehicle");
		}
	}
}
