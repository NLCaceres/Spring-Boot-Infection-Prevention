package edu.usc.nlcaceres.infectionprotection_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//? All Model classes are a part of the @NonNullAPI AND @NonNullFields declaration in models.package-info.java
//? This acts similar to Kotlin where all class props, method params and method returns ARE CONSIDERED NON-NULL BY DEFAULT
//? SO we have to opt into null values via Spring's @Nullable annotation, similar to using Kotlin optionals!
//? HOWEVER, unlike Lombok, Spring's annotations don't generate checks for you!
//? It IS STILL POSSIBLE to get a Null-Pointer Exception if your code runs into some null value unexpectedly
//? SO important to create null checks UNLESS I just pepper in Lombok where it doesn't generate error-prone code
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode
@Document(collection = "employees") @JsonView(JsonViews.Public.class)
public class Employee {

    public static Employee of(String id, String firstName, String surname, Profession profession) {
        return new Employee(id, firstName, surname, profession);
    }

    @NonNull @Id private String id;

    @NonNull @Field("first_name") private String firstName;

    @NonNull private String surname;

    @NonNull @DocumentReference private Profession profession;
}
//? Java 14 provides "records" as an alternative to Lombok's @Data annotation with the added bonus of immutability by default
//? Records are just like Kotlin data classes and declared similarly `public record Employee(String id, String firstName)`
//? They generate a constructor, getters, equals(), hashCode(), and toString()
//? The constructor can be overloaded AND the generated constructor can be modified as follows:
//? `public Employee { Objects.requireNonNull(firstName) }`
//? BUT Spring-Boot recommends against using them, so best to just work with normal Java classes, and if needed, add Lombok
