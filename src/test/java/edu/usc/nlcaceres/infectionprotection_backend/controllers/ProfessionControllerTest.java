package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.services.ProfessionService;
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

@WebMvcTest(ProfessionController.class)
public class ProfessionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ProfessionService professionService;

    @Test
    public void findProfessionList() throws Exception {
        List<Profession> mockProfessionList = ModelFactory.getProfessionList();
        when(professionService.getAll()).thenReturn(mockProfessionList);

        mockMvc.perform(get("/api/professions")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(mockProfessionList)));
    }
    @Test
    public void findSingleProfession() throws Exception {
        Profession mockProfession = ModelFactory.getProfession(null, null);
        when(professionService.getById("abc")).thenReturn(mockProfession);

        mockMvc.perform(get("/api/professions/abc")).andExpect((status().isOk()))
                .andExpect(content().json(mapper.writeValueAsString(mockProfession)));
    }
    @Test
    public void unableToFindProfession() throws Exception {
        when(professionService.getById("123")).thenThrow(new RuntimeException());
        MvcResult result = mockMvc.perform(get("/api/professions/123")).andExpect((status().isNotFound())).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
