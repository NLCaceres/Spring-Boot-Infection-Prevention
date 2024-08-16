package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LocationTests {
    @Test
    public void testStaticFactory() {
        // - WHEN 4 params are set in the static factory method
        Location location = Location.of("123", "Foo", "1", "12");
        // - THEN all 4 fields of Location are correctly set
        assertThat(location.getId()).isEqualTo("123");
        assertThat(location.getFacilityName()).isEqualTo("Foo");
        assertThat(location.getUnitNum()).isEqualTo("1");
        assertThat(location.getRoomNum()).isEqualTo("12");

        // - WHEN only 3 params are set in the static factory method
        Location locationWithNullID = Location.of("Bar", "2", "213");
        // - THEN the ID field is set to null, despite @NonNull BUT this lets MongoDB generate & set the ID later
        assertThat(locationWithNullID.getId()).isNull();
        // - BUT the other main fields are set correctly
        assertThat(locationWithNullID.getFacilityName()).isEqualTo("Bar");
        assertThat(locationWithNullID.getUnitNum()).isEqualTo("2");
        assertThat(locationWithNullID.getRoomNum()).isEqualTo("213");
    }
    @Test
    public void testLombokEquals() {
        Location location = Location.of("Abc", "Facility", "Unit", "Room");
        // - WHEN only the id matches, THEN the locations are NOT equal
        Location otherLocation = Location.of("Abc", "facility", "unit", "room");
        assertThat(location.equals(otherLocation)).isFalse();

        // - WHEN only the id and facility names matches, THEN the locations are NOT equal
        Location anotherLocation = Location.of("Abc", "Facility", "unit", "room");
        assertThat(location.equals(anotherLocation)).isFalse();

        // - WHEN all field values matches, THEN the locations are equal
        Location finalLocation = Location.of("Abc", "Facility", "Unit", "Room");
        assertThat(location.equals(finalLocation)).isTrue();
    }
    @Test
    public void testLombokToString() {
        Location location = ModelFactory.getLocation(null, null, null);
        assertThat(location.toString()).isEqualTo("Location(id=abc, facilityName=USC, unitNum=2, roomNum=123)");
    }
}
