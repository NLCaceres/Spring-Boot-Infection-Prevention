package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.LocationController;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.services.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@WebMvcTest(LocationController.class)
public class LocationControllerTests {

    @Autowired
    private LocationController locationController;

    @MockBean
    private LocationService locationService;

    @Test
    public void findLocationList() throws Exception {
        List<Location> mockLocationList = ModelFactory.getLocationList();
        when(locationService.getAll()).thenReturn(mockLocationList);

        List<Location> actualList = locationController.getAll();
        assertThat(actualList).isEqualTo(mockLocationList);
    }
    @Test
    public void findSingleLocation() throws Exception {
        Location mockLocation = ModelFactory.getLocation(null, null, null);
        when(locationService.getById("abc")).thenReturn(mockLocation);

        Location actualLocation = locationController.getById("abc").getBody();
        assertThat(actualLocation).isEqualTo(mockLocation);
    }
    @Test
    public void unableToFindLocation() throws Exception {
        when(locationService.getById("123")).thenThrow(new RuntimeException());

        Location actualLocation = locationController.getById("123").getBody();
        assertThat(actualLocation).isNull();
    }
}
