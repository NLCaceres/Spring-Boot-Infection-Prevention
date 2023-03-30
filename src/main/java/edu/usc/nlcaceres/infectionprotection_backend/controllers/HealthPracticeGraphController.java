package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.services.HealthPracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class HealthPracticeGraphController {
    @Autowired
    HealthPracticeService healthPracticeService;

    @QueryMapping
    public List<HealthPractice> healthPractices() {
        return healthPracticeService.getAll();
    }
    @QueryMapping
    public HealthPractice healthPracticeById(@Argument String id) {
        try { return healthPracticeService.getById(id); }
        catch (Exception error) { return null; }
    }
}
