package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
}
