package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportTests {
    @Test
    public void testStaticFactory() {
        // - WHEN 5 params are used
        Report report = Report.of(
            "123", Employee.of("Foo", "Bar", Profession.of("Fizz", "Buzz")),
            HealthPractice.of("321", "Fee"), Location.of("ABC", "1", "213"),
            Instant.parse("2022-12-25T13:14:00.018Z")
        );
        // - THEN all fields of the Report model are properly filled
        assertThat(report.getId()).isEqualTo("123");
        assertThat(report.getEmployee().getFirstName()).isEqualTo("Foo");
        assertThat(report.getHealthPractice().getId()).isEqualTo(("321"));
        assertThat(report.getLocation().getFacilityName()).isEqualTo(("ABC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a");
        assertThat(
            report.getDate().atZone(ZoneId.of("America/Los_Angeles")).format(formatter)
        ).isEqualTo(("Dec 25, 2022 5:14 AM"));

        // - WHEN 4 params are used
        Report anotherReport = Report.of(
            Employee.of("Roo", "Sue", Profession.of("Do", "Shoe")),
            HealthPractice.of("421", "Fai"), Location.of("DEF", "2", "414"),
            Instant.parse("2023-02-14T00:31:00.018Z")
        );
        // - THEN all fields of the Report EXCEPT the ID are filled
        assertThat(anotherReport.getId()).isNull(); // - ID is the only `null` field
        assertThat(anotherReport.getEmployee().getFirstName()).isEqualTo("Roo");
        assertThat(anotherReport.getHealthPractice().getId()).isEqualTo("421");
        assertThat(anotherReport.getLocation().getFacilityName()).isEqualTo("DEF");
        assertThat(
            anotherReport.getDate().atZone(ZoneId.of("America/Los_Angeles")).format(formatter)
        ).isEqualTo(("Feb 13, 2023 4:31 PM"));
    }
    @Test
    public void testLombokEquals() {
        Profession otherProfession = Profession.of("Foo", "Bar", "Fizz");
        Employee otherEmployee = Employee.of("Foo", "Bar", "Fizz", otherProfession);
        HealthPractice otherHealthPractice = HealthPractice.of("Foo", "Bar");
        Location otherLocation = Location.of("Abc", "Facility", "Unit", "Room");
        Instant otherInstant = Instant.now();
        Report report = ModelFactory.getReport();
        //* WHEN only the id matches, THEN the reports are NOT equal */
        Report otherReport = Report.of("abc", otherEmployee, otherHealthPractice, otherLocation, otherInstant);
        assertThat(report.equals(otherReport)).isFalse();

        Profession profession = ModelFactory.getProfession(null, null);
        Employee employee = ModelFactory.getEmployee(null, null, profession);
        //* WHEN only the id and employee match, THEN the reports are NOT equal */
        Report anotherReport = Report.of("abc", employee, otherHealthPractice, otherLocation, otherInstant);
        assertThat(report.equals(anotherReport)).isFalse();

        HealthPractice healthPractice = ModelFactory.getHealthPractice(null);
        //* WHEN only the id, employee, and healthPractice match, THEN the reports are NOT equal */
        Report someReport = Report.of("abc", employee, healthPractice, otherLocation, otherInstant);
        assertThat(report.equals(someReport)).isFalse();

        Location location = ModelFactory.getLocation(null, null, null);
        //* WHEN only the id, employee, healthPractice, and location match, THEN the reports are NOT equal */
        Report someOtherReport = Report.of("abc", employee, healthPractice, location, otherInstant);
        assertThat(report.equals(someOtherReport)).isFalse();

        Report finalReport = Report.of("abc", employee, healthPractice, location, report.getDate());
        //* WHEN all field values match, THEN the reports are equal */
        assertThat(report.equals(finalReport)).isTrue();
        System.out.println(report.toString());
    }
    @Test
    public void testLombokToString() {
        Report report = ModelFactory.getReport();
        assertThat(report.toString())
            .isEqualTo("Report(id=abc, employee=Employee(id=abc, firstName=Melody, surname=Rios, " +
                "profession=Profession(id=abc, observedOccupation=Clinic, serviceDiscipline=Doctor)), " +
                "healthPractice=HealthPractice(id=abc, name=Hand Hygiene, precautionType=Standard), " +
                "location=Location(id=abc, facilityName=USC, unitNum=2, roomNum=123), date=" + report.getDate() + ")");
    }
}
