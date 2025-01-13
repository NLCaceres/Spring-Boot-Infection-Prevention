package edu.usc.nlcaceres.infectionprotection_backend.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
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
public class PrecautionControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PrecautionService precautionService;

    @Test
    public void findPrecautionList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/precautions")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Precaution> actualList = Arrays.asList(mapper.readValue(jsonResponse, Precaution[].class));
        assertThat(actualList).hasSize(2);
        // Backrefs to the original Precautions are now null since the Response does not include a backref in the HealthPractice key
        actualList.forEach(precaution -> {
            precaution.getHealthPractices().forEach(healthPractice -> assertThat(healthPractice.getPrecaution()).isNull());
        });
    }
    @Test
    public void findSinglePrecaution() throws Exception {
        String jsonPrecautions = mockMvc.perform(get("/api/precautions"))
            .andReturn().getResponse().getContentAsString();
        Precaution firstPrecaution = mapper.readValue(jsonPrecautions, Precaution[].class)[0];
        String jsonResponse = mockMvc.perform(get("/api/precautions/" + firstPrecaution.getId()))
                .andExpect((status().isOk()))
                .andReturn().getResponse().getContentAsString();

        Precaution precaution = mapper.readValue(jsonResponse, Precaution.class);
        assertThat(precaution.getName()).isEqualTo(firstPrecaution.getName());
        // Backref to this Precaution now null since no backref in HealthPractice key
        precaution.getHealthPractices().forEach(healthPractice -> assertThat(healthPractice.getPrecaution()).isNull());
    }
    @Test
    public void unableToFindPrecaution() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/precautions/123"))
                .andExpect((status().isNotFound()))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
