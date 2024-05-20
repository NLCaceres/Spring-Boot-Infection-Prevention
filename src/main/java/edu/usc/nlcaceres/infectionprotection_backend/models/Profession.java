package edu.usc.nlcaceres.infectionprotection_backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data @RequiredArgsConstructor(staticName = "of")
@Document(collection = "professions") @JsonView(JsonViews.Public.class)
public class Profession {
    @Id @NonNull private String id;
    @Field("observed_occupation") @NonNull private String observedOccupation;
    @Field("service_discipline") @NonNull private String serviceDiscipline;
}
