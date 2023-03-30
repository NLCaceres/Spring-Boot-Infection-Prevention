package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ReportGraphController {
    @Autowired
    ReportService reportService;

    @QueryMapping
    public List<Report> reports() {
        return reportService.getAll();
    }
    @QueryMapping
    public Report reportById(@Argument String id) {
        try { return reportService.getById(id); }
        catch (Exception error) { return null; }
    }
}
