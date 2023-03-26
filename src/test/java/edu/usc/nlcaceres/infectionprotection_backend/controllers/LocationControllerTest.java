package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.services.LocationService;
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

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    LocationService locationService;

    @Test
    public void findLocationList() throws Exception {
        List<Location> mockLocationList = ModelFactory.getLocationList();
        when(locationService.getAll()).thenReturn(mockLocationList);

        mockMvc.perform(get("/api/locations")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(mockLocationList)));
    }
    @Test
    public void findSingleLocation() throws Exception {
        Location mockLocation = ModelFactory.getLocation(null, null, null);
        when(locationService.getById("abc")).thenReturn(mockLocation);

        mockMvc.perform(get("/api/locations/abc")).andExpect((status().isOk()))
                .andExpect(content().json(mapper.writeValueAsString(mockLocation)));
    }
    @Test
    public void unableToFindLocation() throws Exception {
        when(locationService.getById("123")).thenThrow(new RuntimeException());
        MvcResult result = mockMvc.perform(get("/api/locations/123")).andExpect((status().isNotFound())).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
