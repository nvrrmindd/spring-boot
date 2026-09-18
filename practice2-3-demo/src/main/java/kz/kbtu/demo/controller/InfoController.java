package kz.kbtu.demo.controller;
import kz.kbtu.demo.config.AppProperties;
import kz.kbtu.demo.service.ExtraFeatureService;
import kz.kbtu.demo.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class InfoController {

    private final AppProperties appProperties;
    private final GreetingService greetingService;
    private final Optional<ExtraFeatureService> extraFeatureService;

    public InfoController(AppProperties appProperties,
                           GreetingService greetingService,
                           Optional<ExtraFeatureService> extraFeatureService) {
        this.appProperties = appProperties;
        this.greetingService = greetingService;
        this.extraFeatureService = extraFeatureService;
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("appName", appProperties.getName());
        result.put("greetingLanguage", appProperties.getGreetingLanguage());
        result.put("maxGreetings", appProperties.getMaxGreetings());
        result.put("extraFeatureEnabled", extraFeatureService.isPresent());
        return result;
    }

    @GetMapping("/api/greet")
    public Map<String, String> greet(@RequestParam(defaultValue = "world") String name) {
        return Map.of("message", greetingService.greet(name));
    }

    @GetMapping("/api/feature")
    public Map<String, String> feature() {
        String message = extraFeatureService
                .map(ExtraFeatureService::getFeatureMessage)
                .orElse("Фича выключена (нет такого бина в контексте)");
        return Map.of("message", message);
    }
}
