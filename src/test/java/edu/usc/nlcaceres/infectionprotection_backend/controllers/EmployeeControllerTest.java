package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class) // Will only start up this controller (w/out the param, all controllers will start)
public class EmployeeControllerTest { // Using @WebMvcTest, and not @SpringBootTest, will only start up Spring-Web, not the full app

    @Autowired // Rather than autowire the controller to directly test it, INSTEAD
    private MockMvc mockMvc; // MockMvc can be injected to interact with any or all controllers you want to test!
    @Autowired
    ObjectMapper mapper; // Useful to convert mocks into JSON strings for comparison

    @MockBean
    EmployeeService employeeService;

    @Test
    public void findEmployeeList() throws Exception {
        List<Employee> mockEmployeeList = ModelFactory.getEmployeeList();
        when(employeeService.getAll()).thenReturn(mockEmployeeList);

        mockMvc.perform(get("/api/employees")).andExpect(status().isOk()) // Can use "andDo(print())" for a ton of helpful info
                .andExpect(content().json(mapper.writeValueAsString(mockEmployeeList)));
    }
    @Test
    public void findSingleEmployee() throws Exception {
        Employee mockEmployee = ModelFactory.getEmployee(null, null, null);
        when(employeeService.getById("abc")).thenReturn(mockEmployee);

        mockMvc.perform(get("/api/employees/abc")).andExpect((status().isOk()))
                .andExpect(content().json(mapper.writeValueAsString(mockEmployee)));
    }
    @Test
    public void unableToFindEmployee() throws Exception {
        when(employeeService.getById("123")).thenThrow(new RuntimeException());
        // If deeper inspection is needed into the Response or Request, can use MvcResult via "andReturn"
        MvcResult result = mockMvc.perform(get("/api/employees/123")).andExpect((status().isNotFound())).andReturn();
        // EVEN THOUGH, a TON is possible with the fluent api -> "andExpect" or "andExpectAll"
        assertThat(result.getResponse().getContentAsString()).isEmpty(); // BUT an "assertThat" feels so nice and simple!
    }
}
