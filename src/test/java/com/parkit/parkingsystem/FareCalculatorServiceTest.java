package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	void calculateFareCar() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);

		assertThat(ticket.getPrice())
				.isEqualTo(Precision.round(Fare.CAR_RATE_PER_HOUR, 2));

	}

	@Test
	void calculateFareBike() {
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertThat(ticket.getPrice())
				.isEqualTo(Precision.round(Fare.BIKE_RATE_PER_HOUR, 2));
	}

	@Test
	void calculateFareUnkownType() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(2);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	void calculateFareBikeWithFutureInTime() {

		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	void calculateFareBikeWithLessThanOneHourParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);// 45
		// minutes
		// parking
		// time
		// should
		// give
		// 3/4th
		// parking
		// fare
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Precision.round(0.75 * Fare.BIKE_RATE_PER_HOUR, 2),
				ticket.getPrice());
	}

	@Test
	void calculateFareCarWithLessThanOneHourParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);// 45
		// minutes
		// parking
		// time
		// should
		// give
		// 3/4th
		// parking
		// fare
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Precision.round(0.75 * Fare.CAR_RATE_PER_HOUR, 2),
				ticket.getPrice());
	}

	@Test
	void calculateFareCarWithMoreThanADayParkingTime() {

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
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Precision.round(24 * Fare.CAR_RATE_PER_HOUR, 2),
				ticket.getPrice());
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
		LocalDateTime outTime = LocalDateTime.now();
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
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	void calculateFareBikeWith30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);// 30
																	// minutes
		// parking
		// time
		// should
		// be
		// free(
		// give
		// 0)
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	void calculateFareCarWith30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);// 30
																	// minutes
		// parking
		// time
		// should
		// be
		// free(
		// give
		// 0)
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}

	@Test
	void fivePercentDiscount_ForOneHour_OfCarParking() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);// 5% discount
																	// for 1
																	// hour of
																	// car
																	// parking
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCD");

		double fare = ticket.getPrice() - (ticket.getPrice() * 0.05);

		// When
		fareCalculatorService.fivePercentDiscount(ticket);
		// Then
		assertEquals(Precision.round(fare, 2), ticket.getPrice());
	}
	@Test
	void fivePercentDiscount_ForOneHour_OfBikeParking() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);// 5% discount
																	// for 1
																	// hour of
																	// bike
																	// parking
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCD");

		double fare = ticket.getPrice() - (ticket.getPrice() * 0.05);

		// When
		fareCalculatorService.fivePercentDiscount(ticket);
		// Then
		assertEquals(Precision.round(fare, 2), ticket.getPrice());
	}
}
