package edu.usc.nlcaceres.infectionprotection_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import edu.usc.nlcaceres.infectionprotection_backend.migrations.Migrations;
import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;

@Configuration
public class MongockConfig {

  @Value("${SPRING_ENV:}") // ?: Use ":" to set default as empty string ""
  private String springEnv;

  @Bean // ?: Need a ConnectionDriver if not using @EnableMongock in the main App file
  public SpringDataMongoV4Driver mongockDriver(MongoTemplate mongoTemplate) {
    SpringDataMongoV4Driver driver = SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
    if (springEnv.equals("dev")) {
      driver.disableTransaction(); // ?: Enabled by default but unavailable without Replica sets
    }
    else {
      driver.enableTransaction();
    }
    return driver;
  }

  // ?: This Bean lets me combine `application.properties`/`properties.yaml` with this Builder's config
  @Bean // ?: Alternatively, if no `application.properties` needed, I could use the MongockApplicationRunner Bean
  public MongockInitializingBeanRunner mongockRunner(SpringDataMongoV4Driver driver, ApplicationContext context) {
    return MongockSpringboot.builder()
      .setDriver(driver).setDefaultAuthor("NLCaceres")
      .addMigrationScanPackage("edu.usc.nlcaceres.infectionprotection_backend.migrations")
      .setLockGuardEnabled(false) // Prevents Proxies from being used
      .addMigrationClasses(Migrations.changeUnits) // https://docs.spring.io/spring-framework/reference/core/aop/proxying.html
      .setSpringContext(context)
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
