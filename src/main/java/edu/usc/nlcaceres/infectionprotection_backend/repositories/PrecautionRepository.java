package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecautionRepository extends MongoRepository<Precaution, String> {
}
