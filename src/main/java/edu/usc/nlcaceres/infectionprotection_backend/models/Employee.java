package edu.usc.nlcaceres.infectionprotection_backend.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Data // ToString, Equals, HashCode, Setters (on non-final), Getters
@RequiredArgsConstructor(staticName = "of") // On the other hand, there's @Builder, i.e. Employee.builder().firstName("foo").surname("bar").build()
@Document(collection = "employees")
public class Employee {

    @Id @NonNull private String id;
    @Field("first_name") @NonNull private String firstName; // @NonNull works when the property is actually used, adding an "if (prop == null)" check
    // Consequently, if using @NoArgsConstructor, NO NullPointerException is thrown until the prop is accessed w/out having been set
    @NonNull private String surname;
    @DocumentReference @NonNull private Profession profession;
}
