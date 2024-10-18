package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.List;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.HealthPracticeRepository;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.PrecautionRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="create-health_practices", order="6")
public class CreateHealthPracticesChangeUnit {
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
    if (!mongoTemplate.collectionExists("health_practices")) {
      mongoTemplate.createCollection("health_practices");
    }
  }
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.dropCollection("health_practices");
  }

  @Execution
  public void execution(HealthPracticeRepository healthPracticeRepository, PrecautionRepository precautionRepository) {
    if (healthPracticeRepository.count() == 0) {
      Precaution standardPrecaution = precautionRepository.findByName("Standard");
      HealthPractice handHygiene = HealthPractice.of("Hand Hygiene", standardPrecaution);
      HealthPractice ppe = HealthPractice.of("PPE", standardPrecaution);

      Precaution isolationPrecaution = precautionRepository.findByName("Isolation");
      HealthPractice contact = HealthPractice.of("Contact", isolationPrecaution);
      HealthPractice contactEnteric = HealthPractice.of("Contact Enteric", isolationPrecaution);
      HealthPractice airborne = HealthPractice.of("Airborne", isolationPrecaution);
      HealthPractice droplet = HealthPractice.of("Droplet", isolationPrecaution);

      healthPracticeRepository.saveAll(List.of(handHygiene, ppe, contact, contactEnteric, airborne, droplet));
    }
  }
  @RollbackExecution
  public void rollbackExecution(HealthPracticeRepository healthPracticeRepository) {
    healthPracticeRepository.deleteAll();
  }
}
