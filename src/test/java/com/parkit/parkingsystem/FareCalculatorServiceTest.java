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
	void calculateFareBikeWithNullOutTime() {

		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	// 45 minutes of Bike parking time should give 3/4th parking fare
	void calculateFareBikeWithLessThanOneHourParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);

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
	// 45 minutes of car parking time should give 3/4th parking fare
	void calculateFareCarWithLessThanOneHourParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
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
	// 24 hours parking time should give 24 *parking fare per hour
	void calculateFareCarWithMoreThanADayParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(24);
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
	// 20 minutes parking time for car should be free( give 0)
	void calculateFareCarWithLessThan30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);

		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	// 20 minutes parking time for bike should be free( give 0)
	void calculateFareBikeWithLessThan30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);

		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	// 30 minutes of Bike parking time should be free( give 0)
	void calculateFareBikeWith30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0), ticket.getPrice());
	}
	@Test
	// 20 minutes of CAR parking time should be free( give 0)
	void calculateFareCarWith30Minutes() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
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
	@Test
	void getTheDurationToBePaidTest_forMoreThen30Minutes() {
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusHours(1);
		double duration = fareCalculatorService.getTheDurationToBePaid(inTime,
				outTime);
		assertThat(duration).isEqualTo(60);
	}
	@Test
	void getTheDurationToBePaidTest_forLessThen30Minutes() {
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(15);
		double duration = fareCalculatorService.getTheDurationToBePaid(inTime,
				outTime);
		assertThat(duration).isZero();
	}
	@Test
	void getTheDurationToBePaidTest_for30Minutes() {
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(30);
		double duration = fareCalculatorService.getTheDurationToBePaid(inTime,
				outTime);
		assertThat(duration).isZero();
	}
	@Test
	void fivePercentDiscountTest_for30Minutes() {
		ticket.setInTime(LocalDateTime.now());
		ticket.setOutTime(LocalDateTime.now().plusMinutes(30));
		ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
		ticket.setVehicleRegNumber("ABCD");
		fareCalculatorService.fivePercentDiscount(ticket);

		assertThat(ticket.getPrice()).isEqualTo(0);

	}
	@Test
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

}
