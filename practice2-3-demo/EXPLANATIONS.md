# Объяснение: Practice 2 + Practice 3

Важно сразу: я собрал весь проект руками, но **не смог прогнать `mvn test` здесь в
песочнице** — у неё нет доступа к Maven Central (там просто закрыт сетевой доступ,
даже `curl` до `repo.maven.apache.org` блокируется). Код я вычитал вручную и
прогнал через `javac`, чтобы отсеять синтаксические ошибки — их нет. Но финальный
`mvn test` тебе нужно будет запустить у себя (в IntelliJ/терминале с интернетом) —
инструкция ниже. Если что-то не соберётся — присылай ошибку, разберём.

---

## Структура проекта

```
practice2-3-demo/
├── pom.xml
├── src/main/java/kz/kbtu/demo/
│   ├── DemoApplication.java
│   ├── config/AppProperties.java
│   ├── service/GreetingService.java
│   ├── service/ExtraFeatureService.java
│   ├── service/impl/SimpleGreetingService.java
│   ├── service/impl/ExtraFeatureServiceImpl.java
│   └── controller/InfoController.java
├── src/main/resources/
│   ├── application.yml       (общий конфиг)
│   ├── application-dev.yml   (профиль dev)
│   └── application-test.yml  (профиль test)
└── src/test/java/kz/kbtu/demo/...  (5 тестов)
```

---

## Practice 2 — Maven-зависимости, профили dev/test, `@ConfigurationProperties`

### 1. Зависимости через Maven (`pom.xml`)

Я взял `spring-boot-starter-parent` как `<parent>` — это главный трюк Spring Boot:
родительский POM сам решает, какие версии всех библиотек совместимы друг с другом.
Поэтому в `<dependencies>` версии **не указаны вообще** — их подставляет parent.

Добавлены три зависимости:
- `spring-boot-starter-web` — чтобы был REST-контроллер (`InfoController`);
- `spring-boot-configuration-processor` (`optional`) — генерирует
  подсказки для `application.yml` в IDE (автодополнение по `app.`);
- `spring-boot-starter-test` (`scope: test`) — JUnit 5 + AssertJ + Spring Test,
  всё нужное для тестов одним пакетом.

Это и есть "добавить зависимости с помощью Maven" — просто перечислить
`<dependency>` без версий, потому что parent их сам подставит.

### 2. Профили dev/test

Три yml-файла:
- `application.yml` — база, действует **всегда**;
- `application-dev.yml` — накатывается **поверх** базы, если активен профиль `dev`;
- `application-test.yml` — то же самое, но для профиля `test`.

Как это работает: Spring Boot сначала грузит `application.yml`, а потом, если
задан активный профиль, догружает `application-<профиль>.yml` и **перезатирает**
совпадающие ключи. Всё, что профиль не переопределил, остаётся из базового файла.

Как переключать профиль:

```bash
# запуск с профилем dev (порт 8081, язык ru, доп. фича включена)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# или через переменную окружения
SPRING_PROFILES_ACTIVE=dev java -jar target/practice2-3-demo-1.0.0.jar
```

Для профиля `test` ничего запускать вручную не нужно — тесты сами указывают
`@ActiveProfiles("test")`, и Spring подхватывает `application-test.yml`.

### 3. Типизированный `@ConfigurationProperties` (`AppProperties.java`)

До этого весь курс, скорее всего, показывал `@Value("${app.name}")` —
это работает, но:
- строку `"${app.name}"` компилятор не проверяет — опечатался, получил `null`
  в рантайме, а не ошибку сборки;
- если таких `@Value` в проекте 20 штук, они разбросаны по всему коду.

`@ConfigurationProperties(prefix = "app")` вместо этого делает **один класс**,
который целиком описывает секцию `app:` из yml. Поля называются camelCase
(`greetingLanguage`), а в yml они kebab-case (`greeting-language`) — Spring сам
их сопоставляет.

Чтобы Spring вообще узнал про этот класс, в `DemoApplication` добавлена
аннотация `@ConfigurationPropertiesScan` — она находит все `@ConfigurationProperties`
классы в пакете и делает из них бины. Альтернатива — `@EnableConfigurationProperties(AppProperties.class)`,
но со scan-вариантом не нужно руками регистрировать каждый новый класс настроек.

