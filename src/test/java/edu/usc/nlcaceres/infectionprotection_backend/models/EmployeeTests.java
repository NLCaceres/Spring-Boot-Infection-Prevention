package edu.usc.nlcaceres.infectionprotection_backend.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTests {
    @Test
    public void testStaticFactory() {
        Profession profession = Profession.of("Barfoo", "Foobar");
        // - WHEN 4 params are set in the static factory method
        Employee employee = Employee.of("123", "Foo", "Bar", profession);
        // - THEN all 4 fields of Employee are correctly set
        assertThat(employee.getId()).isEqualTo("123");
        assertThat(employee.getFirstName()).isEqualTo("Foo");
        assertThat(employee.getSurname()).isEqualTo("Bar");
        assertThat(employee.getProfession()).isEqualTo(Profession.of("Barfoo", "Foobar"));

        // - WHEN only 3 params are set in the static factory method
        Employee employeeWithNullID = Employee.of("Fizz", "Buzz", profession);
        // - THEN the ID field is set to null, despite @NonNull
        assertThat(employeeWithNullID.getId()).isNull();
        // - BUT the other main fields are set correctly
        assertThat(employeeWithNullID.getFirstName()).isEqualTo("Fizz");
        assertThat(employeeWithNullID.getSurname()).isEqualTo("Buzz");
        assertThat(employeeWithNullID.getProfession()).isEqualTo(Profession.of("Barfoo", "Foobar"));
    }
    @Test
    public void testLombokEquals() {
        Profession profession = Profession.of("Foo", "Bar", "Fizz");
        Employee employee = Employee.of("Foo", "Bar", "Fizz", profession);
        
        Profession otherProfession = Profession.of("foo", "Bar", "Fizz");
        assertThat(profession.equals(otherProfession)).isFalse(); //* Sanity check that professions DON'T equal */
        Employee otherEmployee = Employee.of("Foo", "Ba", "Fiz", otherProfession);
        //* WHEN only the ids match, THEN the employees are NOT equal */
        assertThat(employee.equals(otherEmployee)).isFalse();

        Employee anotherEmployee = Employee.of("Foo", "Bar", "Fiz", otherProfession);
        //* WHEN only the ids and firstName match, THEN the employees are NOT equal */
        assertThat(employee.equals(anotherEmployee)).isFalse();

        Employee finalEmployee = Employee.of("Foo", "Bar", "Fizz", otherProfession);
        //* WHEN all fields EXCEPT the Profession match, THEN the employees are NOT equal */
        assertThat(employee.equals(finalEmployee)).isFalse();

        //* WHEN all fields match, THEN the employees are equal */
        assertThat(employee.equals(Employee.of("Foo", "Bar", "Fizz", profession))).isTrue();
    }
    @Test
    public void testLombokToString() {
        Profession profession = Profession.of("Foo", "Bar", "Fizz");
        Employee employee = Employee.of("Foo", "Bar", "Fizz", profession);
        assertThat(employee.toString()).isEqualTo("Employee(id=Foo, firstName=Bar, surname=Fizz, " +
            "profession=Profession(id=Foo, observedOccupation=Bar, serviceDiscipline=Fizz))");
    }
}
