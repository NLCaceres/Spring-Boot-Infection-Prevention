package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Update;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="rename-reports-health_practices-field", order="8")
public class RenameReportsHealthPracticeFieldChangeUnit {
  @Execution
  public void execution(MongoTemplate mongoTemplate) {
    // ?: `updateMulti` is the probably less used alternative to Spring's fluent MongoTemplate API
    mongoTemplate.updateMulti( // ?: like `update().matching().apply().all()`... see `RenameLocations`
      query(where("healthPractice").exists(true)),
      new Update().rename("healthPractice", "health_practice"), Report.class
    );
  }
  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.updateMulti(
      query(where("health_practice").exists(true)),
      new Update().rename("health_practice", "healthPractice"), Report.class
    );
  }
}
