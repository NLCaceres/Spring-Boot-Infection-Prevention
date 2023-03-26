package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.HealthPracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppHealthPracticeService implements HealthPracticeService {

    @Autowired
    HealthPracticeRepository healthPracticeRepository;

    @Override
    public List<HealthPractice> getAll() {
        return healthPracticeRepository.findAll();
    }

    @Override
    public HealthPractice getById(String id) {
        return healthPracticeRepository.findById(id).orElseThrow();
    }

    @Override
    public String save(HealthPractice healthPractice) {
        return healthPracticeRepository.save(healthPractice).getId();
    }

    @Override
    public void delete(String id) {
        healthPracticeRepository.deleteById(id);
    }
}
