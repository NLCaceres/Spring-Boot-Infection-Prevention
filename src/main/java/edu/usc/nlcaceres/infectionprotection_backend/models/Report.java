package edu.usc.nlcaceres.infectionprotection_backend.models;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode
@Document(collection = "reports") @JsonView(JsonViews.Public.class)
public class Report {

    public static Report of(String id, Employee employee, HealthPractice healthPractice, Location location, Instant date) {
        return new Report(id, employee, healthPractice, location, date);
    }
    public static Report of(Employee employee, HealthPractice healthPractice, Location location, Instant date) {
        Report report = new Report();
        report.setEmployee(employee);
        report.setHealthPractice(healthPractice);
        report.setLocation(location);
        report.setDate(date);
        return report;
    }

    @NonNull @Id private String id;

    @NonNull @DocumentReference private Employee employee;

    @NonNull @Field("health_practice") @DocumentReference private HealthPractice healthPractice;

    @NonNull @DocumentReference private Location location;

    @NonNull @Field("date_reported") private Instant date;
}
