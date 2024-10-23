package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;

@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=7.0.12")
public class EmployeeRepositoryTests {
  @Autowired
  EmployeeRepository employeeRepository;

  @AfterEach
  public void resetDB() {
    employeeRepository.deleteAll();
  }

  @Test
  public void findEmployeeByFirstNameAndSurname() throws Exception {
    Profession profession = Profession.of("abc", "def");
    Employee fooBar = Employee.of("Foo", "Bar", profession);
    Employee fooBarfoo = Employee.of("Foo", "Barfoo", profession);
    Employee fizzBuzz = Employee.of("Fizz", "Buzz", profession);
    Employee fishyBuzz = Employee.of("Fishy", "Buzz", profession);
    employeeRepository.saveAll(List.of(fooBar, fooBarfoo, fizzBuzz, fishyBuzz));
    // - Sanity check that all 4 Employees are saved
    assertThat(employeeRepository.count()).isEqualTo(4);

    // - WHEN only the `firstName` OR `surname` match, THEN no document is returned, so `null` is returned
    assertThat(employeeRepository.findByFirstNameAndSurname("Fizz", null)).isNull();
    assertThat(employeeRepository.findByFirstNameAndSurname("", "Buzz")).isNull();
    
    // - WHEN Employees with the same `firstName` exist
    Employee employeeWithSameFirstName = employeeRepository.findByFirstNameAndSurname("Foo", "Barfoo");
    // - THEN ONLY the Employee with BOTH a matching `firstName` and `surname` is returned
    assertThat(employeeWithSameFirstName).isNotNull();
    assertThat(employeeWithSameFirstName.getFirstName()).isEqualTo(fooBar.getFirstName()).isEqualTo(fooBarfoo.getFirstName());
    assertThat(employeeWithSameFirstName.getSurname()).isNotEqualTo(fooBar.getSurname()).isEqualTo(fooBarfoo.getSurname());

    // - WHEN Employees with the same `surname` exist
    Employee employeeWithSameSurname = employeeRepository.findByFirstNameAndSurname("Fizz", "Buzz");
    // - THEN ONLY the Employee with BOTH a matching `firstName` and `surname` is returned
    assertThat(employeeWithSameSurname).isNotNull();
    assertThat(employeeWithSameSurname.getSurname()).isEqualTo(fizzBuzz.getSurname()).isEqualTo(fishyBuzz.getSurname());
    assertThat(employeeWithSameSurname.getFirstName()).isEqualTo(fizzBuzz.getFirstName()).isNotEqualTo(fishyBuzz.getFirstName());

    // - WHEN Employees exists with identical `firstName` and `surname`
    employeeRepository.save(Employee.of("Foo", "Bar", profession));
    // - THEN attempting to find a match causes the query to throw
    assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
      employeeRepository.findByFirstNameAndSurname("Foo", "Bar");
    });
  }
}
