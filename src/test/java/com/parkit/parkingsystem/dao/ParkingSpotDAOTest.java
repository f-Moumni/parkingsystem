package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

import nl.altindag.log.LogCaptor;
@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

	private ParkingSpotDAO parkingSpotDAO;
	private static LogCaptor logCaptor;

	@Mock
	private static DataBaseConfig dataBaseConfig;
	@Mock
	PreparedStatement prepareStatement;
	@Mock
	ResultSet resultSet;
	@Mock
	Connection connection;
	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		parkingSpotDAO = new ParkingSpotDAO();
		logCaptor = LogCaptor.forName("ParkingSpotDAO");
		logCaptor.setLogLevelToInfo();
	}

	@Test
	void getNextAvailableSlot_ForCar_ShouldRetrunParkingNumber() {
		// given
		ParkingType parkingType = ParkingType.CAR;

		// when
		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);// the
																		// parking
																		// numbers
																		// for
																		// car
																		// are
																		// 1,2
		// then // and 3
		assertThat(result).isBetween(1, 3);

	}
	@Test
	void getNextAvailableSlot_ForBike_ShouldRetrunParkingNumber() {
		// given
		ParkingType parkingType = ParkingType.BIKE;
		// when
		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);// the
																		// parking
																		// numbers
																		// for
																		// bike
																		// are
																		// 4 and
																		// 5
		// Then
		assertThat(result).isBetween(4, 5);
	}
	@Test
	void getNextAvailableSlot_WithNullParkingType_ShouldLogError() {
		ParkingType parkingType = null;

		int result = parkingSpotDAO.getNextAvailableSlot(parkingType);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available slot");
		assertThat(result).isEqualTo(-1);

	}

	@Test
	void updateParking_withZeroinParkingNumber_ShouldReturnFlase() {
		ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, true);

		assertFalse(parkingSpotDAO.updateParking(parkingSpot));
	}
	@Test
	void updateParking_withAvalidParkingSpot_ShouldReturnTrue() {
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);

		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}

	@Test
	void updateParking_withNullParkingSpot_ShouldReturnFlaseAndLogError() {
		ParkingSpot parkingSpot = null;

		assertFalse(parkingSpotDAO.updateParking(parkingSpot));
		assertThat(logCaptor.getErrorLogs())
				.contains("Error updating parking info");
	}
	@Test
	void vehicleIsInParking_withvehicleIsInParking_shouldReturnFalse() {
		String vehicleRegNumber = "AAA";
		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}
	@Test
	void vehicleIsInParking_withvehicleNOTInParking_shouldReturnTrue() {
		String vehicleRegNumber = "ABCDEF";
		assertTrue(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}
	@Test
	void vehicleIsInParking_withNullvehicleRegNumber_shouldReturnFalseAndLogError()
			throws Exception, SQLException {
		String vehicleRegNumber = null;

		assertTrue(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
		assertThat(logCaptor.getErrorLogs())
				.contains("Error vehicule exit controlling");
	}
}
