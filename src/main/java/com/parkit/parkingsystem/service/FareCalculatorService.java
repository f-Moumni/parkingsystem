package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.math3.util.Precision;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
/**
 * FareCalculatorService allow to calculate the price to paid
 * 
 * @author Tek ,Fatima
 *
 */
public class FareCalculatorService {
	/**
	 * calculate the price to pay for a ticket according to the entry and exit
	 * dates
	 * 
	 * @param ticket
	 *            ticket for which the calculation will be done.
	 * @throws IllegalArgumentException
	 *             if unknown parking type.
	 */
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null)
				|| (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:"
					+ ticket.getOutTime().toString());
		}
		double duration = getTheDurationToBePaid(ticket.getInTime(),
				ticket.getOutTime());
		double fare = 0;
		int minuteInHour = 60;
		switch (ticket.getParkingSpot().getParkingType()) {
			case CAR : {
				fare = Precision.round(
						(duration / minuteInHour) * Fare.CAR_RATE_PER_HOUR, 2);
				break;
			}
			case BIKE : {
				fare = Precision.round(
						(duration / minuteInHour) * Fare.BIKE_RATE_PER_HOUR, 2);
				break;
			}
			default :
				throw new IllegalArgumentException("Unkown Parking Type");
		}
		ticket.setPrice(fare);
	}
	/**
	 * get the duration time to be paid in minutes
	 * 
	 * @param inTime
	 *            entry date time
	 * @param outTime
	 *            exit date time
	 * @return duration to be paid in minutes
	 */
	public double getTheDurationToBePaid(LocalDateTime inTime,
			LocalDateTime outTime) {

		return ((Duration.between(inTime, outTime).toMinutes() <= 30)
				? 0
				: Duration.between(inTime, outTime).toMinutes());

	}
	/**
	 * calculate the 5% discount for a given ticket
	 * 
	 * @param ticket
	 *            ticket that will have the discount
	 */
	public void fivePercentDiscount(Ticket ticket) {

		double discount = 0.05;
		double fare = ticket.getPrice() - (ticket.getPrice() * discount);

		ticket.setPrice(Precision.round(fare, 2));

	}
}