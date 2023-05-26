package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthPracticeTests {
    @Test
    public void removeBackReferencesInParentPrecaution() {
        HealthPractice healthPractice = ModelFactory.getHealthPractice(null);
        assertThat(healthPractice.getName()).isEqualTo("Hand Hygiene"); // Sanity check that data is init properly
        assertThat(healthPractice.getPrecaution()).isNotNull();
        assertThat(healthPractice.getPrecaution().getHealthPractices()).isNotEmpty();

        // Grab the parent precaution and set its list of healthPractices to an empty one to prevent cyclic references
        healthPractice.removeBackReference();
        assertThat(healthPractice.getPrecaution().getHealthPractices()).isEmpty();
    }
}
