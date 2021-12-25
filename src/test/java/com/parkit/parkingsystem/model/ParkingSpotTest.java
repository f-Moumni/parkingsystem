package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {
	ParkingSpot parkingSpot;
	@BeforeEach
	private void setUpTest() {
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
	}

	@Test
	void getIdTest() {

		int result = parkingSpot.getId();
		assertThat(result).isEqualTo(1);
	}
	@Test
	void setIdTest() {
		parkingSpot.setId(2);
		assertThat(parkingSpot.getId()).isEqualTo(2);
	}
	@Test
	void getParkingTypeTest() {
		ParkingType result = parkingSpot.getParkingType();
		assertThat(result).isEqualTo(ParkingType.CAR);
	}
	@Test
	void setParkingTypeTest() {
		parkingSpot.setParkingType(ParkingType.BIKE);
		assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
	}
	@Test
	void isAvailableTest() {
		boolean result = parkingSpot.isAvailable();
		assertThat(result).isTrue();
	}
	@Test
	void setAvailableTest() {
		parkingSpot.setAvailable(false);
		assertThat(parkingSpot.isAvailable()).isFalse();
	}
	@Test
	void equalsTest_withAnEqualObject_shouldReturnTrue() {
		ParkingSpot secondParkingSpot = new ParkingSpot(1, ParkingType.CAR,
				true);
		boolean result = parkingSpot.equals(secondParkingSpot);
		assertThat(result).isTrue();
	}
	@Test
	void equalsTest_withAnUnEqualObject_shouldReturnFalse() {
		ParkingSpot secondParkingSpot = new ParkingSpot(4, ParkingType.BIKE,
				true);
		boolean result = parkingSpot.equals(secondParkingSpot);
		assertThat(result).isFalse();
	}
	@Test
	void equalsTest_withNullObject_shouldReturnFalse() {
		boolean result = parkingSpot.equals(null);
		assertThat(result).isFalse();
	}
	@Test
	void HashCodeTest() {
		int result = parkingSpot.hashCode();
		assertThat(result).isEqualTo(1);
	}
}
