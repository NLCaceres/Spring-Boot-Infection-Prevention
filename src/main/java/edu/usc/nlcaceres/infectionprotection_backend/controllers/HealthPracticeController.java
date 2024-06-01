package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.models.JsonViews;
import edu.usc.nlcaceres.infectionprotection_backend.services.HealthPracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController @RequestMapping("/api/healthPractices")
public class HealthPracticeController {

    @Autowired
    private HealthPracticeService healthPracticeService;

    @GetMapping @JsonView(JsonViews.Public.class)
    public List<HealthPractice> getAll() {
        return healthPracticeService.getAll();
    }
    @GetMapping("/{id}") @JsonView(JsonViews.Public.class)
    public ResponseEntity<HealthPractice> getById(@PathVariable String id) {
        try {
            HealthPractice healthPractice = healthPracticeService.getById(id);
            return new ResponseEntity<>(healthPractice, HttpStatus.OK);
        }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
