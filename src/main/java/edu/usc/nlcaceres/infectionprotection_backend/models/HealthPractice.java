package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.util.ArrayList;

// Could also add a @JsonInclude(Include.NON_NULL) to ensure null values never get sent to MongoDb
@Data // Since each HealthPractice has a ref to its parent Precaution, and vice versa, the generated toString can cause Stack Overflows
@RequiredArgsConstructor // if not cut off somewhere down the cyclic/recursive references
@Document(collection = "healthpractices") // JsonIdentityInfo is one way to limit recursion BUT isn't perfect in One-to-Many relationships
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HealthPractice {
    @Id @NonNull private String id;
    @NonNull private String name;
    // HealthPractice accessed via a parent Precaution should set this Precaution ref to null to avoid cyclic references
    @ToString.Exclude // Excluding this property from Lombok's generated toString should prevent overflows
    @DocumentReference(lazy = true) private Precaution precaution; // Lazy isn't enough if Jackson or toString recursively loads this ref in

    public void removeBackReference() {
        getPrecaution().setHealthPractices(new ArrayList<>());
    }
}
