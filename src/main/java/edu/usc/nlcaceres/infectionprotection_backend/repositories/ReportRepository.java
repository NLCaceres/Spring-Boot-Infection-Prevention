package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
}
