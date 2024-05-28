# Spring Boot Starter для логирования HTTP запросов

Выполнено как задание в открытой школе T1:
> Разработать Spring Boot Starter, который предоставит возможность логировать HTTP запросы в вашем приложении на базе Spring Boot.

## Запуск

* Клонировать проект
* Скомпилировать и инсталировать стартер
* Скомпилировать тестовые приложения
* Запустить тестовые приложения в docker
```bash
git clone https://github.com/GibbedHead/tz3-springstarter.git
cd .\tz3-springstarter\http-logging-spring-boot-starter-parent
mvn install
# spring web приложение для проверки
cd ..\sample-web-app
mvn package
# reactive spring web приложение для проверки
cd ..\sample-reactive-web-app
mvn package
cd ..
docker compose up -d
```

## Описание реализации

* ### [sample-web-app](sample-web-app) и [sample-reactive-web-app](sample-reactive-web-app)

        Простые приложения на Spring Web и Spring Reactive Web, предоставялющие простое API для добавления и просмотре списка книг

* ### [http-logging-spring-boot-starter-parent](http-logging-spring-boot-starter-parent)

		Многомодульный проект maven, содержащий стартер и интеграционные тесты к нему.

	* [http-logging](http-logging-spring-boot-starter-parent%2Fhttp-logging)

		Логика логирования. Для Spring Web - OncePerRequestFilter, для Spring Reactive Web - WebFilter и декораторы кастомные для доступа к телам запросов/ответов.

	* [http-logging-autoconfigure](http-logging-spring-boot-starter-parent%2Fhttp-logging-autoconfigure)

		Классы автоконфигурации, создающие нужные бины

	* [http-logging-properties](http-logging-spring-boot-starter-parent%2Fhttp-logging-properties)
	
		Класс свойств, для настройки стартера. Включение/выключение всего логирования и тел запроса/ответа по отдельности.
		
	* [http-logging-spring-boot-starter](http-logging-spring-boot-starter-parent%2Fhttp-logging-spring-boot-starter)
	
		Сам модуль стартера, который и будет подключаться в приложения.
		
    * [http-logging-tests](http-logging-spring-boot-starter-parent%2Fhttp-logging-tests)
	
		Интеграционные тесты, в которых для обоих случаев проверяется что запрос к эндпоинтам вызывает логирование и есть нужный текст в логе.

## Postman

В файфле [postman.json](postman.json) есть просты тесты для обоих тестовых приложений. Позволят удобно обратиться к api и посмотреть логи приложений в docker 


## Полная задача

    Задание: Создание Spring Boot Starter для логирования HTTP запросов
    
    Описание:
    
        Ваша задача - разработать Spring Boot Starter, который предоставит возможность логировать HTTP запросы в вашем приложении на базе Spring Boot.
    
    Требования:
    
    Функциональность:
    
        Ваш Spring Boot Starter должен предоставлять возможность логировать все входящие и исходящие HTTP запросы и ответы вашего приложения.
    
        Логирование должно включать в себя
            метод запроса,
            URL,
            заголовки запроса и ответа,
            код ответа,
            время обработки запроса и т.д.
    
    Реализация:
    
        Создайте проект Maven для вашего Spring Boot Starter.
    
        Используйте Spring Boot для автоконфигурации вашего Starter.
    
        Реализуйте механизм перехвата и логирования HTTP запросов с помощью фильтров или интерцепторов Spring, или Spring AOP.
    
        Обеспечьте настройку уровня логирования и формата вывода логов.
    
    Документация:
    
        Напишите подробное описание вашего Spring Boot Starter, включая его функциональность и способы использования.
    
        Обеспечьте хорошую документацию по API и конфигурации вашего Starter.
    
    Тестирование:
    
        Напишите unit-тесты для проверки корректности работы вашего Spring Boot Starter.
    
        Покройте тестами основные сценарии использования и краевые случаи.