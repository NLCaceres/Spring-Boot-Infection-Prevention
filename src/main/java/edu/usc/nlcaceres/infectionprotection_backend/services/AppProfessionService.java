package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppProfessionService implements ProfessionService {

    @Autowired
    ProfessionRepository professionRepository;

    @Override
    public List<Profession> getAll() {
        return professionRepository.findAll();
    }

    @Override
    public Profession getById(String id) {
        return professionRepository.findById(id).orElseThrow();
    }

    @Override
    public String save(Profession profession) {
        return professionRepository.save(profession).getId();
    }

    @Override
    public void delete(String id) {
        professionRepository.deleteById(id);
    }
}
