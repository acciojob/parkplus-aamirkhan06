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
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception
    {
        Optional<ParkingLot> parkingLotOpt = parkingLotRepository3.findById(parkingLotId);
        if(!parkingLotOpt.isPresent()){
            //throw new Exception("Cannot make reservation");
            return null;
        }
        ParkingLot parkingLot = parkingLotOpt.get();

        Optional<User> userOpt = userRepository3.findById(userId);
        if(!userOpt.isPresent()){
//            throw new Exception("Cannot make reservation");
            return null;
        }
        User user = userOpt.get();
        Spot requiredSpot=null;
        int finalHourPrice=Integer.MAX_VALUE;
        List<Spot> spotList=parkingLot.getSpotList();
        for(Spot spot:spotList){
            if(!spot.getOccupied()){
                if(numberOfWheels<=2 && finalHourPrice>spot.getPricePerHour()){
                    finalHourPrice=spot.getPricePerHour();
                    requiredSpot=spot;
                }
                else if(numberOfWheels<=4 && finalHourPrice>spot.getPricePerHour()){
                    finalHourPrice=spot.getPricePerHour();
                    requiredSpot=spot;
                }
                else{
                    finalHourPrice=spot.getPricePerHour();
                    requiredSpot=spot;
                }
            }
        }
        if(requiredSpot==null){
            //throw new Exception("Cannot make reservation");
            return null;
        }

        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(requiredSpot);
        reservation.setUser(user);

        List<Reservation> userReservations=user.getReservationList();
        List<Reservation> spotReservations=requiredSpot.getReservationList();

        userReservations.add(reservation);
        spotReservations.add(reservation);
        requiredSpot.setOccupied(true);

        user.setReservationList(userReservations);
        requiredSpot.setReservationList(spotReservations);

        userRepository3.save(user);
        spotRepository3.save(requiredSpot);

        return reservation;
    }
}
