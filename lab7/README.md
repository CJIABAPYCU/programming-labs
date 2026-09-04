# Лабораторная работа #7

Развитие лабораторной работы №6: хранение коллекции перенесено в реляционную СУБД,
добавлены регистрация и авторизация пользователей, обработка запросов сделана многопоточной.

Проект по-прежнему разделён на три Maven-модуля:

- `common`: сериализуемая модель, типизированные сетевые запросы и ответы, фрагментация UDP-сообщений, `Credentials`.
- `server`: многопоточный UDP-сервер на `DatagramChannel` в неблокирующем режиме + слой доступа к PostgreSQL.
- `client`: интерактивный клиент на `DatagramSocket` и `DatagramPacket`.

## Хранение данных

Коллекция хранится в PostgreSQL (`server/storage/PostgresStorage.java`); файловое хранилище убрано.
`id` объектов выдаёт последовательность БД `organizations_id_seq`. Коллекция в оперативной памяти
обновляется только после успешной записи в базу; команды чтения (`show`, `info`, `print_ascending`,
`filter_contains_name`, `count_less_than_type`) работают только с памятью.

Параметры подключения — аргументы запуска либо переменные окружения
`LAB7_DB_URL`, `LAB7_DB_USER`, `LAB7_DB_PASSWORD` (по умолчанию `jdbc:postgresql://pg:5432/studs`).

## Авторизация

Команды `register <login>` и `login <login>` доступны без авторизации, все остальные — только
авторизованному пользователю. Пароли хранятся в виде MD5-хеша (`server/auth/PasswordHasher.java`),
логин и пароль передаются в поле `credentials` каждого запроса.

Каждый объект хранит логин создателя (`owner_login`). Просматривать коллекцию может любой
пользователь, изменять и удалять — только собственные объекты.

## Многопоточность

- чтение запросов — `ForkJoinPool`;
- обработка запросов — `Executors.newFixedThreadPool(...)`;
- отправка ответов — `ForkJoinPool`;
- доступ к коллекции в памяти синхронизирован через `ReentrantReadWriteLock`
  (`server/core/OrganizationRepository.java`).

## Команды клиента

```
register login
login login
help
info
show
insert key {element}
update id {element}
remove_key key
clear
execute_script file_name
exit
remove_greater {element}
replace_if_lowe key {element}
remove_greater_key key
count_less_than_type type
filter_contains_name name
print_ascending
```

## Запуск

```bash
mvn -DskipTests package

java -jar server/target/server-1.0-SNAPSHOT.jar 5454 jdbc:postgresql://localhost:5433/studs <user> <password>
java -jar client/target/client-1.0-SNAPSHOT.jar localhost 5454
```
