package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.services.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ProfessionGraphController {
    @Autowired
    ProfessionService professionService;

    @QueryMapping
    public List<Profession> professions() {
        return professionService.getAll();
    }
    @QueryMapping
    public Profession professionById(@Argument String id) {
        try { return professionService.getById(id); }
        catch (Exception error) { return null; }
    }
}
