package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.time.Instant;

@Data @RequiredArgsConstructor(staticName = "of")
@Document(collection = "reports")
public class Report {
    @Id @NonNull private String id;
    @DocumentReference @NonNull private Employee employee;
    // TODO: Convert field from "healthPractice" to "health_practice" (see Location.java for details)
    @DocumentReference @NonNull private HealthPractice healthPractice;
    @DocumentReference @NonNull private Location location;
    @NonNull private Instant date;
}
