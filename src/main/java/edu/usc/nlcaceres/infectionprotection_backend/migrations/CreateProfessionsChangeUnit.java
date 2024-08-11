package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ProfessionRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="create-professions", order="1") // ?: Author gets set to default
public class CreateProfessionsChangeUnit {
  // ?: ChangeUnits run these methods in the following order:
  // ?: 1. @BeforeExecution 2. @Execution
  // ?: 3. @RollbackBeforeExecution 4. @RollbackExecution
  // ?: BUT the rollbacks ONLY occur if you manually set them off via your MongoDB provider
  // ?: OR if you have professional Mongock access to set them off via the CLI
  @BeforeExecution // ?: Useful to run Data Definition Language (DDL) operations like creating a table or index
  public void beforeExecution(MongoTemplate mongoTemplate) {
    if (!mongoTemplate.collectionExists("professions")) {
      mongoTemplate.createCollection("professions");
    }
  }
  @RollbackBeforeExecution // ?: Useful to undo the `@BeforeExecution` (Mandatory if you use @BeforeExecution)
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.dropCollection("professions");
  }

  @Execution // ?: Run the main table changes, like column additions, removal, updates, etc
  public void execution(ProfessionRepository professionRepository) {
    if (professionRepository.count() == 0) {
      professionRepository.saveAll(List.of(
        Profession.of("Clinic", "Doctor"),
        Profession.of("Clinic", "Nurse")
      ));
    }
  }
  @RollbackExecution // ?: Undo the changes of `@Execution`
  public void rollbackExecution(ProfessionRepository professionRepository) {
    professionRepository.deleteAll();
  }
}
