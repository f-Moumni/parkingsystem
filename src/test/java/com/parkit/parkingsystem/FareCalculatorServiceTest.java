package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	@DisplayName("For a car parked one hour, the price should be equal to the car rate per hour.")
	void calculateFareCar() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertThat(ticket.getPrice())
				.isEqualTo(Precision.round(Fare.CAR_RATE_PER_HOUR, 2));

	}

	@Test
	@DisplayName("For a Bike parked one hour, the price should be equal to the bike rate per hour.")
	void calculateFareBike() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertThat(ticket.getPrice())
				.isEqualTo(Precision.round(Fare.BIKE_RATE_PER_HOUR, 2));
	}

	@Test
	@DisplayName("For a Unkown Type, shouled throw NullPointerException.")
	void calculateFareUnkownType() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(2);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// Then
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("For a Bike, with future in time,shouled throw IllegalArgumentException")
	void calculateFareBikeWithFutureInTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// Then
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}
	@Test
	@DisplayName("For a Bike, With Null OutTime, shouled throw NullPointerException")
	void calculateFareBikeWithNullOutTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// Then
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("For a Bike, With 45 minutes of Bike parking time should give 3/4th parking fare")
	void calculateFareBikeWithLessThanOneHourParkingTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals(Precision.round(0.75 * Fare.BIKE_RATE_PER_HOUR, 2),
				ticket.getPrice());
	}

	@Test
	@DisplayName("For a Car, With 45 minutes of car parking time should give 3/4th parking fare")
	void calculateFareCarWithLessThanOneHourParkingTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals(Precision.round(0.75 * Fare.CAR_RATE_PER_HOUR, 2),
				ticket.getPrice());
	}

	@Test
	@DisplayName("For a Car, With 24 hours parking time should give 24 *parking fare per hour")
	void calculateFareCarWithMoreThanADayParkingTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(24);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals(Precision.round(24 * Fare.CAR_RATE_PER_HOUR, 2),
				ticket.getPrice());
	}

	@Test
	@DisplayName("For a Car, With 20 minutes parking time for car should be free( give 0)")
	void calculateFareCarWithLessThan30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals((0), ticket.getPrice());
	}
	@Test
	@DisplayName("For a bike,with 20 minutes parking time for bike should be free( give 0)")
	void calculateFareBikeWithLessThan30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals((0), ticket.getPrice());
	}
	@Test
	@DisplayName("For a Bike,with 30 minutes of Bike parking time should be free( give 0)")
	void calculateFareBikeWith30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals((0), ticket.getPrice());
	}
	@Test
	@DisplayName("For a car,with 30 minutes of car parking time should be free( give 0)")
	void calculateFareCarWith30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals((0), ticket.getPrice());
	}

	@Test
	@DisplayName("discount For a car,with on hour of car parking time should give far with 5% discount")
	void fivePercentDiscount_ForOneHour_OfCarParking() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
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
	@DisplayName("get the duration to be paid for one hour, should give 60 minutes")
	void getTheDurationToBePaidTest_forMoreThen30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusHours(1);
		// When
		double duration = fareCalculatorService.getTheDurationToBePaid(inTime,
				outTime);
		// Then
		assertThat(duration).isEqualTo(60);
	}
	@Test
	@DisplayName("get the duration to be paid for 15 minutes, should give 0")
	void getTheDurationToBePaidTest_forLessThen30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(15);
		// When
		double duration = fareCalculatorService.getTheDurationToBePaid(inTime,
				outTime);
		// Then
		assertThat(duration).isZero();
	}
	@Test
	@DisplayName("get the duration to be paid for 30 minutes, should give 0")
	void getTheDurationToBePaidTest_for30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(30);
		// When
		double duration = fareCalculatorService.getTheDurationToBePaid(inTime,
				outTime);
		// Then
		assertThat(duration).isZero();
	}
	@Test
	@DisplayName("get the duration to be paid for 30 minutes, should give 0")
	void fivePercentDiscountTest_for30Minutes() {
		ticket.setInTime(LocalDateTime.now());
		ticket.setOutTime(LocalDateTime.now().plusMinutes(30));
		ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
		ticket.setVehicleRegNumber("ABCD");
		fareCalculatorService.fivePercentDiscount(ticket);

		assertThat(ticket.getPrice()).isEqualTo(0);

	}
	@Test
	@DisplayName("discount For a bike,with on hour of bike parking time should give far with 5% discount")
	void fivePercentDiscountTest_forMoreThen30Minutes() {
		ticket.setInTime(LocalDateTime.now());
		ticket.setOutTime(LocalDateTime.now().plusHours(1));
		ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
		ticket.setVehicleRegNumber("ABCD");
		ticket.setPrice(Precision.round(Fare.BIKE_RATE_PER_HOUR, 2));

		double reducedPrice = ticket.getPrice() - (ticket.getPrice() * 0.05);
		fareCalculatorService.fivePercentDiscount(ticket);

		assertThat(ticket.getPrice()).isEqualTo(reducedPrice);

	}
	@Test
	@DisplayName("discount For a bike,with 20minnutes of bike parking time should give 0")
	void fivePercentDiscount_ForLessThen30minutes_OfBikeParking() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCD");
		// When
		fareCalculatorService.fivePercentDiscount(ticket);
		// Then
		assertThat(ticket.getPrice()).isEqualTo(0);
	}

}
