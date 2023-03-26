package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data @RequiredArgsConstructor(staticName = "of")
@Document(collection = "locations")
public class Location {
    @Id @NonNull private String id;
    @NonNull private String facilityName; // TODO: Convert Location field names in db from camelCase to snake_case
    // Why change field names to snake_case? It is common convention in SQL based databases even if MongoDB seems to prefer
    // the camelCase style due to its Javascript roots. If I switch to SQL style snake_case though, migration would be simpler
    // Overall MongoDB doesn't really seem to care BUT SQL databases DO CARE so easiest to fall in line with the SQL convention
    @NonNull private String unitNum;
    @NonNull private String roomNum;
}
