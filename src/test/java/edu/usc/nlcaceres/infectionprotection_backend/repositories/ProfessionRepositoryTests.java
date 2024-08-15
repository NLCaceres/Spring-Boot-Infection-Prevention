package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;

@DataMongoTest
public class ProfessionRepositoryTests {
  @Autowired
  ProfessionRepository professionRepository;

  @AfterEach
  public void resetDB() {
    professionRepository.deleteAll();
  }

  @Test
  public void findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline() throws Exception {
    Profession fooBar = Profession.of("Foo", "Bar");
    Profession fooBarfoo = Profession.of("Foo", "Barfoo");
    Profession fizzBuzz = Profession.of("Fizz", "Buzz");
    Profession fishyBuzz = Profession.of("Fishy", "Buzz");
    professionRepository.saveAll(List.of(fooBar, fooBarfoo, fizzBuzz, fishyBuzz));

    assertThat(professionRepository.count()).isEqualTo(4); // - Sanity check that DB saved test data

    // - WHEN 2 Professions share the same `observedOccupation`
    // - THEN the `serviceDiscipline` is the determining factor to find a unique document
    assertThat( // - BOTH use "Foo" BUT "Bar" is the `serviceDiscipline` needed
      professionRepository.findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Foo", "Bar")
    ).isNotNull().isEqualTo(Profession.of(fooBar.getId(), "Foo", "Bar"));
    assertThat( // - AND "Barfoo" is the `serviceDiscipline`
      professionRepository.findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Foo", "Barfoo")
    ).isNotNull().isEqualTo(Profession.of(fooBarfoo.getId(), "Foo", "Barfoo"));

    // - WHEN 2 Professions share the same `serviceDiscipline`
    // - THEN the `observedOccupation` is the determining factor to find a unique document
    assertThat( // - BOTH use "Buzz" BUT "Fizz" is the `observedOccupation` needed
      professionRepository.findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Fizz", "Buzz")
    ).isNotNull().isEqualTo(Profession.of(fizzBuzz.getId(), "Fizz", "Buzz"));
    assertThat( // - AND "Fishy" is the `observedOccupation` needed here
      professionRepository.findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Fishy", "Buzz")
    ).isNotNull().isEqualTo(Profession.of(fishyBuzz.getId(), "Fishy", "Buzz"));

    // - WHEN no matching Profession exists, THEN null is returned
    assertThat(
      professionRepository.findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Cat", "Dog")
    ).isNull();

    Profession foobarTwo = Profession.of("Foo", "Bar");
    professionRepository.save(foobarTwo);
    assertThat(fooBar).isNotEqualTo(foobarTwo);
    // - WHEN 2 Professions have the same `serviceDiscipline` and `observedOccupation`
    Profession foundFoobar =
      professionRepository.findFirstProfessionDistinctByObservedOccupationAndServiceDiscipline("Foo", "Bar");
    // - THEN the Profession created earliest is the Profession that is returned
    assertThat(foundFoobar).isNotNull().isEqualTo(fooBar);
    assertThat(foundFoobar).isNotEqualTo(foobarTwo);
  }
}