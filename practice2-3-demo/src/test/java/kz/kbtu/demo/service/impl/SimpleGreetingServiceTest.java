package kz.kbtu.demo.service.impl;

import kz.kbtu.demo.config.AppProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Практика 3: это ЮНИТ-тест без Spring вообще - никакого @SpringBootTest,
 * контекст не поднимается, тест выполняется за миллисекунды.
 *
 * Это и есть главная выгода конструкторной инъекции: раз AppProperties
 * передаётся через конструктор, можно просто написать "new AppProperties()"
 * и "new SimpleGreetingService(...)" самим, без контейнера Spring.
 * С полевой инъекцией (@Autowired на поле) так сделать не получится -
 * поле останется null, если объект не создал сам Spring.
 */
class SimpleGreetingServiceTest {

    @Test
    void greetsInEnglishByDefault() {
        AppProperties props = new AppProperties();
        props.setGreetingLanguage("en");
        SimpleGreetingService service = new SimpleGreetingService(props);

        assertThat(service.greet("Aidos")).isEqualTo("Hello, Aidos!");
    }

    @Test
    void greetsInRussianWhenConfigured() {
        AppProperties props = new AppProperties();
        props.setGreetingLanguage("ru");
        SimpleGreetingService service = new SimpleGreetingService(props);

        assertThat(service.greet("Айдос")).isEqualTo("Привет, Айдос!");
    }
}
