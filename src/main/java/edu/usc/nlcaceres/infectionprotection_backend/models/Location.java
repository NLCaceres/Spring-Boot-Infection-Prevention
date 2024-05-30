package edu.usc.nlcaceres.infectionprotection_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode
@Document(collection = "locations") @JsonView(JsonViews.Public.class)
public class Location {

    public static Location of(String id, String facilityName, String unitNum, String roomNum) {
        return new Location(id, facilityName, unitNum, roomNum);
    }

    @NonNull @Id private String id;

    //* Why change field names to snake_case? It is common convention in SQL based databases even if MongoDB seems to prefer
    //* the camelCase style due to its Javascript roots. If I switch to SQL style snake_case though, migration would be simpler
    //* Overall MongoDB doesn't really seem to care BUT SQL databases DO CARE so easiest to fall in line with the SQL convention
    @NonNull private String facilityName; // TODO: Use Mongock to create a MongoDB update script and convert the camelCase field names to snake_case

    @NonNull private String unitNum;

    @NonNull private String roomNum;
}
