package edu.usc.nlcaceres.infectionprotection_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//? @SpringBootApplication conveniently adds in @Configuration, @EnableAutoConfiguration and @ComponentScan
//? @Configuration declares this class the source of 'bean' definitions for the app context
//? While @EnableAutoConfig starts adding those beans on the classpath as well as property settings to activate related behavior
//? @ComponentScan searches all files at the same level as this Application class and any subpackages
@SpringBootApplication
public class InfectionprotectionBackendApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(InfectionprotectionBackendApplication.class, args);
  }

  @Override //? Alternatively a CommandLineRunner Bean method can be defined to run on start (and even in a particular order)
  public void run(String ...args) throws Exception {
    System.out.println("Running the command line runner");
    // Can check if the Mongo Collections contain any data. If they don't, then begin seeding
  }
}
