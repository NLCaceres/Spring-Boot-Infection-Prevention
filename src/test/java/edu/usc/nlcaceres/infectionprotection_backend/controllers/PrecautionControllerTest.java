package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
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

@WebMvcTest(PrecautionController.class)
public class PrecautionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    PrecautionService precautionService;

    @Test
    public void findPrecautionList() throws Exception {
        List<Precaution> mockPrecautionList = ModelFactory.getPrecautionList();
        when(precautionService.getAll()).thenReturn(mockPrecautionList);

        mockMvc.perform(get("/api/precautions")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(mockPrecautionList)));
    }
    @Test
    public void findSinglePrecaution() throws Exception {
        Precaution mockPrecaution = ModelFactory.getPrecaution(null);
        when(precautionService.getById("abc")).thenReturn(mockPrecaution);

        mockMvc.perform(get("/api/precautions/abc")).andExpect((status().isOk()))
                .andExpect(content().json(mapper.writeValueAsString(mockPrecaution)));
    }
    @Test
    public void unableToFindPrecaution() throws Exception {
        when(precautionService.getById("123")).thenThrow(new RuntimeException());
        MvcResult result = mockMvc.perform(get("/api/precautions/123")).andExpect((status().isNotFound())).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
