package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
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

    @GetMapping @JsonView(Precaution.PublicJsonView.class)
    public List<Precaution> getAll() {
        return precautionService.getAll();
    }
    @GetMapping("/{id}") @JsonView(Precaution.PublicJsonView.class)
    public ResponseEntity<Precaution> getById(@PathVariable String id) {
        try {
            Precaution precaution = precautionService.getById(id);
            return new ResponseEntity<>(precaution, HttpStatus.OK);
        }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
