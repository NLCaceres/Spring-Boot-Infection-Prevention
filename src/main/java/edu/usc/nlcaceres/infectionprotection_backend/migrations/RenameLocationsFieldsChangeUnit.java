package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.query.Update;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@ChangeUnit(id="rename-locations-fields", order="4")
public class RenameLocationsFieldsChangeUnit {
  @Execution
  public void execution(MongoTemplate mongoTemplate) {
    // ?: Bulk Mode (vs the typical batch insert) ensures documents are atomically processed
    mongoTemplate.bulkOps(BulkMode.ORDERED, Location.class)
      .updateMulti(
        query(new Criteria().orOperator( // ?: A BasicQuery takes a String version of this query
          where("facilityName").exists(true), where("unitNum").exists(true), where("roomNum").exists(true)
        )),
        new Update().rename("facilityName", "facility_name")
          .rename("unitNum", "unit_num").rename("roomNum", "room_num")
      );
  }
  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.bulkOps(BulkMode.ORDERED, Location.class)
      .updateMulti(
        query(new Criteria().orOperator(
          where("facility_name").exists(true), where("unit_num").exists(true), where("room_num").exists(true)
        )),
        new Update().rename("facility_name", "facilityName")
          .rename("unit_num", "unitNum").rename("room_num", "roomNum")
      );
  }
}
