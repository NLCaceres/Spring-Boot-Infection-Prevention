package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.services.ReportService;
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

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ReportService reportService;

    @Test
    public void findReportList() throws Exception {
        List<Report> mockReportList = ModelFactory.getReportList();
        when(reportService.getAll()).thenReturn(mockReportList);

        mockMvc.perform(get("/api/reports")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(mockReportList)));
    }
    @Test
    public void findSingleReport() throws Exception {
        Report mockReport = ModelFactory.getReport();
        when(reportService.getById("abc")).thenReturn(mockReport);

        mockMvc.perform(get("/api/reports/abc")).andExpect((status().isOk()))
                .andExpect(content().json(mapper.writeValueAsString(mockReport)));
    }
    @Test
    public void unableToFindReport() throws Exception {
        when(reportService.getById("123")).thenThrow(new RuntimeException());
        MvcResult result = mockMvc.perform(get("/api/reports/123")).andExpect((status().isNotFound())).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
