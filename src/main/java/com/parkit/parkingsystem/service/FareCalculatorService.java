package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null)
				|| (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:"
					+ ticket.getOutTime().toString());
		}
		double duration = getTheDurationToBePaid(ticket.getInTime(),
				ticket.getOutTime());

		switch (ticket.getParkingSpot().getParkingType()) {
			case CAR : {
				ticket.setPrice((duration / 60) * (Fare.CAR_RATE_PER_HOUR));

				break;
			}
			case BIKE : {
				ticket.setPrice((duration / 60) * (Fare.BIKE_RATE_PER_HOUR));
				break;
			}
			default :
				throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private double getTheDurationToBePaid(LocalDateTime inTime,
			LocalDateTime outTime) {

		return ((Duration.between(inTime, outTime).toMinutes() <= 30)
				? 0
				: Duration.between(inTime, outTime).toMinutes());

	}
}