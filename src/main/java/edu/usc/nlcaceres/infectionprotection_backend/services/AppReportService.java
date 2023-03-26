package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppReportService implements ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Override
    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    @Override
    public Report getById(String id) {
        return reportRepository.findById(id).orElseThrow();
    }

    @Override
    public String save(Report report) {
        return reportRepository.save(report).getId();
    }

    @Override
    public void delete(String id) {
        reportRepository.deleteById(id);
    }
}
