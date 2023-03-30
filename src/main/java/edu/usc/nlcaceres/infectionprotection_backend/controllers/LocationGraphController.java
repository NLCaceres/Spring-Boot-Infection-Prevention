package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class LocationGraphController {
    @Autowired
    LocationService locationService;

    @QueryMapping
    public List<Location> locations() {
        return locationService.getAll();
    }
    @QueryMapping
    public Location locationById(@Argument String id) {
        try { return locationService.getById(id); }
        catch (Exception error) { return null; }
    }
}
