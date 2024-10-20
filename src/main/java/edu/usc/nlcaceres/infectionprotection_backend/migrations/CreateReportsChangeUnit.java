package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.data.mongodb.core.MongoTemplate;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.EmployeeRepository;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.HealthPracticeRepository;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.LocationRepository;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ReportRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="create-reports", order="7")
public class CreateReportsChangeUnit {
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
    if (!mongoTemplate.collectionExists("reports")) {
      mongoTemplate.createCollection("reports");
    }
  }
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.dropCollection("reports");
  }

  @Execution
  public void execution(
    ReportRepository reportRepository, EmployeeRepository employeeRepository,
    HealthPracticeRepository healthPracticeRepository, LocationRepository locationRepository
  ) {
    if (reportRepository.count() == 0) {
      List<Employee> employees = getAndOrderEmployees(employeeRepository);

      // - Hand Hygiene, Contact, Droplet, Hand Hygiene, PPE
      List<HealthPractice> healthPractices = getAndOrderHealthPractices(healthPracticeRepository);

      // - USC Unit 4 Room: 202 --> HSC Unit 3 Room: 321 --> HSC Unit 3 Room: 213 --> HSC Unit 5 Room: 121 --> USC Unit 2 Room: 123
      List<Location> locations = getAndOrderLocations(locationRepository);

      // - May 18 11:36PM PST, May 19..., May 25..., May 13..., April 21... (All same time PM PST)
      Instant timestamp = Instant.parse("2019-05-19T06:36:05.018Z");
      List<Instant> timestamps = List.of(
        timestamp, timestamp.plus(1, ChronoUnit.DAYS), timestamp.plus(7, ChronoUnit.DAYS),
        timestamp.minus(5, ChronoUnit.DAYS), timestamp.minus(27, ChronoUnit.DAYS)
      );

      List<Report> reports = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        reports.add(Report.of(employees.get(i), healthPractices.get(i), locations.get(i), timestamps.get(i)));
      }
      reportRepository.saveAll(reports);
    }
  }
  @RollbackExecution
  public void rollbackExecution(ReportRepository reportRepository) {
    reportRepository.deleteAll();
  }

  @FunctionalInterface
  interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
  }
  private List<Employee> getAndOrderEmployees(EmployeeRepository employeeRepository) {
    List<Employee> employees = employeeRepository.findAll();

    BiFunction<String, String, Predicate<Employee>> getEmployeeByFirstNameAndSurname = (String firstName, String surname) ->
      (Employee employee) -> employee.getFirstName().equals(firstName) && employee.getSurname().equals(surname);

    // ?: IF the ChangeUnit throws/fails anywhere, THEN the Rollback methods run, and the ChangeUnit re-runs next server startup
    Employee johnSmith = employees.stream().filter(getEmployeeByFirstNameAndSurname.apply("John", "Smith")).findFirst().get();
    Employee jillChambers = employees.stream().filter(getEmployeeByFirstNameAndSurname.apply("Jill", "Chambers")).findFirst().get();
    Employee victorRichards = employees.stream().filter(getEmployeeByFirstNameAndSurname.apply("Victor", "Richards")).findFirst().get();
    Employee melodyRios = employees.stream().filter(getEmployeeByFirstNameAndSurname.apply("Melody", "Rios")).findFirst().get();
    Employee brianIshida = employees.stream().filter(getEmployeeByFirstNameAndSurname.apply("Brian", "Ishida")).findFirst().get();

    return List.of(johnSmith, jillChambers, victorRichards, melodyRios, brianIshida);
  }
  private List<HealthPractice> getAndOrderHealthPractices(HealthPracticeRepository healthPracticeRepository) {
      List<HealthPractice> healthPractices = healthPracticeRepository.findAll();

      Function<String, Predicate<HealthPractice>> getHealthPracticeByName = (String name) ->
        (HealthPractice healthPractice) -> healthPractice.getName().equals(name);

      HealthPractice handHygiene = healthPractices.stream().filter(getHealthPracticeByName.apply("Hand Hygiene")).findFirst().get();
      HealthPractice contact = healthPractices.stream().filter(getHealthPracticeByName.apply("Contact")).findFirst().get();
      HealthPractice droplet = healthPractices.stream().filter(getHealthPracticeByName.apply("Droplet")).findFirst().get();
      HealthPractice ppe = healthPractices.stream().filter(getHealthPracticeByName.apply("PPE")).findFirst().get();

      return List.of(handHygiene, contact, droplet, handHygiene, ppe);
  }
  private List<Location> getAndOrderLocations(LocationRepository locationRepository) {
      List<Location> locations = locationRepository.findAll();

      TriFunction<String, String, String, Predicate<Location>> getLocationByFields =
        (String facilityName, String unitNum, String roomNum) -> (Location location) ->
          location.getFacilityName().equals(facilityName) && location.getUnitNum().equals(unitNum) && location.getRoomNum().equals(roomNum);

      Location uscUnit4 = locations.stream().filter(getLocationByFields.apply("USC", "4", "202")).findFirst().get();
      Location hscUnit3Room321 = locations.stream().filter(getLocationByFields.apply("HSC", "3", "321")).findFirst().get();
      Location hscUnit3Room213 = locations.stream().filter(getLocationByFields.apply("HSC", "3", "213")).findFirst().get();
      Location hscUnit5 = locations.stream().filter(getLocationByFields.apply("HSC", "5", "121")).findFirst().get();
      Location uscUnit2 = locations.stream().filter(getLocationByFields.apply("USC", "2", "213")).findFirst().get();

      return List.of(uscUnit4, hscUnit3Room321, hscUnit3Room213, hscUnit5, uscUnit2);
  }
}
