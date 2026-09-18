package kz.kbtu.demo.service.impl;

import kz.kbtu.demo.service.ExtraFeatureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Практика 3, условный бин, сторона "выключено".
 * Профиль test даёт app.feature.extra-enabled=false -> бина в контексте нет.
 */
@SpringBootTest
@ActiveProfiles("test")
class ExtraFeatureDisabledTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void beanIsNotCreatedWhenPropertyIsFalse() {
        assertThatThrownBy(() -> context.getBean(ExtraFeatureService.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);
    }
}
