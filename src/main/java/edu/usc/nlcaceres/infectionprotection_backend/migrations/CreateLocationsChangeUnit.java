package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.List;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.LocationRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="create-locations", order="3")
public class CreateLocationsChangeUnit {
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
    if (!mongoTemplate.collectionExists("locations")) {
      mongoTemplate.createCollection("locations");
    }
  }
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.dropCollection("locations");
  }

  @Execution
  public void execution(LocationRepository locationRepository) {
    if (locationRepository.count() == 0) {
      String usc = "USC";
      String hsc = "HSC";
      locationRepository.saveAll(List.of(
        Location.of(usc, "2", "213"),
        Location.of(usc, "4", "202"),
        Location.of(hsc, "3", "213"),
        Location.of(hsc, "3", "321"),
        Location.of(hsc, "5", "121")
      ));
    }
  }
  @RollbackExecution
  public void rollbackExecution(LocationRepository locationRepository) {
    locationRepository.deleteAll();
  }
}
