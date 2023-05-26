package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
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

    @GetMapping
    public List<HealthPractice> getAll() {
        List<HealthPractice> healthPracticeList = healthPracticeService.getAll();
        // Since parallelStreams can be slow for short lists, it may be best to use them only if (N > 100 or even 1000)
        // An alternative may be Spring's Paging system to keep lists short so no parallel streams needed
        healthPracticeList.parallelStream().forEach(HealthPractice::removeBackReference);
        return healthPracticeList;
    }
    @GetMapping("/{id}")
    public ResponseEntity<HealthPractice> getById(@PathVariable String id) {
        try {
            HealthPractice healthPractice = healthPracticeService.getById(id);
            healthPractice.removeBackReference();
            return new ResponseEntity<>(healthPractice, HttpStatus.OK);
        }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
