package edu.usc.nlcaceres.infectionprotection_backend;

import com.mongodb.ConnectionString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDbConfig {
    // ? Alternatively could extend AbstractMongoClientConfiguration and override its mongoClient + dbName methods
    // ? BUT I wanted to go with something that seems more in line with what Spring's Ref Docs would prefer (even if the docs are kinda unclear)
    @Autowired
    Environment env;

    private boolean isDevMode() {
        return env.getProperty("SPRING_ENV", "").equals("dev");
    }
    private String getDatabaseProtocol() {
        return isDevMode() ? "mongodb" : "mongodb+srv";
    }
    private String getDatabaseName() {
        return env.getProperty("MONGO_DB", "dev");
    }
    private String getMongoUser() {
        return isDevMode() ? "root" : env.getProperty("MONGO_USER", "");
    }
    private String getMongoPassword() {
        return isDevMode() ? "example" : env.getProperty("MONGO_PASSWORD", "");
    }
    private String getMongoHost() {
        return isDevMode() ? "mongodb:27017" : env.getProperty("MONGO_HOST", "");
    }
    private String mongoURI() {
        // * Using StringBuilder to make the following String:
        // * "mongodb+srv://${MONGO_USER}:${MONGO_PASSWORD}@${MONGO_HOST}/?retryWrites=true&w=majority&appName=Cluster0"
        StringBuilder sb = new StringBuilder(124);
        sb.append(getDatabaseProtocol());
        sb.append("://");
        sb.append(getMongoUser());
        sb.append(":");
        sb.append(getMongoPassword());
        sb.append("@");
        sb.append(getMongoHost());
        sb.append("/");
        // ? Normally would set the DB name below BUT slightly simpler to let the MongoTemplate bean do it below
        // sb.append(getDatabaseName());
        // ? Since no need to set the DB Name, just set the queryParam-based options
        sb.append("?retryWrites=true&w=majority&appName=");
        // ? The "appName" option is fairly new and helps identify the client, particularly, in logs kept by the MongoDB Host
        sb.append(env.getProperty("MONGO_APP_NAME", ""));
        return sb.toString();
    }

    // ? Better to use Spring's MongoClientFactoryBean over the actual MongoClient type since the FactoryBean will
    // ? display more Spring-oriented error messaging if something goes wrong + more configuration options!
    @Bean
    public MongoClientFactoryBean mongoClient() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setConnectionString(new ConnectionString(mongoURI()));
        return mongo;
    }

    // ? MongoTemplates are responsible for actual operations on the MongoDB instance, so have to make my own Bean to ensure
    // ? my customized connection created above is actually used by this app's MongoRepos when the App loads all the Beans
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient().getObject(), getDatabaseName());
    }
}
