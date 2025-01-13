package edu.usc.nlcaceres.infectionprotection_backend.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.services.ProfessionService;
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
public class ProfessionControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ProfessionService professionService;

    @Test
    public void findProfessionList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/professions")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Profession> actualList = Arrays.asList(mapper.readValue(jsonResponse, Profession[].class));
        assertThat(actualList).hasSize(2);
    }
    @Test
    public void findSingleProfession() throws Exception {
        String jsonProfessions = mockMvc.perform(get("/api/professions"))
            .andReturn().getResponse().getContentAsString();
        Profession firstProfession = mapper.readValue(jsonProfessions, Profession[].class)[0];
        String jsonResponse = mockMvc.perform(get("/api/professions/" + firstProfession.getId()))
                .andExpect((status().isOk()))
                .andReturn().getResponse().getContentAsString();

        Profession profession = mapper.readValue(jsonResponse, Profession.class);
        assertThat(profession.getObservedOccupation()).isEqualTo(firstProfession.getObservedOccupation());
        assertThat(profession.getServiceDiscipline()).isEqualTo(firstProfession.getServiceDiscipline());
    }
    @Test
    public void unableToFindProfession() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/professions/123"))
                .andExpect((status().isNotFound()))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
