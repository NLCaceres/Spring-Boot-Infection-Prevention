package edu.usc.nlcaceres.infectionprotection_backend.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.services.ReportService;
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

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ReportService reportService;

    @Test
    public void findReportList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/reports")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Report> actualList = Arrays.asList(mapper.readValue(jsonResponse, Report[].class));
        assertThat(actualList).hasSize(5);
        // Backrefs to the Report's HealthPractice are now null (since not included in the Response JSON)
        actualList.forEach(report -> assertThat(report.getHealthPractice().getPrecaution().getHealthPractices()).isEmpty());
    }
    @Test
    public void findSingleReport() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/reports/5dc9b295cab4fa0bd0b23d58"))
                .andExpect((status().isOk()))
                .andReturn().getResponse().getContentAsString();

        Report report = mapper.readValue(jsonResponse, Report.class);

        assertThat(report.getEmployee().getFirstName()).isEqualTo("John");
        assertThat(report.getEmployee().getSurname()).isEqualTo("Smith");

        assertThat(report.getLocation().getFacilityName()).isEqualTo("HSC");
        assertThat(report.getLocation().getUnitNum()).isEqualTo("5");
        assertThat(report.getLocation().getRoomNum()).isEqualTo("123");

        assertThat(report.getHealthPractice().getName()).isEqualTo("Hand Hygiene");
        // Backrefs to the Report's HealthPractice are now null (since not included in the Response JSON)
        assertThat(report.getHealthPractice().getPrecaution().getHealthPractices()).isEmpty();
    }
    @Test
    public void unableToFindReport() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/reports/123"))
                .andExpect((status().isNotFound()))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
