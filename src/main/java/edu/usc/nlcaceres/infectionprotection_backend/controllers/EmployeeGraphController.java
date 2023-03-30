package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class EmployeeGraphController {
    @Autowired
    EmployeeService employeeService;

    @QueryMapping
    public List<Employee> employees() {
        return employeeService.getAll();
    }
    @QueryMapping
    public Employee employeeById(@Argument String id) {
        try { return employeeService.getById(id); }
        catch (Exception error) { return null; }
    } // Even with GraphQL, it is STILL important to catch errors during the query/route because GraphQL only understands its own errors
    // Any errors you throw or fail to catch will be written as Internal errors to the Client
}
