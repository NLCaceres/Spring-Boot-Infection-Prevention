package edu.usc.nlcaceres.infectionprotection_backend.controllers.graphql;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.EmployeeGraphController;
import edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars.DateScalarConfiguration;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.aot.DisabledInAotMode;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@GraphQlTest(EmployeeGraphController.class)
@Import(DateScalarConfiguration.class)
public class EmployeeControllerTests {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void getEmployeeList() throws Exception {
        List<Employee> mockEmployeeList = ModelFactory.getEmployeeList();
        when(employeeService.getAll()).thenReturn(mockEmployeeList);

        //? BEFORE Jackson defaulted to the @RequiredArgsConstructor WHICH caused a DecodingException if it didn't find a Profession key-value
        //* NOW Jackson uses the @NoArgsConstructor, allowing GraphQL to NOT return a Profession IF NOT requested */
        //? SO the GraphQL "!" non-null operator JUST indicates to clients that the Profession won't be null IF requested
        List<Employee> employees = this.tester.document("{ employees { id firstName surname } }").execute().path("employees")
            .entityList(Employee.class).get();
        assertThat(employees).hasSize(2);
        employees.forEach(employee -> assertThat(employee.getProfession()).isNull());

        //* Requesting 'profession' now fills the Profession field of the Employees as expected, of course */
        List<Employee> fullEmployees = this.tester.document("{ employees { id firstName surname profession { id observedOccupation serviceDiscipline } } }")
            .execute().path("employees").entityList(Employee.class).get();
        assertThat(fullEmployees).hasSize(2);
        fullEmployees.forEach(employee -> assertThat(employee.getProfession()).isNotNull());
    }

    @Test
    public void getEmployeeById() throws Exception {
        Employee mockEmployee = ModelFactory.getEmployee(null, null, null);
        when(employeeService.getById("abc")).thenReturn(mockEmployee);

        //? An alternative and, technically, more accurate view into the response GraphQL sends is the following example
        //? BUT it isn't the best solution for Lists since they can be fairly long and tough to assert upon in JSON form
        this.tester.document("{ employeeById(id: \"abc\") { firstName surname } }").execute().path("employeeById")
                .matchesJson("{ \"firstName\": \"Melody\", \"surname\": \"Rios\" } ");
    }
    @Test
    public void tryToGetUnknownEmployee() throws Exception {
        when(employeeService.getById("123")).thenThrow(new RuntimeException());
        this.tester.document("{ employeeById(id: \"123\") { firstName surname } }").execute().path("employeeById")
                .valueIsNull();
    }
}
