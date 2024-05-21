package edu.usc.nlcaceres.infectionprotection_backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

// Could also add a @JsonInclude(Include.NON_NULL) to ensure null values never get sent to MongoDb
@Data // Since each HealthPractice has a ref to its parent Precaution, and vice versa, the generated toString can cause Stack Overflows
@RequiredArgsConstructor // if not cut off somewhere down the cyclic/recursive references
@Document(collection = "healthpractices") // JsonIdentityInfo is one way to limit recursion BUT isn't perfect in One-to-Many relationships
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HealthPractice {
    @JsonView({ JsonViews.Public.class, Precaution.PublicJsonView.class }) @Id @NonNull private String id;
    @JsonView({ JsonViews.Public.class, Precaution.PublicJsonView.class }) @NonNull private String name;
    // HealthPractice accessed via a parent Precaution should set this Precaution ref to null to avoid cyclic references
    @ToString.Exclude // Excluding this property from Lombok's generated toString should prevent overflows
    @DocumentReference(lazy = true) @JsonView(JsonViews.Public.class)
    private Precaution precaution; // Lazy isn't enough if Jackson or toString recursively loads this ref in
}
