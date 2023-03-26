package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import java.util.List;

public interface ReportService {

    List<Report> getAll();
    Report getById(String id);
    String save(Report report);
    void delete(String id);

}
