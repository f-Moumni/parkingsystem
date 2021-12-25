package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

	private ParkingSpotDAO parkingSpotDAO;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		parkingSpotDAO = new ParkingSpotDAO();

	}

	@Test
	void getNextAvailableSlot_ForCar_ShouldRetrunParkingNumber() {
		ParkingType parkingType = ParkingType.CAR;

		// when
		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);// the
																		// parking
																		// numbers
																		// for
																		// car
																		// are
																		// 1,2
																		// and 3
		assertThat(result).isBetween(1, 3);

	}
	@Test
	void getNextAvailableSlot_ForBike_ShouldRetrunParkingNumber() {
		ParkingType parkingType = ParkingType.BIKE;
		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);// the
		// parking
		// numbers for bike
		// are
		// 4 and 5
		assertThat(result).isBetween(4, 5);
	}
	// @Test
	void getNextAvailableSlot_ForNullParkingType_ShouldthrowNullPointerException() {

		assertThrows(NullPointerException.class,
				() -> parkingSpotDAO.getNextAvailableSlot(null));
	}

	// @Test
	void updateParking__ShouldthrowException() {
		ParkingSpot parkingSpot = new ParkingSpot(1, null, true);

		assertThrows(Exception.class,
				() -> parkingSpotDAO.updateParking(parkingSpot));
	}
	@Test
	void updateParking_withZeroinParkingNumber_ShouldReturnFlase() {
		ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, true);

		assertFalse(parkingSpotDAO.updateParking(parkingSpot));
	}
	@Test
	void updateParking_withAvalidParkingSpot_ParkingSpot() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);

		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}

	@Test
	void vehicleIsInParking() {
		String vehicleRegNumber = "AAA";
		assertTrue(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}
}
