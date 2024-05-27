package edu.usc.nlcaceres.infectionprotection_backend.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//? Spring-Boot's Data lib prefers making an @AllArgsConstructor and letting factory methods like Precaution of() handle "overloaded" params
//? BUT Jackson NEEDS a @NoArgsConstructor for parsing JSON to Objs and vice-versa (though it could more slowly use a constructor marked with @JsonCreator)
//? SO Spring will ALSO use the @NoArgsConstructor to build instances of the model like it's the empty Java class default constructor
//? IF I wanted Spring to use the factory methods instead, THEN I could mark it with @PersistanceCreator BUT it's simpler to let Spring decide
@Document(collection = "precautions") @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Precaution {

    public static Precaution of(String id, String name) {
        return new Precaution(id, name, new ArrayList<>());
    }

    @JsonView({ JsonViews.Public.class, PublicJsonView.class }) @NonNull @Id private String id;

    @JsonView({ JsonViews.Public.class, PublicJsonView.class }) @NonNull private String name;

    //? ReadOnlyProperty's will NOT be saved to its MongoDB document, instead we can use this document's ID to query for
    //? HealthPractices with matching IDs in a 'precaution' BSON property via @DocumentReference(lookup)
    @JsonView(PublicJsonView.class) @ReadOnlyProperty @DocumentReference(lookup="{ 'precaution':?#{#self._id} }")
    private List<HealthPractice> healthPractices;
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
    
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }

        Precaution other = (Precaution) obj;

        if (!id.equals(other.id) || !name.equals(other.name)) {
            return false;
        }

        if (healthPractices == null && other.healthPractices == null) {
            return true;
        }
        //? Since prev condition checks if BOTH healthPractice fields are null, now ensure BOTH lists are NOT null AND the same size
        if (!(healthPractices != null && other.healthPractices != null) || healthPractices.size() != other.healthPractices.size()) {
            return false; //* Made it here if one or the other healthPractice list is null OR they're different size
        }
        //? To avoid setting off a stack overflow calling healthPractices.equals(other.healthPractices)
        //? Compare the lists one element at a time, BUT first sort them to avoid the comparison failing due to order
        List<HealthPractice> sortedHealthPractices = new ArrayList<>(healthPractices);
        List<HealthPractice> sortedOtherHealthPractices = new ArrayList<>(other.healthPractices);
        sortedHealthPractices.sort(Comparator.comparing(HealthPractice::getId).thenComparing(Comparator.comparing(HealthPractice::getName)));
        sortedOtherHealthPractices.sort(Comparator.comparing(HealthPractice::getId).thenComparing(Comparator.comparing(HealthPractice::getName)));
        for (int i = 0; i < sortedHealthPractices.size(); i++) {
            HealthPractice healthPractice = sortedHealthPractices.get(i);
            HealthPractice otherHealthPractice = sortedOtherHealthPractices.get(i);
            if (otherHealthPractice == null ||
                !healthPractice.getId().equals(otherHealthPractice.getId()) || !healthPractice.getName().equals(otherHealthPractice.getName())) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result + name.hashCode();
        //? Can't use .forEach() since using vars outside the lambda scope must be final
        for (HealthPractice healthPractice : healthPractices) {
            result = prime * result + healthPractice.getId().hashCode();
            result = prime * result + healthPractice.getName().hashCode();
        }
        return result;
    }
    //* The following interface lets Precaution set its own JSON shape, which will help the Precaution REST API controller create a better response
    //* that differs from what other REST API controllers are able to see of the Precaution type via the default/common JsonViews.Public annotation
    public interface PublicJsonView {}
}
