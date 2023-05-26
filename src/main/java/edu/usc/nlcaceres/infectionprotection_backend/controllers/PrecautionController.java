package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController @RequestMapping("/api/precautions")
public class PrecautionController {

    @Autowired
    private PrecautionService precautionService;

    @GetMapping
    public List<Precaution> getAll() {
        List<Precaution> precautionList = precautionService.getAll();
        precautionList.parallelStream().forEach(Precaution::removeBackReference);
        return precautionList;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Precaution> getById(@PathVariable String id) {
        try {
            Precaution precaution = precautionService.getById(id);
            precaution.removeBackReference();
            return new ResponseEntity<>(precaution, HttpStatus.OK);
        }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
