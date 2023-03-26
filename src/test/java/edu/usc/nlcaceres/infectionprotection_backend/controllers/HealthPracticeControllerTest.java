package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.services.HealthPracticeService;
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

@WebMvcTest(HealthPracticeController.class)
public class HealthPracticeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    HealthPracticeService healthPracticeService;

    @Test
    public void findHealthPracticeList() throws Exception {
        List<HealthPractice> mockHealthPracticeList = ModelFactory.getHealthPracticeList();
        when(healthPracticeService.getAll()).thenReturn(mockHealthPracticeList);

        mockMvc.perform(get("/api/healthPractices")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(mockHealthPracticeList)));
    }
    @Test
    public void findSingleHealthPractice() throws Exception {
        HealthPractice mockHealthPractice = ModelFactory.getHealthPractice(null);
        when(healthPracticeService.getById("abc")).thenReturn(mockHealthPractice);

        mockMvc.perform(get("/api/healthPractices/abc")).andExpect((status().isOk()))
                .andExpect(content().json(mapper.writeValueAsString(mockHealthPractice)));
    }
    @Test
    public void unableToFindHealthPractice() throws Exception {
        when(healthPracticeService.getById("123")).thenThrow(new RuntimeException());
        MvcResult result = mockMvc.perform(get("/api/healthPractices/123")).andExpect((status().isNotFound())).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
