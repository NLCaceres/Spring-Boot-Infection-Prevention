package edu.usc.nlcaceres.infectionprotection_backend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import io.mongock.driver.api.driver.ConnectionDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;

@Configuration
public class MongockConfig {

  // ?: This Bean lets me combine `application.properties`/`properties.yaml` with this Builder's config
  @Bean // ?: Alternatively, if no `application.properties` needed, I could use the MongockApplicationRunner Bean
  public MongockInitializingBeanRunner mongockRunner(ConnectionDriver driver, ApplicationContext context) {
    return MongockSpringboot.builder()
      .setDriver(driver).setSpringContext(context).setDefaultAuthor("NLCaceres")
      .addMigrationScanPackage("edu.usc.nlcaceres.infectionprotection_backend.migrations")
      .buildInitializingBeanRunner();
  }

  // ?: This TransactionManager is needed/responsible for running the ChangeUnits in the transaction scope
  @Bean // ?: It also seems to be newly required for Mongo-SpringData-V4
  public MongoTransactionManager transactionManager(MongoTemplate mongoTemplate) {
      TransactionOptions transactionalOptions = TransactionOptions.builder()
        .readConcern(ReadConcern.MAJORITY).readPreference(ReadPreference.primary())
        .writeConcern(WriteConcern.MAJORITY.withJournal(true))
        .build();
      return new MongoTransactionManager(mongoTemplate.getMongoDatabaseFactory(), transactionalOptions);
  }

}
