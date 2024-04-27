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
import java.util.Optional;

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
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour)
    {
        //make the spot
        ParkingLot newParkingLot;

        newParkingLot = parkingLotRepository1.findById(parkingLotId).get();


        Spot newSpot = new Spot();
        SpotType st;
        if(numberOfWheels<=2){
            st=SpotType.TWO_WHEELER;
        }

        else if (numberOfWheels==4 || numberOfWheels==3) {
            st=SpotType.FOUR_WHEELER;

        }
        else st=SpotType.OTHERS;

        newSpot.setSpotType(st);
        newSpot.setPricePerHour(pricePerHour);

        newParkingLot.getSpotList().add(newSpot);

        ParkingLot savedParkingLot=parkingLotRepository1.save(newParkingLot);

        newSpot.setParkingLot(savedParkingLot);

//        spotRepository1.save(newSpot);

        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId)
    {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour)
    {
        Spot spot = null;
        ParkingLot parkingLot =parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        for(Spot spot1 : spotList)
        {
            if(spot1.getId()==spotId)
            {
                spot1.setPricePerHour(pricePerHour);
                spotRepository1.save(spot1);
                spot =spot1;
                break;
            }
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
