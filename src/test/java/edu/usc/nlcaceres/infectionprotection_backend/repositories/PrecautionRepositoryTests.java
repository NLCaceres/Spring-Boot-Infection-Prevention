package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;

@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=7.0.12")
public class PrecautionRepositoryTests {
  @Autowired
  PrecautionRepository precautionRepository;

  @AfterEach
  public void resetDB() {
    precautionRepository.deleteAll();
  }

  @Test
  public void findPrecautionByName() throws Exception {
    // - SETUP
    Precaution fooPrecaution = Precaution.of("Foo");
    Precaution barPrecaution = Precaution.of("Bar");
    precautionRepository.saveAll(List.of(fooPrecaution, barPrecaution));
    assertThat(precautionRepository.count()).isEqualTo(2);

    // - WHEN querying by name and a document with a matching name is found
    Precaution foundPrecaution = precautionRepository.findByName("Foo");
    // - THEN the returned Precaution is not null AND has the corresponding name
    assertThat(foundPrecaution).isNotNull();
    assertThat(foundPrecaution.getName()).isEqualTo("Foo");

    // - WHEN querying by name and no document is found with a matching name
    Precaution unfoundPrecaution = precautionRepository.findByName("Fizz");
    // - THEN the return is simply `null`
    assertThat(unfoundPrecaution).isNull();

    precautionRepository.saveAll(List.of(Precaution.of("Foo")));
    // - Sanity check that the above duplicate was saved
    assertThat(precautionRepository.count()).isEqualTo(3);

    // - WHEN duplicate documents exist with the same name
    assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
      // - THEN querying by that name will throw this Exception
      precautionRepository.findByName("Foo");
    });
  }
}
