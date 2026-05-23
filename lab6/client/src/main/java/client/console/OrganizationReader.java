package client.console;

import common.model.Address;
import common.model.Coordinates;
import common.model.Location;
import common.model.Organization;
import common.model.OrganizationType;

import java.util.Locale;

/**
 * Reads Organization fields from console or script input.
 */
public class OrganizationReader {

    private final TextIO console;

    /**
     * @param console text IO
     */
    public OrganizationReader(TextIO console) {
        this.console = console;
    }

    /**
     * Reads an organization without generated fields.
     *
     * @return draft organization
     * @throws InputAbortException if input ends unexpectedly
     */
    public Organization readNew() throws InputAbortException {
        return readDraft();
    }

    /**
     * Reads an organization for update. The id is assigned on the server.
     *
     * @param id ignored generated id
     * @return draft organization
     * @throws InputAbortException if input ends unexpectedly
     */
    public Organization readForUpdate(int id) throws InputAbortException {
        return readDraft();
    }

    private Organization readDraft() throws InputAbortException {
        String name = readRequiredString("name");
        Coordinates coordinates = readCoordinates();
        int annualTurnover = readRequiredInt("annualTurnover", 1);
        long employeesCount = readRequiredLong("employeesCount", 1);
        OrganizationType type = readOptionalEnum("type", OrganizationType.class);
        Address address = readAddress();
        return new Organization(null, name, coordinates, null,
                annualTurnover, employeesCount, type, address);
    }

    private Coordinates readCoordinates() throws InputAbortException {
        float x = readRequiredFloat("coordinates.x");
        long y = readRequiredLongExclusive("coordinates.y", -323);
        return new Coordinates(x, y);
    }

    private Address readAddress() throws InputAbortException {
        String street = readOptionalNonEmptyString("officialAddress.street");
        String zipCode = readOptionalStringMinLength("officialAddress.zipCode", 10);
        double townX = readRequiredDouble("officialAddress.town.x");
        double townY = readRequiredDouble("officialAddress.town.y");
        String townName = readRequiredString("officialAddress.town.name");
        return new Address(street, zipCode, new Location(townX, townY, townName));
    }

    private String readRequiredString(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            if (!line.isEmpty()) {
                return line;
            }
            console.printError("Значение не может быть пустым.");
        }
    }

    private String readOptionalNonEmptyString(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + " или пусто для null: ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                return null;
            }
            return line;
        }
    }

    private String readOptionalStringMinLength(String fieldName, int minLength) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + " или пусто для null: ");
            String line = readLineStrict().trim();
            if (line.isEmpty()) {
                return null;
            }
            if (line.length() >= minLength) {
                return line;
            }
            console.printError("Длина строки должна быть не меньше " + minLength + ".");
        }
    }

    private int readRequiredInt(String fieldName, int minInclusive) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= minInclusive) {
                    return value;
                }
                console.printError("Значение должно быть >= " + minInclusive + ".");
            } catch (NumberFormatException e) {
                console.printError("Некорректное целое число.");
            }
        }
    }

    private long readRequiredLong(String fieldName, long minInclusive) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            try {
                long value = Long.parseLong(line);
                if (value >= minInclusive) {
                    return value;
                }
                console.printError("Значение должно быть >= " + minInclusive + ".");
            } catch (NumberFormatException e) {
                console.printError("Некорректное целое число.");
            }
        }
    }

    private long readRequiredLongExclusive(String fieldName, long minExclusive) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            try {
                long value = Long.parseLong(line);
                if (value > minExclusive) {
                    return value;
                }
                console.printError("Значение должно быть > " + minExclusive + ".");
            } catch (NumberFormatException e) {
                console.printError("Некорректное целое число.");
            }
        }
    }

    private float readRequiredFloat(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            try {
                return Float.parseFloat(line);
            } catch (NumberFormatException e) {
                console.printError("Некорректное число.");
            }
        }
    }

    private double readRequiredDouble(String fieldName) throws InputAbortException {
        while (true) {
            console.print("Введите " + fieldName + ": ");
            String line = readLineStrict().trim();
            try {
                return Double.parseDouble(line);
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
