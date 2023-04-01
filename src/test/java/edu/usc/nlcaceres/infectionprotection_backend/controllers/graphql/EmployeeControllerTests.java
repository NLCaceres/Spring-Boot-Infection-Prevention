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
import org.springframework.core.codec.DecodingException;
import org.springframework.graphql.test.tester.GraphQlTester;
import java.util.List;
import static org.mockito.Mockito.when;

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

        // Decoding error thrown here due to invalid JSON returned BUT GraphQL's "!" non-null operator does NOT guarantee that
        // all fields will be returned or else throw. Instead, it simply lets clients know that those fields will always be non-null if requested
        try { // So the real issue with the following query is the Employee Lombok @NonNull class declaration, NOT the Controller
            this.tester.document("{ employees { id firstName surname } }").execute().path("employees").entityList(Employee.class).get();
        } catch (DecodingException error) {
            System.out.println("Incomplete Employee JSON sent causing DecodingException. Spring unable to instantiate Employee object");
        }
        this.tester.document("{ employees { id firstName surname profession { id observedOccupation serviceDiscipline } } }").execute()
                .path("employees").entityList(Employee.class).hasSize(2);
    }

    @Test
    public void getEmployeeById() throws Exception {
        Employee mockEmployee = ModelFactory.getEmployee(null, null, null);
        when(employeeService.getById("abc")).thenReturn(mockEmployee);

        // An alternative and, technically, more accurate view into the response GraphQL sends is the following example
        // BUT it isn't the best solution for Lists since they can be fairly long and tough to assert upon in JSON form
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
