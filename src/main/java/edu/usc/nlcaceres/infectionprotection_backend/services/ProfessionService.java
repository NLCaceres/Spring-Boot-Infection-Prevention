package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import java.util.List;

public interface ProfessionService {

    List<Profession> getAll();
    Profession getById(String id);
    String save(Profession profession);
    void delete(String id);

}
