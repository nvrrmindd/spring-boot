package kz.kbtu.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Практика 2: проверяем, что типизированный конфиг реально подхватывает
 * значения из application.yml + application-test.yml.
 */
@SpringBootTest
@ActiveProfiles("test")
class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    void bindsValuesFromTestProfile() {
        assertThat(appProperties.getName()).isEqualTo("Practice Demo (test)");
        assertThat(appProperties.getGreetingLanguage()).isEqualTo("en");
        assertThat(appProperties.getMaxGreetings()).isEqualTo(1);
        assertThat(appProperties.getFeature().isExtraEnabled()).isFalse();
    }
}
