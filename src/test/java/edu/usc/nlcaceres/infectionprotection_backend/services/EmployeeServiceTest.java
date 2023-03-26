package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void findEmployeeList() throws Exception {
        List<Employee> mockEmployeeList = ModelFactory.getEmployeeList();
        when(employeeRepository.findAll()).thenReturn(mockEmployeeList);

        List<Employee> employeeList = employeeService.getAll();
        assertThat(employeeService).isNotNull();

        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isEqualTo(2);
    }
    @Test
    public void findSingleEmployee() throws Exception {
        Employee mockEmployee = ModelFactory.getEmployee(null, null, null);
        when(employeeRepository.findById("abc")).thenReturn(Optional.of(mockEmployee));

        Employee employee = employeeService.getById("abc");
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Melody");
    }
    @Test
    public void unableToFindEmployee() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            Employee employee = employeeService.getById("abc");
        });
    }
}
