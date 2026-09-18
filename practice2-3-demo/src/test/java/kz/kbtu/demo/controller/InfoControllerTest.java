package kz.kbtu.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционный тест: поднимаем весь контекст на случайном порту
 * (см. server.port: 0 в application-test.yml) и реально стучимся по HTTP.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InfoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void infoReflectsTestProfileConfig() {
        Map<?, ?> body = restTemplate.getForObject("/api/info", Map.class);

        assertThat(body.get("appName")).isEqualTo("Practice Demo (test)");
        assertThat(body.get("greetingLanguage")).isEqualTo("en");
        assertThat(body.get("extraFeatureEnabled")).isEqualTo(false);
    }

    @Test
    void greetUsesConfiguredLanguage() {
        Map<?, ?> body = restTemplate.getForObject("/api/greet?name=Nazyken", Map.class);

        assertThat(body.get("message")).isEqualTo("Hello, Nazyken!");
    }
}
