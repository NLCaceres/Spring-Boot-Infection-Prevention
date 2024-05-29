package edu.usc.nlcaceres.infectionprotection_backend.models;

import java.util.List;
import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

//? Spring-Boot's Data lib prefers making an @AllArgsConstructor and letting factory methods like Precaution of() handle "overloaded" params
//? BUT Jackson NEEDS a @NoArgsConstructor for parsing JSON to Objs and vice-versa (though it could more slowly use a constructor marked with @JsonCreator)
//? SO Spring will ALSO use the @NoArgsConstructor to build instances of the model like it's the empty Java class default constructor
//? IF I wanted Spring to use the factory methods instead, THEN I could mark it with @PersistanceCreator BUT it's simpler to let Spring decide
@Document(collection = "precautions") @AllArgsConstructor @Getter @Setter @EqualsAndHashCode
public class Precaution {

    public Precaution() {
        this.id = null;
        this.name = null;
        this.healthPractices = new ArrayList<>();
    }
    public static Precaution of(String id, String name) {
        return new Precaution(id, name, new ArrayList<>());
    }

    @JsonView({ JsonViews.Public.class, PublicJsonView.class }) @NonNull @Id private String id;

    @JsonView({ JsonViews.Public.class, PublicJsonView.class }) @NonNull private String name;

    //? ReadOnlyProperty's will NOT be saved to its MongoDB document, instead we can use this document's ID to query for
    //? HealthPractices with matching IDs in a 'precaution' BSON property via @DocumentReference(lookup)
    @JsonView(PublicJsonView.class) @ReadOnlyProperty @DocumentReference(lookup="{ 'precaution':?#{#self._id} }")
    @NonNull private List<HealthPractice> healthPractices;
    //? @DocumentReference(lookup) is better than using Jackson's @JsonManagedReference here with @JsonBackReference in HealthPractice

    @Override
    public String toString() {
        String precautionInfo = "Precaution(id=" + id + ", name=" + name + ", healthPractices=[";
        String healthPracticeInfo = healthPractices.stream().reduce("", (finalStr, healthPractice) -> {
            return finalStr + healthPractice.getName() + ", ";
        }, String::concat);
        String finalHealthPracticeInfo =
            healthPracticeInfo.endsWith(", ") ? healthPracticeInfo.substring(0, healthPracticeInfo.length() - 2) : "";
        return precautionInfo + finalHealthPracticeInfo + "])";
    }
    
    //* The following interface lets Precaution set its own JSON shape, which will help the Precaution REST API controller create a better response
    //* that differs from what other REST API controllers are able to see of the Precaution type via the default/common JsonViews.Public annotation
    public interface PublicJsonView {}
}
