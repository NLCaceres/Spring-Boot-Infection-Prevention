package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usc.nlcaceres.infectionprotection_backend.models.JsonViews;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController @RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping @JsonView(JsonViews.Public.class)
    public List<Report> getAll() {
        return reportService.getAll();
    }
    @GetMapping("/{id}") @JsonView(JsonViews.Public.class)
    public ResponseEntity<Report> getById(@PathVariable String id) {
        try {
            Report report = reportService.getById(id);
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
