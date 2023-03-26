package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.PrecautionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppPrecautionService implements PrecautionService {

    @Autowired
    PrecautionRepository precautionRepository;

    @Override
    public List<Precaution> getAll() {
        return precautionRepository.findAll();
    }

    @Override
    public Precaution getById(String id) {
        return precautionRepository.findById(id).orElseThrow();
    }

    @Override
    public String save(Precaution precaution) {
        return precautionRepository.save(precaution).getId();
    }

    @Override
    public void delete(String id) {
        precautionRepository.deleteById(id);
    }
}
