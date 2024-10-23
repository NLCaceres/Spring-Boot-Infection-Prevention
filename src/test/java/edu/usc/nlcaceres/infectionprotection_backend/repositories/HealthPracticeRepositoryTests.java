package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;

@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=7.0.12")
public class HealthPracticeRepositoryTests {
  @Autowired
  HealthPracticeRepository healthPracticeRepository;

  @AfterEach
  public void resetDB() {
    healthPracticeRepository.deleteAll();
  }

  @Test
  public void findHealthPracticeByName() throws Exception {
    HealthPractice foo = HealthPractice.of("Foo", Precaution.of("Bar"));
    HealthPractice fizz = HealthPractice.of("Fizz", Precaution.of("Buzz"));
    healthPracticeRepository.saveAll(List.of(foo, fizz));
    // - Sanity check that both HealthPractices are saved
    assertThat(healthPracticeRepository.count()).isEqualTo(2);

    // - WHEN no HealthPractice is found with a matching name, THEN `null` is returned
    assertThat(healthPracticeRepository.findByName("Fiz")).isNull();

    // - WHEN a document with a matching `name` exists, THEN `null` is NOT returned
    assertThat(healthPracticeRepository.findByName("Foo")).isNotNull();
    // - AND the matching document is returned
    assertThat(healthPracticeRepository.findByName("Foo").getName()).isEqualTo("Foo");
    assertThat(healthPracticeRepository.findByName("Foo").getId()).isNotNull();

    // - WHEN HealthPractices with matching names exist
    healthPracticeRepository.save(HealthPractice.of("Foo", Precaution.of("Bar")));
    // - THEN the query throws
    assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
      healthPracticeRepository.findByName("Foo");
    });
  }
}
