package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address)
    {
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot.setSpotList(new ArrayList<>());
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour)
    {
        //make the spot
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList=parkingLot.getSpotList();
        Spot spot=new Spot();

        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);
        spot.setReservationList(new ArrayList<>());
        if(numberOfWheels<=2)
        {
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels==4 || numberOfWheels==3)
        {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else
        {
            spot.setSpotType(SpotType.OTHERS);
        }
        //save in repo:-
        Spot savedSpot=spotRepository1.save(spot);
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);

        return savedSpot;
    }

    @Override
    public void deleteSpot(int spotId)
    {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour)
    {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=null;
        List<Spot> spotList=parkingLot.getSpotList();
        for(Spot profile: spotList){
            if(profile.getId()==spotId)
            {
                profile.setPricePerHour(pricePerHour);
                spot=spotRepository1.save(profile);
            }
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
