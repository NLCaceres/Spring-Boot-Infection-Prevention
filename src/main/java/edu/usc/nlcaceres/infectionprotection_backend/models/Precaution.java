package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.util.List;

@Data @RequiredArgsConstructor(staticName = "of")
@Document(collection = "precautions")
public class Precaution {
    // Commonly receive HealthPractices through a parent Precaution so at worst the practicesList will be empty but never null
    @Id @NonNull private String id;
    @NonNull private String name;
    @DocumentReference @NonNull private List<HealthPractice> practices;
}
