package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class PrecautionGraphController {
    @Autowired
    PrecautionService precautionService;

    @QueryMapping
    public List<Precaution> precautions() {
        return precautionService.getAll();
    }
    @QueryMapping
    public Precaution precautionById(@Argument String id) {
        try { return precautionService.getById(id); }
        catch (Exception error) { return null; }
    }
}
