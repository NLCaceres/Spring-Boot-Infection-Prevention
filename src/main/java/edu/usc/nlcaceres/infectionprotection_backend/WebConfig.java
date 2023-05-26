package edu.usc.nlcaceres.infectionprotection_backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        PathPatternParser patternParser = new PathPatternParser();
        patternParser.setCaseSensitive(false);
        configurer.setPatternParser(patternParser);
    }
}
