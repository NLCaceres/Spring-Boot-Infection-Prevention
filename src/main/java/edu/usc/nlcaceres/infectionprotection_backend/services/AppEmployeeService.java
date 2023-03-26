package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppEmployeeService implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(String id) {
        return employeeRepository.findById(id).orElseThrow(); // Throws NoSuchElementException which is UNCHECKED!
    } // Descendants of RuntimeException and Error are NOT required to be marked with "throws"
    // Consequently, if this does throw and isn't handled then the server prints error message

    @Override
    public String save(Employee employee) {
        return employeeRepository.save(employee).getId(); //* Return ID of saved employee to controller to provide in response
    }

    @Override
    public void delete(String id) {
        employeeRepository.deleteById(id);
    }
}
