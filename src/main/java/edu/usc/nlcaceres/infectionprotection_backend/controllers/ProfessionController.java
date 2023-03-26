package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.services.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController @RequestMapping("/api/professions")
public class ProfessionController {

    @Autowired
    private ProfessionService professionService;

    @GetMapping
    public List<Profession> getAll() {
        return professionService.getAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Profession> getById(@PathVariable String id) {
        try { return new ResponseEntity<>(professionService.getById(id), HttpStatus.OK); }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
