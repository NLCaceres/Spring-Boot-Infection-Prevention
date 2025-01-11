package edu.usc.nlcaceres.infectionprotection_backend;

import com.mongodb.ConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

// ?: AbstractMongoClientConfiguration is also an option, overriding its MongoClient
// ?: and DbName methods BUT this MongoDbConfig seems more Spring-like,
@Configuration // ?: despite the sorta unclear docs
public class MongoDbConfig {
    @Value("${SPRING_ENV:}")
    private String springEnv;

    @Value("${MONGO_USER:}")
    private String mongoUser;

    @Value("${MONGO_PASSWORD:}")
    private String mongoPassword;

    @Value("${MONGO_HOST:}")
    private String mongoHost;

    @Value("${MONGO_APP_NAME:}")
    private String mongoAppName;

    @Value("${MONGO_DB:dev}")
    private String databaseName;

    private boolean isDevMode() {
        return springEnv.equals("dev");
    }
    private boolean isTesting() {
        return springEnv.equals("test");
    }
    private String mongoURI() {
        // - Using StringBuilder to form a Mongo URL similar to:
        // - "mongodb+srv://${MONGO_USER}:${MONGO_PASSWORD}@${MONGO_HOST}"
        StringBuilder sb = new StringBuilder(124);
        sb.append(isDevMode() ? "mongodb" : "mongodb+srv");
        sb.append("://");
        sb.append(isDevMode() ? "root" : mongoUser);
        sb.append(":");
        sb.append(isDevMode() ? "example" : mongoPassword);
        sb.append("@");
        sb.append(isTesting() ? "localhost:27017" : isDevMode() ? "mongodb:27017" : mongoHost);
        sb.append("/");
        // ?: Since no need to set the DB Name, just set the queryParam-based options
        sb.append("?retryWrites=true&w=majority&appName=");
        // ?: The "appName" option is fairly new and helps identify the client
        sb.append(mongoAppName); // ?: particularly in logs kept by Mongo itself
        return sb.toString();
    }

    // ?: Better to use Spring's MongoClientFactoryBean over the actual MongoClient type
    @Bean // ?: since Spring's FactoryBean displays better error messages + is more configurable
    public MongoClientFactoryBean mongoClient() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setConnectionString(new ConnectionString(mongoURI()));
        return mongo;
    }

    // ?: MongoTemplates perform MongoDB operations so must make a custom Bean for the
    @Bean // ?: app MongoRepositories use to connect and modify the database
    public MongoTemplate mongoTemplate() throws Exception { // ?: Set DB name here, NOT in mongoURI()
        return new MongoTemplate(mongoClient().getObject(), databaseName);
    }
}
