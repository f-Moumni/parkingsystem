package com.parkit.parkingsystem.service;

import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null)
				|| (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:"
					+ ticket.getOutTime().toString());
		}
		double duration = getTheDurationToBePaid(ticket.getInTime(),
				ticket.getOutTime());

		switch (ticket.getParkingSpot().getParkingType()) {
			case CAR : {
				ticket.setPrice(
						Math.round((duration * Fare.CAR_RATE_PER_HOUR) * 100.0)
								/ 100.0);
				break;
			}
			case BIKE : {
				ticket.setPrice(
						Math.round((duration * Fare.BIKE_RATE_PER_HOUR) * 100.0)
								/ 100.0);
				break;
			}
			default :
				throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private double getTheDurationToBePaid(Date inTime, Date outTime) {
		double duration = ((outTime.getTime() - inTime.getTime())
				/ (1000 * 60));
		if (duration <= 30) {
			duration = 0;
		}

		return duration / 60;

	}
}