package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LocationTests {
    @Test
    public void testLombokEquals() {
        Location location = Location.of("Abc", "Facility", "Unit", "Room");
        //* WHEN only the id matches, THEN the locations are NOT equal */
        Location otherLocation = Location.of("Abc", "facility", "unit", "room");
        assertThat(location.equals(otherLocation)).isFalse();

        //* WHEN only the id and facility names matches, THEN the locations are NOT equal */
        Location anotherLocation = Location.of("Abc", "Facility", "unit", "room");
        assertThat(location.equals(anotherLocation)).isFalse();

        //* WHEN all field values matches, THEN the locations are equal */
        Location finalLocation = Location.of("Abc", "Facility", "Unit", "Room");
        assertThat(location.equals(finalLocation)).isTrue();
    }
    @Test
    public void testLombokToString() {
        Location location = ModelFactory.getLocation(null, null, null);
        assertThat(location.toString()).isEqualTo("Location(id=abc, facilityName=USC, unitNum=2, roomNum=123)");
    }
}
