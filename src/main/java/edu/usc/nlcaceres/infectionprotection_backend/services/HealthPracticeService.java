package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import java.util.List;

public interface HealthPracticeService {

    List<HealthPractice> getAll();
    HealthPractice getById(String id);
    String save(HealthPractice healthPractice);
    void delete(String id);

}
