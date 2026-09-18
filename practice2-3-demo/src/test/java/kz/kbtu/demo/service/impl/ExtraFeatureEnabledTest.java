package kz.kbtu.demo.service.impl;

import kz.kbtu.demo.service.ExtraFeatureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Практика 3, условный бин, сторона "включено".
 * Здесь мы поверх профиля test точечно переопределяем свойство через
 * "properties = ..." прямо в аннотации теста - и бин появляется в контексте.
 */
@SpringBootTest(properties = "app.feature.extra-enabled=true")
@ActiveProfiles("test")
class ExtraFeatureEnabledTest {

    @Autowired
    private ExtraFeatureService extraFeatureService;

    @Test
    void beanIsCreatedWhenPropertyIsTrue() {
        assertThat(extraFeatureService.getFeatureMessage()).contains("включена");
    }
}
