# Лабораторная работа #6

Проект разделён на три Maven-модуля:

- `common`: сериализуемая модель, типизированные сетевые запросы и ответы, фрагментация UDP-сообщений.
- `server`: однопоточный UDP-сервер на `DatagramChannel` в неблокирующем режиме.
- `client`: интерактивный клиент на `DatagramSocket` и `DatagramPacket`.

## Команды клиента

Команда `save` отсутствует в клиенте. Команда `exit` завершает только клиент.

```
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

На сервере доступны только консольные команды:

```
save
exit
```

## Запуск

```bash
mvn -DskipTests package

java -jar server/target/server-1.0-SNAPSHOT.jar 5454 /absolute/path/to/organizations.csv
java -jar client/target/client-1.0-SNAPSHOT.jar localhost 5454
```

CSV сохраняет ключ `LinkedHashMap` первым полем, затем поля `Organization`.

## UDP

Каждый `Request` и `Response` сериализуется как объект Java. Если сериализованный объект больше одной UDP-датаграммы, он делится на прикладные фрагменты `MessageFragment`; принимающая сторона собирает фрагменты обратно и только потом десериализует исходный объект.
