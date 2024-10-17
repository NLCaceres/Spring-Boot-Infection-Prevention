package edu.usc.nlcaceres.infectionprotection_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//? To limit recursion, there's @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//? BUT isn't great with 1-Many Relationships, so there's also @JsonInclude(Include.NON_NULL) to prevent null values from being sent to MongoDB
@Document(collection = "healthpractices") @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class HealthPractice {

    public static HealthPractice of(String id, String name) {
        return new HealthPractice(id, name, null);
    }
    public static HealthPractice of(String name, Precaution precaution) {
        HealthPractice healthPractice = new HealthPractice();
        healthPractice.setName(name);
        healthPractice.setPrecaution(precaution);
        return healthPractice;
    }

    @JsonView({ JsonViews.Public.class, Precaution.PublicJsonView.class }) @NonNull @Id private String id;

    @JsonView({ JsonViews.Public.class, Precaution.PublicJsonView.class }) @NonNull private String name;

    //? Lazy without Jackson @JsonView isn't enough to avoid the HealthPractice-Precaution cyclic ref
    @DocumentReference(lazy = true) @JsonView(JsonViews.Public.class)
    @Nullable private Precaution precaution;

    @Override
    public boolean equals(@Nullable Object obj) { //? @Nullable required to match original param type signature
        if (this == obj) { return true; } //? Memory address check
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }

        HealthPractice other = (HealthPractice) obj;

        if (!id.equals(other.id) || !name.equals(other.name)) { //? `x.equals(null)` ALWAYS returns false
            return false;
        }

        if (precaution != null && other.precaution != null) { //? Avoiding an endless loop set off by precaution.equals(other.precaution)
            return precaution.getId().equals(other.precaution.getId()) && precaution.getName().equals(other.precaution.getName());
        }
        else if (precaution == null && other.precaution == null) {
            return true;
        }
        else {
            return false;
        }
    }

    //? Referencing precaution in toString and hashcode causes cyclic refs which lead to a stack overflow
    @Override
    public String toString() {
        String healthPracticeInfo = "HealthPractice(id=" + id + ", name=" + name;
        return precaution != null ? healthPracticeInfo + ", precautionType=" + precaution.getName() + ")" : healthPracticeInfo + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result + name.hashCode();
        if (precaution == null) {
            result = prime * result;
        }
        else {
            result = prime * result + precaution.getId().hashCode();
            result = prime * result + precaution.getName().hashCode();
        }
        return result;
    }
}
