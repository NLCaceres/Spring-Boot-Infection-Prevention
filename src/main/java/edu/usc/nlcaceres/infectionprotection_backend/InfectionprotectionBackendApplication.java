package edu.usc.nlcaceres.infectionprotection_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication conveniently adds in @Configuration, @EnableAutoConfiguration and @ComponentScan
// @Configuration declares this class the source of 'bean' definitions for the app context
// While @EnableAutoConfig starts adding those beans on the classpath as well as property settings to activate related behavior
// @ComponentScan searches all files at the same level as this Application class and any subpackages
@SpringBootApplication
public class InfectionprotectionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfectionprotectionBackendApplication.class, args);
	}

}
