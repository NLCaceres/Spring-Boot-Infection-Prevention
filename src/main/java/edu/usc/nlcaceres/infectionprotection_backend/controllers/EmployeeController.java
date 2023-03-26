package edu.usc.nlcaceres.infectionprotection_backend.controllers;

import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import edu.usc.nlcaceres.infectionprotection_backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController @RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.getAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable String id) { // Can throw due to employeeService throwing NoSuchElementException
        try { return new ResponseEntity<>(employeeService.getById(id), HttpStatus.OK); }
        catch (Exception error) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    }
}
