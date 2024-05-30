package edu.usc.nlcaceres.infectionprotection_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode
@Document(collection = "professions") @JsonView(JsonViews.Public.class)
public class Profession {

    public static Profession of(String id, String observedOccupation, String serviceDiscipline) {
        return new Profession(id, observedOccupation, serviceDiscipline);
    }

    @NonNull @Id private String id;

    @NonNull @Field("observed_occupation") private String observedOccupation;

    @NonNull @Field("service_discipline") private String serviceDiscipline;
}
