package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@SpringBootTest
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @MockBean
    private LocationRepository locationRepository;

    @Test
    public void findLocationList() throws Exception {
        List<Location> mockLocationList = ModelFactory.getLocationList();
        when(locationRepository.findAll()).thenReturn(mockLocationList);

        List<Location> locationList = locationService.getAll();
        assertThat(locationService).isNotNull();

        assertThat(locationList).isNotEmpty();
        assertThat(locationList.size()).isEqualTo(2);
    }
    @Test
    public void findSingleLocation() throws Exception {
        Location mockLocation = ModelFactory.getLocation(null, null, null);
        when(locationRepository.findById("abc")).thenReturn(Optional.of(mockLocation));

        Location location = locationService.getById("abc");
        assertThat(location).isNotNull();
        assertThat(location.getFacilityName()).isEqualTo("USC");
        assertThat(location.getUnitNum()).isEqualTo("2");
        assertThat(location.getRoomNum()).isEqualTo("123");
    }
    @Test
    public void unableToFindLocation() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            Location location = locationService.getById("abc");
        });
    }
}
