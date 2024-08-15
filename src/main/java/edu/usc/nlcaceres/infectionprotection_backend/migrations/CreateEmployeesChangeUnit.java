package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.List;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.EmployeeRepository;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ProfessionRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id="create-employees", order="2")
public class CreateEmployeesChangeUnit {
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
    if (!mongoTemplate.collectionExists("employees")) {
      mongoTemplate.createCollection("employees");
    }
  }
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
    mongoTemplate.dropCollection("employees");
  }

  @Execution
  public void execution(EmployeeRepository employeeRepository, ProfessionRepository professionRepository) {
    Profession doctor = professionRepository
      .findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Clinic", "Doctor");
    Profession nurse = professionRepository
      .findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Clinic", "Nurse");

    if (employeeRepository.count() == 0) {
      employeeRepository.saveAll(List.of(
        Employee.of("John", "Smith", nurse),
        Employee.of("Jill", "Chambers", nurse),
        Employee.of("Victor", "Richards", doctor),
        Employee.of("Melody", "Rios", doctor),
        Employee.of("Brian", "Ishida", doctor)
      ));
    }
  }
  @RollbackExecution
  public void rollbackExecution(EmployeeRepository employeeRepository) {
    employeeRepository.deleteAll();
  }
}
