package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;

//* VERY important to check HealthPractice and Precaution equals() and toString() because of their cyclic references */
public class PrecautionTests {
    @Test
    public void testStaticFactory() {
        // - WHEN 1 param is set in the static factory method
        Precaution precaution = Precaution.of("Foo");
        // - THEN the Precaution ID is null, but its name is set to the param value with an empty HealthPractice list
        assertThat(precaution.getId()).isEqualTo(null);
        assertThat(precaution.getName()).isEqualTo("Foo");
        assertThat(precaution.getHealthPractices()).isEqualTo(new ArrayList<>());

        // - WHEN 2 String params are set
        Precaution anotherPrecaution = Precaution.of("123", "Bar");
        // - THEN the Precaution ID AND name are set respectively, but with an empty HealthPractice list still
        assertThat(anotherPrecaution.getId()).isEqualTo("123");
        assertThat(anotherPrecaution.getName()).isEqualTo("Bar");
        assertThat(anotherPrecaution.getHealthPractices()).isEqualTo(new ArrayList<>());

        // - WHEN 2 params are set but 1 is a list
        Precaution finalPrecaution = Precaution.of("Fizz", List.of(HealthPractice.of("321", "Buzz")));
        // - THEN the Precaution ID is null, but name AND list are BOTH set respectively
        assertThat(finalPrecaution.getId()).isEqualTo(null);
        assertThat(finalPrecaution.getName()).isEqualTo("Fizz");
        assertThat(finalPrecaution.getHealthPractices().size()).isEqualTo(1);
        assertThat(finalPrecaution.getHealthPractices().get(0).getId()).isEqualTo("321");
        assertThat(finalPrecaution.getHealthPractices().get(0).getName()).isEqualTo("Buzz");
    }
    @Test
    public void testEquals() {
        Precaution precaution = ModelFactory.getPrecaution(null);
        Precaution other = Precaution.of("abc", "Foo");
        //* WHEN only the IDs match, THEN the Precautions are NOT equal */
        assertThat(precaution.equals(other)).isFalse();

        Precaution another = Precaution.of("abc", "Isolation");
        //* WHEN the IDs and names match but not the list of health practices (size included), THEN  the Precautions are NOT equal */
        assertThat(precaution.equals(another)).isFalse();

        Precaution fullMatch = Precaution.of("abc", "Isolation");
        fullMatch.setHealthPractices(List.of(HealthPractice.of("abc", "Hand Hygiene"), HealthPractice.of("abc", "PPE")));
        //* WHEN all fields match including the HealthPractice's BUT not the Precaution back-ref, THEN the Precautions are NOT equal */
        assertThat(precaution.equals(fullMatch)).isFalse();

        //* WHEN all fields match + the HealthPractice and its back-ref to the Precaution (specifically the ID and name), THEN Precautions are equal */
        fullMatch.getHealthPractices().forEach(healthPractice -> healthPractice.setPrecaution(Precaution.of("abc", "Standard")));
        assertThat(precaution.equals(fullMatch)).isTrue();

        //* Order matters for Lists. Even if the HealthPractices are identical, incorrect order will cause equals to return false */
        fullMatch.setHealthPractices(List.of(HealthPractice.of("abc", "PPE"), HealthPractice.of("abc", "Hand Hygiene")));
        assertThat(precaution.equals(fullMatch)).isFalse();
    }
    @Test
    public void testToString() {
        Precaution precaution = ModelFactory.getPrecaution(null);
        //* Sanity checks that the data is init as expected */
        assertThat(precaution.getId()).isEqualTo("abc");
        assertThat(precaution.getName()).isEqualTo("Isolation");
        //* HealthPractices shouldn't be null and at least an empty list. ALSO, the backRef to the precaution should also be non-null */
        assertThat(precaution.getHealthPractices()).hasSize(2);
        precaution.getHealthPractices().forEach(healthPractice -> assertThat(healthPractice.getPrecaution()).isNotNull());
        assertThat(precaution.getHealthPractices().get(0).getName()).isEqualTo("Hand Hygiene");
        assertThat(precaution.getHealthPractices().get(1).getName()).isEqualTo("PPE");

        //* toString() should return the values for id, name and every health practice's name in a list */
        assertThat(precaution.toString()).isEqualTo("Precaution(id=abc, name=Isolation, healthPractices=[Hand Hygiene, PPE])");

        Precaution other = Precaution.of("abc", "Isolation");
        //* WHEN there are no health practices in the list, THEN the health practices list is empty in toString() */
        assertThat(other.toString()).isEqualTo("Precaution(id=abc, name=Isolation, healthPractices=[])");
    }
}
