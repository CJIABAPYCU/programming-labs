package io;

import console.TextIO;
import model.Address;
import model.Coordinates;
import model.Location;
import model.Organization;
import model.OrganizationType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Выполняет сериализацию и десериализацию CSV.
 */
public class OrganizationCsvStorage {
    private static final char DELIMITER = ';';
    private static final int FIELD_COUNT = 12;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final TextIO console;

    /**
     * Создает помощник для CSV IO.
     *
     * @param console консоль для сообщений
     */
    public OrganizationCsvStorage(TextIO console) {
        this.console = console;
    }

    /**
     * Считывает коллекцию из CSV файла.
     *
     * @param fileName путь к файлу
     * @return список организаций
     */
    public List<Organization> load(String fileName) {
        List<Organization> result = new ArrayList<>();
        if (fileName == null || fileName.trim().isEmpty()) {
            console.printError("Переменная окружения LAB5_FILE не установлена.");
            return result;
        }
        Path path = Path.of(fileName);
        if (!Files.exists(path)) {
            console.printError("Файл не найден: " + fileName);
            return result;
        }
        if (!Files.isReadable(path)) {
            console.printError("Нет прав на чтение файла: " + fileName);
            return result;
        }
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Organization org = parseLine(line);
                    if (org != null && org.validate()) {
                        result.add(org);
                    } else {
                        console.printError("Некорректные данные в строке " + lineNumber + ".");
                    }
                } catch (IllegalArgumentException e) {
                    console.printError("Ошибка разбора строки " + lineNumber + ": " + e.getMessage());
                }
            }
            console.println("Загружено элементов: " + result.size() + ".");
        } catch (FileNotFoundException e) {
            console.printError("Файл не найден: " + fileName);
        } catch (SecurityException e) {
            console.printError("Нет прав на чтение файла: " + fileName);
        }
        return result;
    }

    /**
     * Записывает коллекцию в CSV файл.
     *
     * @param fileName путь к файлу
     * @param collection коллекция для записи
     * @return true, если запись выполнена успешно
     */
    public boolean save(String fileName, Iterable<Organization> collection) {
        if (fileName == null || fileName.trim().isEmpty()) {
            console.printError("Переменная окружения LAB5_FILE не установлена.");
            return false;
        }
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Organization org : collection) {
                writer.write(toCsvLine(org));
                writer.write(System.lineSeparator());
            }
            return true;
        } catch (IOException e) {
            console.printError("Ошибка записи файла: " + e.getMessage());
        } catch (SecurityException e) {
            console.printError("Нет прав на запись в файл: " + fileName);
        }
        return false;
    }

    private Organization parseLine(String line) {
        List<String> fields = parseCsvLine(line);
        if (fields.size() < FIELD_COUNT) {
            throw new IllegalArgumentException("Ожидалось полей: " + FIELD_COUNT + ", получено: " + fields.size());
        }
        String idRaw = fields.get(0).trim();
        String nameRaw = fields.get(1).trim();
        String coordXRaw = fields.get(2).trim();
        String coordYRaw = fields.get(3).trim();
        String creationRaw = fields.get(4).trim();
        String turnoverRaw = fields.get(5).trim();
        String employeesRaw = fields.get(6).trim();
        String typeRaw = fields.get(7).trim();
        String streetRaw = fields.get(8).trim();
        String townXRaw = fields.get(9).trim();
        String townYRaw = fields.get(10).trim();
        String townNameRaw = fields.get(11).trim();

        int id = parseIntRequired(idRaw, "id");
        String name = parseStringRequired(nameRaw, "name");
        double coordX = parseDoubleRequired(coordXRaw, "coordinates.x");
        float coordY = parseFloatRequired(coordYRaw, "coordinates.y");
        Coordinates coordinates = new Coordinates(coordX, coordY);
        LocalDateTime creationDate = parseDateRequired(creationRaw, "creationDate");
        long annualTurnover = parseLongRequired(turnoverRaw, "annualTurnover");
        long employeesCount = parseLongRequired(employeesRaw, "employeesCount");
        OrganizationType type = parseEnumOptional(typeRaw, OrganizationType.class);

        String street = streetRaw.isEmpty() ? null : streetRaw;
        Location town = null;
        boolean anyTownField = !townXRaw.isEmpty() || !townYRaw.isEmpty() || !townNameRaw.isEmpty();
        if (anyTownField) {
            if (townXRaw.isEmpty() || townYRaw.isEmpty()) {
                throw new IllegalArgumentException("Для town обязательны x и y");
            }
            Integer townX = parseIntRequired(townXRaw, "town.x");
            Double townY = parseDoubleRequired(townYRaw, "town.y");
            String townName = townNameRaw.isEmpty() ? null : townNameRaw;
            town = new Location(townX, townY, townName);
        }

        Address address = null;
        if (street != null || town != null) {
            address = new Address(street, town);
        }

        return new Organization(id, name, coordinates, creationDate, annualTurnover,
                employeesCount, type, address);
    }

    private String toCsvLine(Organization org) {
        List<String> fields = new ArrayList<>(FIELD_COUNT);
        fields.add(String.valueOf(org.getId()));
        fields.add(org.getName());
        fields.add(String.valueOf(org.getCoordinates().getX()));
        fields.add(String.valueOf(org.getCoordinates().getY()));
        fields.add(org.getCreationDate().format(DATE_FORMATTER));
        fields.add(String.valueOf(org.getAnnualTurnover()));
        fields.add(String.valueOf(org.getEmployeesCount()));
        fields.add(org.getType() == null ? "" : org.getType().name());

        Address address = org.getOfficialAddress();
        String street = "";
        String townX = "";
        String townY = "";
        String townName = "";
        if (address != null) {
            if (address.getStreet() != null) {
                street = address.getStreet();
            }
            if (address.getTown() != null) {
                townX = String.valueOf(address.getTown().getX());
                townY = String.valueOf(address.getTown().getY());
                if (address.getTown().getName() != null) {
                    townName = address.getTown().getName();
                }
            }
        }
        fields.add(street);
        fields.add(townX);
        fields.add(townY);
        fields.add(townName);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) {
                sb.append(DELIMITER);
            }
            sb.append(escapeCsv(fields.get(i)));
        }
        return sb.toString();
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == DELIMITER && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        boolean needQuotes = value.indexOf(DELIMITER) >= 0
                || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0
                || value.indexOf('\r') >= 0;
        if (!needQuotes) {
            return value;
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private int parseIntRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + field + " должно быть целым числом");
        }
    }

    private long parseLongRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + field + " должно быть целым числом");
        }
    }

    private double parseDoubleRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + field + " должно быть числом");
        }
    }

    private float parseFloatRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        try {
            return Float.parseFloat(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Поле " + field + " должно быть числом");
        }
    }

    private LocalDateTime parseDateRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        try {
            return LocalDateTime.parse(raw, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Поле " + field + " имеет неверный формат");
        }
    }

    private String parseStringRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        return raw;
    }

    private <T extends Enum<T>> T parseEnumOptional(String raw, Class<T> enumClass) {
        if (raw.isEmpty()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, raw);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимое значение для " + enumClass.getSimpleName());
        }
    }
}
