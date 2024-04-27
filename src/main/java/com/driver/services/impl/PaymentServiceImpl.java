package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception
    {
        Optional<Reservation> reservationOpt = reservationRepository2.findById(reservationId);
        if(!reservationOpt.isPresent()){
            return null;
        }
        Reservation reservation = reservationOpt.get();
        Spot spot = reservation.getSpot();

        Payment payment = reservation.getPayment();
        if(payment == null){
            payment = new Payment(); // Initialize payment if it's null
        }
        int bill = reservation.getNumberOfHours() * spot.getPricePerHour();

        String modeType=mode.toUpperCase();
        if(modeType.equals("CASH")){
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else if(modeType.equals("CARD")) {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else if(modeType.equals("UPI")){
            payment.setPaymentMode(PaymentMode.UPI);
        }
        else{
            throw new Exception("Payment mode not detected");
        }

        if(amountSent<bill){
            throw new Exception("Insufficient Amount");
        }
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        Payment savedPayment=paymentRepository2.save(payment);
        spot.setOccupied(false);
        reservation.setPayment(savedPayment);
        reservationRepository2.save(reservation);
        return savedPayment;
    }
}
