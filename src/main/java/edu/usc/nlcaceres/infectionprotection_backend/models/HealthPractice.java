package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Could also add a @JsonInclude(Include.NON_NULL) to ensure null values never get sent to MongoDb
@Data @RequiredArgsConstructor(staticName = "of")
@Document(collection = "healthpractices")
public class HealthPractice {
    // Commonly receive healthPractices through Precaution's One-to-Many relationship so an instance of HealthPractice
    // Will commonly have a null Precaution since that HealthPractice was accessed through its parent Precaution
    @Id @NonNull private String id;
    @NonNull private String name;
    private Precaution precaution; // Field in database is currently "precautionType"
}
