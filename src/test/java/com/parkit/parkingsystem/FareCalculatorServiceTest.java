package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;
	private LocalDateTime outTime;
	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();

		outTime = LocalDateTime.now();
	}

	@Test
	public void calculateFareCar() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR);

	}

	@Test
	public void calculateFareBike() {
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareUnkownType() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(2);

		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {

		LocalDateTime inTime = LocalDateTime.now().plusHours(2);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);// 45
		// minutes
		// parking
		// time
		// should
		// give
		// 3/4th
		// parking
		// fare

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(46);// 45
		// minutes
		// parking
		// time
		// should
		// give
		// 3/4th
		// parking
		// fare

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(24);// 24
		// hours
		// parking
		// time
		// should
		// give
		// 24
		// *
		// parking
		// fare
		// per
		// hour

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	void calculateFareCarWithLessThan30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);// 20
		// minutes
		// parking
		// time
		// should
		// be
		// free(
		// give
		// 0)

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	void calculateFareBikeWithLessThan30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);// 20
		// minutes
		// parking
		// time
		// should
		// be
		// free(
		// give
		// 0)

		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	void calculateFare_shouldThrowNullPointerException_ForNullParkingType() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);

		ParkingSpot parkingSpot = new ParkingSpot(4, null, false);// Parking
																	// type is
																	// null it
																	// should
																	// Throw
																	// NullPointerException
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// when
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));

		// Then

	}

}
