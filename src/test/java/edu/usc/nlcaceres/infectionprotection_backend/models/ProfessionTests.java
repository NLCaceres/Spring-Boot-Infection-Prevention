package edu.usc.nlcaceres.infectionprotection_backend.models;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfessionTests {
    @Test
    public void testStaticFactory() {
        // - WHEN 3 params are set in the static factory method
        Profession profession = Profession.of("123", "Foo", "Bar");
        // - THEN all 3 fields of Profession are correctly set
        assertThat(profession.getId()).isEqualTo("123");
        assertThat(profession.getObservedOccupation()).isEqualTo("Foo");
        assertThat(profession.getServiceDiscipline()).isEqualTo("Bar");

        // - WHEN only 2 params are set in the static factory method
        Profession professionWithNullID = Profession.of("Barfoo", "Foobar");
        // - THEN the ID field is set to null, despite @NonNull BUT this lets MongoDB generate & set the ID later
        assertThat(professionWithNullID.getId()).isNull();
        // - BUT the other main fields are set correctly
        assertThat(professionWithNullID.getObservedOccupation()).isEqualTo("Barfoo");
        assertThat(professionWithNullID.getServiceDiscipline()).isEqualTo("Foobar");
    }
    @Test
    public void testLombokEquals() {
        Profession profession = Profession.of("Foo", "Bar", "Fizz");
        // - WHEN only the id matches, THEN the Professions are NOT equal
        Profession otherProfession = Profession.of("Foo", "Ba", "Fiz");
        assertThat(profession.equals(otherProfession)).isFalse();

        Profession anotherProfession = Profession.of("Foo", "Bar", "Fiz");
        // - WHEN only the id and observed occupation fields' values match, THEN the Professions are NOT equal
        assertThat(profession.equals(anotherProfession)).isFalse();

        Profession finalProfession = Profession.of("Foo", "Bar", "Fizz");
        // - WHEN all field values match, THEN the Professions are equal
        assertThat(profession.equals(finalProfession)).isTrue();
    }
    @Test
    public void testLombokToString() {
        Profession profession = ModelFactory.getProfession(null, null);
        assertThat(profession.toString()).isEqualTo("Profession(id=abc, observedOccupation=Clinic, serviceDiscipline=Doctor)");
    }
}
