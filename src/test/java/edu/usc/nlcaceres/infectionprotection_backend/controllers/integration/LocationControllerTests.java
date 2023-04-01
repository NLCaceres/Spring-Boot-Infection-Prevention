package edu.usc.nlcaceres.infectionprotection_backend.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.services.LocationService;
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
public class LocationControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private LocationService locationService;

    @Test
    public void findLocationList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/locations")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Location> actualList = Arrays.asList(mapper.readValue(jsonResponse, Location[].class));
        assertThat(actualList).hasSize(3);
    }
    @Test
    public void findSingleLocation() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/locations/5dc9b294cab4fa0bd0b23d48"))
                .andExpect((status().isOk()))
                .andReturn().getResponse().getContentAsString();

        Location location = mapper.readValue(jsonResponse, Location.class);
        assertThat(location.getFacilityName()).isEqualTo("USC");
        assertThat(location.getUnitNum()).isEqualTo("2");
        assertThat(location.getRoomNum()).isEqualTo("123");
    }
    @Test
    public void unableToFindLocation() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/locations/123"))
                .andExpect((status().isNotFound()))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}
