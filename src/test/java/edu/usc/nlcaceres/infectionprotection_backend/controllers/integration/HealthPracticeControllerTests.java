package edu.usc.nlcaceres.infectionprotection_backend.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.services.HealthPracticeService;
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
public class HealthPracticeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private HealthPracticeService healthPracticeService;

    @Test
    public void findHealthPracticeList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/healthPractices")).andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        List<HealthPractice> actualList = Arrays.asList(mapper.readValue(jsonResponse, HealthPractice[].class));
        assertThat(actualList).hasSize(6);
        actualList.forEach(healthPractice -> assertThat(healthPractice.getPrecaution().getHealthPractices()).isEmpty());
    }
    @Test
    public void findSingleHealthPractice() throws Exception {
        String jsonHealthPractices = mockMvc.perform(get("/api/healthPractices"))
            .andReturn().getResponse().getContentAsString();
        HealthPractice firstHealthPractice = mapper.readValue(jsonHealthPractices, HealthPractice[].class)[0];
        String jsonResponse = mockMvc.perform(get("/api/healthPractices/" + firstHealthPractice.getId()))
                .andExpect((status().isOk()))
                .andReturn().getResponse().getContentAsString();

        HealthPractice healthPractice = mapper.readValue(jsonResponse, HealthPractice.class);
        assertThat(healthPractice.getName()).isEqualTo(firstHealthPractice.getName());
        assertThat(healthPractice.getPrecaution().getHealthPractices()).isEmpty();
    }
    @Test
    public void unableToFindHealthPractice() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/healthPractices/123"))
                .andExpect((status().isNotFound()))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
