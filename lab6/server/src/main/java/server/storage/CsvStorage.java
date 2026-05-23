package server.storage;

import common.model.Address;
import common.model.Coordinates;
import common.model.Location;
import common.model.Organization;
import common.model.OrganizationType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * CSV storage for the server collection.
 */
public class CsvStorage {

    private static final Logger LOG = Logger.getLogger(CsvStorage.class.getName());
    private static final char DELIMITER = ';';
    private static final int FIELD_COUNT_WITH_KEY = 14;
    private static final int FIELD_COUNT_WITHOUT_KEY = 13;

    /**
     * Loads collection entries from CSV.
     *
     * @param fileName path to CSV file
     * @return map preserving file order
     */
    public LinkedHashMap<Integer, Organization> load(String fileName) {
        LinkedHashMap<Integer, Organization> result = new LinkedHashMap<>();
        if (fileName == null || fileName.trim().isEmpty()) {
            LOG.warning("Имя файла коллекции не задано.");
            return result;
        }
        Path path = Path.of(fileName);
        if (!Files.exists(path)) {
            LOG.warning("Файл коллекции не найден: " + fileName);
            return result;
        }
        if (!Files.isReadable(path)) {
            LOG.warning("Нет прав на чтение файла: " + fileName);
            return result;
        }
        try (Scanner scanner = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Map.Entry<Integer, Organization> entry = parseLine(line);
                    if (entry.getValue().validate()) {
                        result.put(entry.getKey(), entry.getValue());
                    } else {
                        LOG.warning("Некорректные данные в строке " + lineNumber);
                    }
                } catch (IllegalArgumentException e) {
                    LOG.warning("Ошибка разбора строки " + lineNumber + ": " + e.getMessage());
                }
            }
            LOG.info("Загружено элементов: " + result.size());
        } catch (FileNotFoundException e) {
            LOG.warning("Файл коллекции не найден: " + fileName);
        } catch (IOException e) {
            LOG.warning("Ошибка чтения файла: " + e.getMessage());
        } catch (SecurityException e) {
            LOG.warning("Нет прав на чтение файла: " + fileName);
        }
        return result;
    }

    /**
     * Saves collection entries to CSV.
     *
     * @param fileName path to CSV file
     * @param entries map entries to save
     * @return true on success
     */
    public boolean save(String fileName, Iterable<Map.Entry<Integer, Organization>> entries) {
        return saveInternal(fileName, entries, true);
    }

    /**
     * Saves collection entries without writing to {@link java.util.logging}.
     *
     * <p>Used from JVM shutdown hook, where JUL handlers may already be closing.
     *
     * @param fileName path to CSV file
     * @param entries map entries to save
     * @return true on success
     */
    public boolean saveSilently(String fileName, Iterable<Map.Entry<Integer, Organization>> entries) {
        return saveInternal(fileName, entries, false);
    }

    private boolean saveInternal(String fileName, Iterable<Map.Entry<Integer, Organization>> entries, boolean log) {
        if (fileName == null || fileName.trim().isEmpty()) {
            if (log) {
                LOG.warning("Имя файла коллекции не задано.");
            }
            return false;
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            for (Map.Entry<Integer, Organization> entry : entries) {
                writer.write(toCsvLine(entry));
                writer.write(System.lineSeparator());
            }
            return true;
        } catch (IOException e) {
            if (log) {
                LOG.warning("Ошибка записи файла: " + e.getMessage());
            }
        } catch (SecurityException e) {
            if (log) {
                LOG.warning("Нет прав на запись в файл: " + fileName);
            }
        }
        return false;
    }

    private Map.Entry<Integer, Organization> parseLine(String line) {
        List<String> fields = parseCsvLine(line);
        if (fields.size() != FIELD_COUNT_WITH_KEY && fields.size() != FIELD_COUNT_WITHOUT_KEY) {
            throw new IllegalArgumentException("Ожидалось полей: "
                    + FIELD_COUNT_WITH_KEY + " или " + FIELD_COUNT_WITHOUT_KEY
                    + ", получено: " + fields.size());
        }

        int offset = fields.size() == FIELD_COUNT_WITH_KEY ? 1 : 0;
        int key = fields.size() == FIELD_COUNT_WITH_KEY
                ? parseIntRequired(fields.get(0).trim(), "key")
                : parseIntRequired(fields.get(0).trim(), "id");

        Integer id = parseIntRequired(fields.get(offset).trim(), "id");
        String name = parseStringRequired(fields.get(offset + 1).trim(), "name");
        float coordX = parseFloatRequired(fields.get(offset + 2).trim(), "coordinates.x");
        long coordY = parseLongRequired(fields.get(offset + 3).trim(), "coordinates.y");
        ZonedDateTime creationDate = parseDateRequired(fields.get(offset + 4).trim(), "creationDate");
        int annualTurnover = parseIntRequired(fields.get(offset + 5).trim(), "annualTurnover");
        long employeesCount = parseLongRequired(fields.get(offset + 6).trim(), "employeesCount");
        OrganizationType type = parseEnumOptional(fields.get(offset + 7).trim(), OrganizationType.class);
        String street = parseOptionalString(fields.get(offset + 8).trim());
        String zipCode = parseOptionalString(fields.get(offset + 9).trim());
        double townX = parseDoubleRequired(fields.get(offset + 10).trim(), "officialAddress.town.x");
        double townY = parseDoubleRequired(fields.get(offset + 11).trim(), "officialAddress.town.y");
        String townName = parseStringRequired(fields.get(offset + 12).trim(), "officialAddress.town.name");

        Organization organization = new Organization(id, name, new Coordinates(coordX, coordY),
                creationDate, annualTurnover, employeesCount, type,
                new Address(street, zipCode, new Location(townX, townY, townName)));
        return Map.entry(key, organization);
    }

    private String toCsvLine(Map.Entry<Integer, Organization> entry) {
        Organization org = entry.getValue();
        Address address = org.getOfficialAddress();
        Location town = address.getTown();
        List<String> fields = new ArrayList<>(FIELD_COUNT_WITH_KEY);
        fields.add(String.valueOf(entry.getKey()));
        fields.add(String.valueOf(org.getId()));
        fields.add(org.getName());
        fields.add(String.valueOf(org.getCoordinates().getX()));
        fields.add(String.valueOf(org.getCoordinates().getY()));
        fields.add(org.getCreationDate().toString());
        fields.add(String.valueOf(org.getAnnualTurnover()));
        fields.add(String.valueOf(org.getEmployeesCount()));
        fields.add(org.getType() == null ? "" : org.getType().name());
        fields.add(address.getStreet() == null ? "" : address.getStreet());
        fields.add(address.getZipCode() == null ? "" : address.getZipCode());
        fields.add(String.valueOf(town.getX()));
        fields.add(String.valueOf(town.getY()));
        fields.add(town.getName());

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
        return "\"" + value.replace("\"", "\"\"") + "\"";
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

    private ZonedDateTime parseDateRequired(String raw, String field) {
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Поле " + field + " обязательно");
        }
        try {
            return ZonedDateTime.parse(raw);
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

    private String parseOptionalString(String raw) {
        return raw.isEmpty() ? null : raw;
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
