package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PrecautionTests {
    @Test
    public void removeBackReferenceInChildHealthPractices() {
        Precaution precaution = ModelFactory.getPrecaution(null);
        assertThat(precaution.getName()).isEqualTo("Isolation"); // Sanity Checks that data is init as expected
        assertThat(precaution.getHealthPractices()).isNotEmpty();
        precaution.getHealthPractices().forEach(healthPractice -> assertThat(healthPractice.getPrecaution()).isNotNull());

        // Grab each child healthPractice and set their precaution back reference to null to prevent cycles
        precaution.removeBackReference();
        precaution.getHealthPractices().forEach(healthPractice -> assertThat(healthPractice.getPrecaution()).isNull());
    }
}
