package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        Reservation reservation = new Reservation();

        User user;
        try {
            user = userRepository3.findById(userId).get();
        } catch (Exception e) {
            reservation.setSpot(null);
            reservationRepository3.save(reservation);
            throw new Exception("Cannot make reservation");
        }

        ParkingLot parkingLot;
        try {
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        } catch (Exception e) {
            reservation.setSpot(null);
            reservationRepository3.save(reservation);
            throw new Exception("Cannot make reservation");
        }

        reservation.setUser(user);

        List<Spot> spotList = parkingLot.getSpotList();

        int minimumPrice = Integer.MAX_VALUE;
        Spot spotAvail = null;
        for (Spot spot : spotList) {
            if (spot.getOccupied() == false) {
                if (getNumberOfWheels(spot.getSpotType()) >= numberOfWheels && spot.getPricePerHour() * timeInHours < minimumPrice) {
                    minimumPrice = spot.getPricePerHour() * timeInHours;
                    spotAvail = spot;

                }
            }
        }

        if(spotAvail == null)
        {
            reservation.setSpot(null);
            reservationRepository3.save(reservation);
            throw new Exception("Cannot make reservation");
        }

        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spotAvail);

        user.getReservationList().add(reservation);

        spotAvail.getReservationList().add(reservation);
        spotAvail.setOccupied(true);

        userRepository3.save(user);
        spotRepository3.save(spotAvail);


        return reservation;
    }
    public int getNumberOfWheels(SpotType spotType)
    {
        if(spotType.equals(SpotType.TWO_WHEELER))
            return 2;
        else if(spotType.equals(SpotType.FOUR_WHEELER))
            return 4;
        return Integer.MAX_VALUE;
    }
}
