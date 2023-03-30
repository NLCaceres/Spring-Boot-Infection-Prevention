package edu.usc.nlcaceres.infectionprotection_backend;

import edu.usc.nlcaceres.infectionprotection_backend.models.*;
import java.time.Instant;
import java.util.List;

public class ModelFactory {
    // Get Single Instance of Model
    // Get "Melody Rios" with id "abc" OR with a name and profession of your choice
    public static Employee getEmployee(String firstName, String surname, Profession profession) {
        String finalFirstName = firstName != null ? firstName : "Melody";
        String finalSurname = surname != null ? surname : "Rios";
        Profession finalProfession = profession != null ? profession : getProfession(null, null);
        return Employee.of("abc", finalFirstName, finalSurname, finalProfession);
    }
    // Get a "Hand Hygiene" Health Practice with id "abc" OR with a name of your choice
    public static HealthPractice getHealthPractice(String name) {
        String finalName = name != null ? name : "Hand Hygiene";
        return HealthPractice.of("abc", finalName);
    }
    // Get a "USC 2 123" Location with id "abc" or facilityName, unitNum, and roomNum of your choice
    public static Location getLocation(String facilityName, String unitNum, String roomNum) {
        String finalFacilityName = facilityName != null ? facilityName : "USC";
        String finalUnitNum = unitNum != null ? unitNum : "2";
        String finalRoomNum = roomNum != null ? roomNum : "123";
        return Location.of("abc", finalFacilityName, finalUnitNum, finalRoomNum);
    }
    // Get an "Isolation" precaution with id "abc" OR with a name of your choice
    public static Precaution getPrecaution(String name) {
        String finalName = name != null ? name : "Isolation";
        return Precaution.of("abc", finalName, List.of(getHealthPractice(null), getHealthPractice("PPE")));
    }
    // Get a "Clinic Doctor" with id "abc" OR with an occupation and discipline of your choice
    public static Profession getProfession(String observedOccupation, String serviceDiscipline) {
        String finalObservedOccupation = observedOccupation != null ? observedOccupation : "Clinic";
        String finalServiceDiscipline = serviceDiscipline != null ? serviceDiscipline : "Doctor";
        return Profession.of("abc", finalObservedOccupation, finalServiceDiscipline);
    }
    public static Report getReport() { // Get a report with all the defaults
        return Report.of("abc", getEmployee(null, null, null),
                getHealthPractice(null), getLocation(null, null, null), Instant.now());
    }

    // Get List of Model Instances
    public static List<Employee> getEmployeeList() { // Get 2 employees - 1 doctor, 1 nurse
        Employee secondEmployee = getEmployee("John", "Smith", getProfession("Clinic", "Nurse"));
        return List.of(getEmployee(null, null, null), secondEmployee);
    }
    public static List<HealthPractice> getHealthPracticeList() { // Get 3 healthPractices - "Hand Hygiene", "PPE", "Airborne
        return List.of(getHealthPractice(null), getHealthPractice("PPE"), getHealthPractice("Airborne"));
    }
    public static List<Location> getLocationList() {  // Get 2 locations - "USC 2 123" and "HSC 3 213"
        return List.of(getLocation(null, null, null), getLocation("HSC", "3", "213"));
    }
    public static List<Precaution> getPrecautionList() { // Get 2 precautions - "Isolation" and "Standard"
        return List.of(getPrecaution(null), getPrecaution("Standard"));
    }
    public static List<Profession> getProfessionList() { // Get 2 professions - "Clinic Doctor" and "Clinic Nurse"
        return List.of(getProfession(null, null), getProfession("Clinic", "Nurse"));
    }
    public static List<Report> getReportList() { // Get 1 report with all the defaults
        return List.of(getReport());
    }
}
