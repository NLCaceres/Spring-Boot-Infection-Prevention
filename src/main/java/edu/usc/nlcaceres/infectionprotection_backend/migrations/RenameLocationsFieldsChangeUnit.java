package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.query.Update;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@ChangeUnit(id="rename-locations-fields", order="4")
public class RenameLocationsFieldsChangeUnit {
  @Execution
  public void execution(MongoTemplate mongoTemplate) {
      mongoTemplate.update(Location.class)
        .matching( // ?: Alternatively, a BasicQuery could take a String version of any query
          new Criteria().orOperator(
            where("facilityName").exists(true), where("unitNum").exists(true), where("roomNum").exists(true)
          )
        )
        .apply(
          new Update().rename("facilityName", "facility_name")
            .rename("unitNum", "unit_num").rename("roomNum", "room_num")
        ).all();
  }
  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
      mongoTemplate.update(Location.class)
        .matching(
          new Criteria().orOperator(
            where("facility_name").exists(true), where("unit_num").exists(true), where("room_num").exists(true)
          )
        )
        .apply(
          new Update().rename("facility_name", "facilityName")
            .rename("unit_num", "unitNum").rename("room_num", "roomNum")
        ).all();
  }
}
