package edu.usc.nlcaceres.infectionprotection_backend.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Unlike the simple REST version, this focuses on handling real data sent from a MongoDb Development Database and the response
@SpringBootTest // Ensures real Service and real Repository are injected (whereas @WebMvcTest will NOT load them)
@AutoConfigureMockMvc // Enables MockMvc to be injected
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc; // MockMvc can easily interact with any or all controllers you want to test!
    @Autowired
    private ObjectMapper mapper; // Useful to convert mocks into JSON strings for comparison
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void findEmployeeList() throws Exception { // Can add "andDo(print())" to the chain for A LOT of helpful info from MvcResult
        String jsonResponse = mockMvc.perform(get("/api/employees")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        // In order to get a Class value token, we can call "mapper.getTypeFactory().constructCollectionType(List.class, Employee.class);"
        // BUT Jackson's readValue() actually can accept a TypeReference instead! making the call "new TypeReference<>() {}"
        // BUT we can make it even simpler by taking Class token from a Java Array of our class and running Arrays.asList to convert it
        List<Employee> actualList = Arrays.asList(mapper.readValue(jsonResponse, Employee[].class));
        assertThat(actualList).hasSize(5); // The above Arrays.asList technique is also surprisingly faster!
    }
    @Test
    public void findSingleEmployee() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/employees/5dc9b295cab4fa0bd0b23d52"))
                .andExpect((status().isOk()))
                .andReturn().getResponse().getContentAsString();

        Employee actualEmployee = mapper.readValue(jsonResponse, Employee.class);
        assertThat(actualEmployee.getFirstName()).isEqualTo("Jill");
        assertThat(actualEmployee.getSurname()).isEqualTo("Chambers");
    }
    @Test
    public void unableToFindEmployee() throws Exception {
        // If deeper inspection is needed into the Response or Request, can use MvcResult via "andReturn"
        MvcResult result = mockMvc.perform(get("/api/employees/123"))
                .andExpect((status().isNotFound()))
                .andReturn();
        // EVEN THOUGH, a TON is possible with the fluent api -> "andExpect" or "andExpectAll"
        assertThat(result.getResponse().getContentAsString()).isEmpty(); // BUT an "assertThat" feels so nice and simple!
    }
}
