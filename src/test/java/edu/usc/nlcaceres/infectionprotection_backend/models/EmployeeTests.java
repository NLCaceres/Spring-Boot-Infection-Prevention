package edu.usc.nlcaceres.infectionprotection_backend.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTests {
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
