package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.math3.util.Precision;

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
		double fare = 0;
		switch (ticket.getParkingSpot().getParkingType()) {
			case CAR : {
				fare = Precision.round((duration / 60) * Fare.CAR_RATE_PER_HOUR,
						2);
				break;
			}
			case BIKE : {
				fare = Precision
						.round((duration / 60) * Fare.BIKE_RATE_PER_HOUR, 2);
				break;
			}
			default :
				throw new IllegalArgumentException("Unkown Parking Type");
		}
		ticket.setPrice(fare);
	}

	private double getTheDurationToBePaid(LocalDateTime inTime,
			LocalDateTime outTime) {

		return ((Duration.between(inTime, outTime).toMinutes() <= 30)
				? 0
				: Duration.between(inTime, outTime).toMinutes());

	}

	public void fivePercentDiscount(Ticket ticket) {

		double discount = 0.05;
		double fare = ticket.getPrice() - (ticket.getPrice() * discount);

		ticket.setPrice(Precision.round(fare, 2));

	}
}