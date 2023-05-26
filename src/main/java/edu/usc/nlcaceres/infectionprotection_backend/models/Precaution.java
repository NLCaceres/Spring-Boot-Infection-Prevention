package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.util.List;

@Data @RequiredArgsConstructor
@Document(collection = "precautions")
public class Precaution {
    @Id @NonNull private String id;
    @NonNull private String name;
    // Usually access HealthPractices via a parent Precaution so the practicesList may be empty but never null
    @ToString.Exclude
    @ReadOnlyProperty @DocumentReference(lookup="{'precaution':?#{#self._id} }") List<HealthPractice> healthPractices;
    // ReadOnlyProperty's will NOT be saved to its MongoDB document, instead we can use this document's ID to query for
    // HealthPractices with matching IDs in a 'precaution' BSON property via @DocumentReference(lookup)
    // Which is a bit better than using @JsonManagedReference (with @JsonBackReference for HealthPractice's Precaution prop)
    // Unfortunately ManagedRef & BackRef cause the List to print in the Precaution JSON, but NOT the Precaution in the HealthPractice JSON

    // Jackson by default loads @DocumentReference(lazy), potentially causing an endless recursive mess without being cut off.
    // W/out access to the JacksonMapper, setting each healthPractice's parent Precaution ref equal to null is the easiest solution
    public void removeBackReference() {
        getHealthPractices().parallelStream().forEach(healthPractice -> healthPractice.setPrecaution(null));
    } // NOTE: Parallel Stream on a short list can be slower than a normal stream if the work load is simple.
}
