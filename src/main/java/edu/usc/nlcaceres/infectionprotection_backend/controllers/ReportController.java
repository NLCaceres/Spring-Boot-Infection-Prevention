package edu.usc.nlcaceres.infectionprotection_backend.controllers;

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

    @GetMapping
    public List<Report> getAll() {
        List<Report> reportList = reportService.getAll();
        reportList.parallelStream().forEach(report -> report.getHealthPractice().removeBackReference());
        return reportList;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable String id) {
        try {
            Report report = reportService.getById(id);
            report.getHealthPractice().removeBackReference();
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
