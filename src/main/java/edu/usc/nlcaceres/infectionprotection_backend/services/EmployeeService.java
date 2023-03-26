package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import java.util.List;

// This service will be provided to a Controller, allowing the Service to encapsulate the Repository's query options
// This ensures the Controller can ONLY do what we want it to do.
public interface EmployeeService {

    List<Employee> getAll();
    Employee getById(String id);
    String save(Employee employee);
    void delete(String id);

}
