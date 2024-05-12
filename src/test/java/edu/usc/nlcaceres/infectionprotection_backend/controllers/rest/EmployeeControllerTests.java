package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.EmployeeController;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

// Unlike integration version, this mocks out data to focus more on the interactions of the controllers
@DisabledInAotMode
@WebMvcTest(EmployeeController.class) // Will only start up this controller (w/out the param, all controllers will start)
public class EmployeeControllerTests { // Using @WebMvcTest, and not @SpringBootTest, will only start up Spring-Web, not the full app

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void findEmployeeList() throws Exception {
        List<Employee> mockEmployeeList = ModelFactory.getEmployeeList();
        when(employeeService.getAll()).thenReturn(mockEmployeeList);

        List<Employee> actualList = employeeController.getAll();
        assertThat(actualList).isEqualTo(mockEmployeeList);
    }
    @Test
    public void findSingleEmployee() throws Exception {
        Employee mockEmployee = ModelFactory.getEmployee(null, null, null);
        when(employeeService.getById("abc")).thenReturn(mockEmployee);

        Employee actualEmployee = employeeController.getById("abc").getBody();
        assertThat(actualEmployee).isEqualTo(mockEmployee);
    }
    @Test
    public void unableToFindEmployee() throws Exception {
        when(employeeService.getById("123")).thenThrow(new RuntimeException());

        Employee actualEmployee = employeeController.getById("123").getBody();
        assertThat(actualEmployee).isNull();
    }
}
