package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import java.util.List;

public interface PrecautionService {

    List<Precaution> getAll();
    Precaution getById(String id);
    String save(Precaution precaution);
    void delete(String id);

}
