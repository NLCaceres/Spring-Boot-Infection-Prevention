package edu.usc.nlcaceres.infectionprotection_backend.repositories;

import edu.usc.nlcaceres.infectionprotection_backend.models.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// @Repository ensures @ComponentScan can find this BUT @SpringBootApp auto adds @EnableMongoRepositories to do the same
// On the other hand, @RepositoryRestResource would set the collection name and endpoint path which is done in the Models and Controllers instead
@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> { // <Employee Document, ID String>

}
