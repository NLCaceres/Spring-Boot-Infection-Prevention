package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

//* VERY important to check HealthPractice and Precaution equals() and toString() because of their cyclic references */
//* If used Lombok to generate those two funcs, then those cyclic refs would cause overflows */
public class HealthPracticeTests {
    @Test
    public void testEquals() {
        HealthPractice healthPractice = ModelFactory.getHealthPractice(null);
        HealthPractice other = new HealthPractice("abc", "Foo", Precaution.of("def", "Standard"));
        //* WHEN the other healthPractice only matches in ID, THEN equals returns false */
        assertThat(healthPractice.equals(other)).isFalse();
        
        HealthPractice another = new HealthPractice("abc", "Hand Hygiene", Precaution.of("def", "Standard"));
        //* WHEN the other healthPractice only matches in ID and name but not precaution, THEN equals returns false */
        assertThat(healthPractice.equals(another)).isFalse();
        
        HealthPractice fullMatch = new HealthPractice("abc", "Hand Hygiene", Precaution.of("abc", "Standard"));
        //* WHEN the other healthPractice fully matches (including the Precaution id and name), THEN equals returns true */
        assertThat(healthPractice.equals(fullMatch)).isTrue();
    }
    @Test
    public void testToString() {
        HealthPractice healthPractice = ModelFactory.getHealthPractice(null);
        //* Sanity check that data is init properly */
        assertThat(healthPractice.getId()).isEqualTo("abc");
        assertThat(healthPractice.getName()).isEqualTo("Hand Hygiene");
        //* Since the precaution prop is nullable, it's best for the linter to grab it via the getter and initialize a new var */
        Precaution precaution = healthPractice.getPrecaution();
        assertThat(precaution).isNotNull(); //? AND because the linter doesn't recognize this line,
        if (precaution != null) { //? An actual null check is still required
            assertThat(precaution.getName()).isEqualTo("Standard");
            assertThat(precaution.getHealthPractices()).isNotEmpty(); //* The backRef to this healthPractice is NOT NULL */
        }

        //* toString() should return the values for ID, name and the parent precaution's name (as precautionType) */
        assertThat(healthPractice.toString()).isEqualTo("HealthPractice(id=abc, name=Hand Hygiene, precautionType=Standard)");
    }
}