---

## Practice 3 — конструкторная инъекция + один условный бин

### 1. Конструкторная инъекция (`SimpleGreetingService.java`)

Как это чаще всего пишут "по умолчанию" (полевая инъекция):

```java
@Service
public class SimpleGreetingService implements GreetingService {
    @Autowired
    private AppProperties appProperties;   // Spring сам подставит значение
}
```

Проблема: `appProperties` тут не `final`, и создать этот класс без Spring-контейнера
невозможно — поле просто останется `null`. Из-за этого юнит-тест сервиса без
поднятия всего Spring-контекста написать нельзя.

После рефакторинга — конструкторная инъекция:

```java
@Service
public class SimpleGreetingService implements GreetingService {
    private final AppProperties appProperties;

    public SimpleGreetingService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
}
```

Плюсы:
- `appProperties` теперь `final` — гарантированно не `null` после создания
  объекта, и его нельзя переприсвоить;
- зависимости видно прямо в сигнатуре конструктора — не нужно лезть в тело класса;
- юнит-тест пишется как обычный Java-код, без Spring:
  `new SimpleGreetingService(new AppProperties())` — см. `SimpleGreetingServiceTest.java`.

Если в классе только один конструктор, `@Autowired` над ним даже не обязателен —
Spring подставит зависимости автоматически. Я оставил его без аннотации специально,
чтобы показать, что так тоже можно (но если конструкторов несколько, `@Autowired`
над нужным уже потребуется).

### 2. Один условный бин (`ExtraFeatureServiceImpl.java`)

```java
@Component
@ConditionalOnProperty(prefix = "app.feature", name = "extra-enabled", havingValue = "true")
public class ExtraFeatureServiceImpl implements ExtraFeatureService { ... }
```

Это значит: Spring создаёт этот бин **только если** в конфиге
`app.feature.extra-enabled=true`. Смотри профили:
- `application.yml` (база) → `false` → бина нет;
- `application-dev.yml` → `true` → бин есть;
- `application-test.yml` → `false` → бина нет.

В `InfoController` этот бин внедряется не напрямую, а через `Optional<ExtraFeatureService>` —
это официальный способ Spring сказать "бин может отсутствовать, не роняй приложение
из-за этого". Если бы бин был обязательным параметром конструктора, а его не
оказалось в контексте — приложение бы вообще не запустилось.

---

## Как всё это проверить у себя

```bash
cd practice2-3-demo

# 1. Тесты (проверяют и Practice 2, и Practice 3 сразу)
mvn test

# 2. Запуск в dev-профиле (порт 8081, язык ru, фича включена)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
curl http://localhost:8081/api/info
curl http://localhost:8081/api/greet?name=Almas
curl http://localhost:8081/api/feature

# 3. Запуск без профиля (порт 8080 по умолчанию, язык en, фича выключена)
mvn spring-boot:run
curl http://localhost:8080/api/info
```

`/api/info` в dev должен вернуть что-то вроде:
```json
{"appName":"Practice Demo","greetingLanguage":"ru","maxGreetings":5,"extraFeatureEnabled":true}
```

### Тесты, которые я написал (5 штук)

| Файл | Что проверяет |
|---|---|
| `AppPropertiesTest` | значения из `application-test.yml` реально попадают в `AppProperties` |
| `SimpleGreetingServiceTest` | сервис работает и на en, и на ru — без поднятия Spring |
| `ExtraFeatureDisabledTest` | в профиле test бина `ExtraFeatureService` нет в контексте |
| `ExtraFeatureEnabledTest` | если включить свойство — бин появляется |
| `InfoControllerTest` | реальный HTTP-запрос к `/api/info` и `/api/greet` отдаёт правильные данные |

Если преподаватель просит именно "показать разницу до/после" для конструкторной
инъекции — в комментариях `SimpleGreetingService.java` прямо расписан вариант
"как было" через `@Autowired`-поле, можно вставить его в отдельный коммит как "до".
