package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;

@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=7.0.12")
public class LocationRepositoryTests {
  @Autowired
  LocationRepository locationRepository;

  @AfterEach
  public void resetDB() {
    locationRepository.deleteAll();
  }

  @Test
  public void findLocationByFacilityNameAndUnitNumAndRoomNum() {
    Location foo = Location.of("Foo", "1", "123");
    Location foo2 = Location.of("Foo", "2", "234");
    Location bar = Location.of("Bar", "1", "345");
    Location fizz = Location.of("Fizz", "3", "123");
    locationRepository.saveAll(List.of(foo, foo2, bar, fizz));
    assertThat(locationRepository.count()).isEqualTo(4);

    // - WHEN a Location only matches one or two of the fields, THEN `null` is returned
    assertThat(locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Foo", "4", "321")).isNull();;
    assertThat(locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Foo", "1", "321")).isNull();;
    
    // - WHEN the Location matches all 3 fields, THEN `null` is NOT returned, a matching document is returned
    Location foundLocation = locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Foo", "1", "123");
    assertThat(foundLocation).isNotNull();
    assertThat(foundLocation.getId()).isNotNull();
    assertThat(foundLocation.getFacilityName()).isEqualTo("Foo");
    assertThat(foundLocation.getUnitNum()).isEqualTo("1");
    assertThat(foundLocation.getRoomNum()).isEqualTo("123");

    // - WHEN Locations have the same `facilityName`, THEN ONLY the Location that matches all 3 fields is returned
    Location foundFacility = locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Foo", "2", "234");
    assertThat(foundFacility).isNotNull();
    assertThat(foundFacility.getFacilityName()).isEqualTo("Foo");
    assertThat(foundFacility.getUnitNum()).isEqualTo("2");
    assertThat(foundFacility.getRoomNum()).isEqualTo("234");

    // - WHEN Locations have the same `unitNum`, THEN ONLY the Location that matches all 3 fields is returned
    Location foundUnit = locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Bar", "1", "345");
    assertThat(foundUnit).isNotNull();
    assertThat(foundUnit.getFacilityName()).isEqualTo("Bar");
    assertThat(foundUnit.getUnitNum()).isEqualTo("1");
    assertThat(foundUnit.getRoomNum()).isEqualTo("345");

    // - WHEN Locations have the same `roomNum`, THEN ONLY the Location that matches all 3 fields is returned
    Location foundRoom = locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Fizz", "3", "123");
    assertThat(foundRoom).isNotNull();
    assertThat(foundRoom.getFacilityName()).isEqualTo("Fizz");
    assertThat(foundRoom.getUnitNum()).isEqualTo("3");
    assertThat(foundRoom.getRoomNum()).isEqualTo("123");

    // - WHEN multiple Locations match all 3 fields, THEN the query will throw
    locationRepository.save(Location.of("Foo", "1", "123"));
    assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
      locationRepository.findByFacilityNameAndUnitNumAndRoomNum("Foo", "1", "123");
    });
  }
}
