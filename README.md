# Spring Boot Starter для логирования HTTP запросов

Выполнено как задание в открытой школе T1:
> Разработать Spring Boot Starter, который предоставит возможность логировать HTTP запросы в вашем приложении на базе Spring Boot.

## Запуск

* Клонировать проект
* Скомпилировать и инсталировать стартер
* Скомпилировать тестовые приложения
* Запустить тестовые приложения
```bash
git clone https://github.com/GibbedHead/tz3-springstarter.git
cd .\tz3-springstarter\http-logging-spring-boot-starter-parent
mvn install
# spring web приложение для проверки
cd ..\sample-web-app
mvn package
java -jar .\target\sample-web-app-0.0.1-SNAPSHOT.jar
# или reactive spring web приложение для проверки
cd ..\sample-reactive-web-app
mvn package
java -jar  .\target\sample-reactive-web-app-0.0.1-SNAPSHOT.jar
```