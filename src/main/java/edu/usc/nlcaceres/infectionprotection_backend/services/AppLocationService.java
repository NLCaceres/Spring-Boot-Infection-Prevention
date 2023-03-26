package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppLocationService implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location getById(String id) {
        return locationRepository.findById(id).orElseThrow();
    }

    @Override
    public String save(Location location) {
        return locationRepository.save(location).getId();
    }

    @Override
    public void delete(String id) {
        locationRepository.deleteById(id);
    }
}
