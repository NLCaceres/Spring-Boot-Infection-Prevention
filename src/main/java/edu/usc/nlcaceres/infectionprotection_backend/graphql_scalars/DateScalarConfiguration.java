package edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars;

import graphql.language.StringValue;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@Configuration
public class DateScalarConfiguration {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        GraphQLScalarType dateScalar = GraphQLScalarType.newScalar().name("Date").description("Java 8 Instant as a GraphQL scalar.")
                .coercing(new Coercing<Instant, String>() {
                    @Override // This is called when a query is run, parsing the graphQL input into a Java Instant
                    public String serialize(final Object dataFetcherResult) {
                        if (dataFetcherResult instanceof Instant) {
                            return dataFetcherResult.toString();
                        } else { // Each func should throw a SPECIFIC Exception if it fails
                            throw new CoercingSerializeException("Expected an Instant object.");
                        } // If this func threw any other Exception, graphQL would likely fail to return any data
                    }
                    @Override // This will be called if a mutation is run with JSON as input with interpolated params
                    public Instant parseValue(final Object input) {
                        try {
                            if (input instanceof String) {
                                return Instant.parse((String) input);
                            } else {
                                throw new CoercingParseValueException("Expected a String");
                            }
                        } catch (DateTimeParseException e) { // MUST throw a CoercingParseValueException
                            throw new CoercingParseValueException(String.format("Not a valid date: '%s'.", input), e);
                        } // Or the mutation will likely not go through
                    }
                    @Override // This will be called if a mutation is run "inline" i.e. someMutation(employee: "John", date: "12-01-12")
                    public Instant parseLiteral(final Object input) {
                        if (input instanceof StringValue) {
                            try {
                                return Instant.parse(((StringValue) input).getValue());
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseLiteralException(e);
                            }
                        } else { // Must throw a CoercingParseLiteralException
                            throw new CoercingParseLiteralException("Expected a StringValue.");
                        } // Or the mutation will likely not go through
                    }
                }).build();
        return (builder) -> builder.scalar(dateScalar);
    }
}
