package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import java.util.List;

public interface LocationService {

    List<Location> getAll();
    Location getById(String id);
    String save(Location location);
    void delete(String id);

}
