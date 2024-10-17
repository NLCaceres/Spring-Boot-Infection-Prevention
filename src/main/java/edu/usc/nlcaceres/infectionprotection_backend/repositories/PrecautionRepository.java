package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecautionRepository extends MongoRepository<Precaution, String> {
  // ?: Spring Boot Repository query keywords are super powerful and there's a lot available
  // ?: Helpful Link: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html

  // ?: "DistinctBy" runs a Mongo "distinct" query, getting EVERY distinct value for a given field (if your Mongo store supports "distinct")
  // ?: BUT returning type T actually ensures uniqueness by default BUT CAN return null AND THROWS if multiple document match
  // ?: BUT adding the "First" keyword prevents the Exception from being thrown if desired
  // ?: "findBy" can even be replaced by a LOT of different options like "getBy" or "findPrecautionBy" or "searchBy"
  Precaution findByName(String name); // - BUT this is probably the simplest to grab a Precaution by a specific name
}
