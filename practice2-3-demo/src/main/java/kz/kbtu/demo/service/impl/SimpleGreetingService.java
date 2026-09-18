package kz.kbtu.demo.service.impl;

import kz.kbtu.demo.config.AppProperties;
import kz.kbtu.demo.service.GreetingService;
import org.springframework.stereotype.Service;

@Service
public class SimpleGreetingService implements GreetingService {

    private final AppProperties appProperties;

    public SimpleGreetingService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public String greet(String personName) {
        String lang = appProperties.getGreetingLanguage();
        if ("ru".equalsIgnoreCase(lang)) {
            return "Привет, " + personName + "!";
        }
        return "Hello, " + personName + "!";
    }
}
