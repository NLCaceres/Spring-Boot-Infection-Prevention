package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.PrecautionRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="create-precautions", order="5")
public class CreatePrecautionsChangeUnit {
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
    if(!mongoTemplate.collectionExists("precautions")) {
      mongoTemplate.createCollection("precautions");
    }
  }
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.dropCollection("precautions");
  }

  @Execution
  public void execution(PrecautionRepository precautionRepository) {
    if (precautionRepository.count() == 0) {
      Precaution standardPrecaution = Precaution.of("Standard");
      Precaution isolationPrecaution = Precaution.of("Isolation");
      precautionRepository.saveAll(List.of(standardPrecaution, isolationPrecaution));
    }
  }
  @RollbackExecution
  public void rollbackExecution(PrecautionRepository precautionRepository) {
    precautionRepository.deleteAll();
  }
}
