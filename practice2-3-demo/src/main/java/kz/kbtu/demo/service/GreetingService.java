package kz.kbtu.demo.service;

/**
 * Practice 3: сервисный слой. Интерфейс отделяет "что делает сервис"
 * от "как он это делает" - контроллеру не важно, какая реализация
 * подставится, это решает Spring.
 */
public interface GreetingService {
    String greet(String personName);
}
