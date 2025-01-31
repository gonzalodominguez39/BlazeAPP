package com.emma.Blaze.service;

import com.emma.Blaze.model.Location;
import com.emma.Blaze.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private  LocationRepository locationRepository;

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }


}
