package console;

import model.Address;
import model.Coordinates;
import model.Location;
import model.Organization;
import model.OrganizationType;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Считывает поля организации из консольного ввода.
 */
public class OrganizationReader {
    private final TextIO console;

    /**
     * Создает считыватель ввода.
     *
     * @param console используемая консоль
     */
    public OrganizationReader(TextIO console) {
        this.console = console;
    }

    /**
     * Считывает новую организацию с автоматически сгенерированной датой создания.
     *
     * @param id сгенерированный id
     * @return созданная организация
     * @throws InputAbortException если ввод прерван
     */
    public Organization readNew(int id) throws InputAbortException {
        return readForUpdate(id, LocalDateTime.now());
    }

    /**
     * Считывает поля организации с фиксированными id и датой создания.
     *
     * @param id существующий id
     * @param creationDate существующая дата создания
     * @return созданная организация
     * @throws InputAbortException если ввод прерван
     */
    public Organization readForUpdate(int id, LocalDateTime creationDate) throws InputAbortException {
        String name = readRequiredString("name");
        Coordinates coordinates = readCoordinates();
        long annualTurnover = readRequiredLong("annualTurnover", 1);
        long employeesCount = readRequiredLong("employeesCount", 1);
        OrganizationType type = readOptionalEnum("type", OrganizationType.class);
        Address officialAddress = readAddress();
        return new Organization(id, name, coordinates, creationDate, annualTurnover,
                employeesCount, type, officialAddress);
    }

    private Coordinates readCoordinates() throws InputAbortException {
        double x = readRequiredDouble("coordinates.x");
        float y = readRequiredFloat("coordinates.y (должно быть больше -913)", -913);
        return new Coordinates(x, y);
    }

    private Address readAddress() throws InputAbortException {
        String street = readOptionalString("officialAddress.street (пусто для null)");
        Integer townX = readOptionalInteger("officialAddress.town.x (пусто для null)");
        Location town = null;
        if (townX != null) {
            Double townY = readRequiredDoubleBoxed("officialAddress.town.y");
            String townName = readOptionalString("officialAddress.town.name (пусто для null)");
            town = new Location(townX, townY, townName);
        }
        if (street == null && town == null) {
            return null;
        }
        return new Address(street, town);
    }

    private String readRequiredString(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict();
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                return trimmed;
            }
            console.printError("Значение не может быть пустым.");
        }
    }

    private String readOptionalString(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict();
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                return null;
            }
            return trimmed;
        }
    }

    private long readRequiredLong(String fieldName, long minInclusive) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                console.printError("Значение не может быть пустым.");
                continue;
            }
            try {
                long value = Long.parseLong(line);
                if (value >= minInclusive) {
                    return value;
                }
                console.printError("Значение должно быть >= " + minInclusive + ".");
            } catch (NumberFormatException e) {
                console.printError("Некорректное число.");
            }
        }
    }

    private double readRequiredDouble(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                console.printError("Значение не может быть пустым.");
                continue;
            }
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                console.printError("Некорректное число.");
            }
        }
    }

    private Double readRequiredDoubleBoxed(String fieldName) throws InputAbortException {
        return readRequiredDouble(fieldName);
    }

    private float readRequiredFloat(String fieldName, float minExclusive) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                console.printError("Значение не может быть пустым.");
                continue;
            }
            try {
                float value = Float.parseFloat(line);
                if (value > minExclusive) {
                    return value;
                }
                console.printError("Значение должно быть > " + minExclusive + ".");
            } catch (NumberFormatException e) {
                console.printError("Некорректное число.");
            }
        }
    }

    private Integer readOptionalInteger(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                return null;
            }
            try {
                return Integer.valueOf(line);
            } catch (NumberFormatException e) {
                console.printError("Некорректное число.");
            }
        }
    }

    private <T extends Enum<T>> T readOptionalEnum(String fieldName, Class<T> enumClass)
            throws InputAbortException {
        String options = buildEnumOptions(enumClass);
        while (true) {
            console.print("Введите " + fieldName + " (" + options + ") или пусто для null: ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                return null;
            }
            try {
                return Enum.valueOf(enumClass, line.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                console.printError("Недопустимое значение. Допустимые: " + options);
            }
        }
    }

    private String buildEnumOptions(Class<? extends Enum<?>> enumClass) {
        StringBuilder sb = new StringBuilder();
        for (Enum<?> value : enumClass.getEnumConstants()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(value.name());
        }
        return sb.toString();
    }

    private String readLineStrict() throws InputAbortException {
        if (!console.hasNextLine()) {
            throw new InputAbortException("Ввод прерван.");
        }
        return console.readLine();
    }
}
