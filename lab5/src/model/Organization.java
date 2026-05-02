package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность организации.
 */
public class Organization implements Comparable<Organization>, Validatable {
    private final int id;
    private final String name;
    private final Coordinates coordinates;
    private final LocalDateTime creationDate;
    private final long annualTurnover;
    private final Long employeesCount;
    private final OrganizationType type;
    private final Address officialAddress;

    /**
     * Полный конструктор для загрузки из хранилища.
     *
     * @param id id (должен быть больше 0 и уникальным)
     * @param name поле name (не null и не пустое)
     * @param coordinates поле coordinates (не null)
     * @param creationDate дата создания (не null)
     * @param annualTurnover annualTurnover (должен быть больше 0)
     * @param employeesCount employeesCount (не null и больше 0)
     * @param type тип организации (может быть null)
     * @param officialAddress официальный адрес (может быть null)
     */
    public Organization(int id,
                        String name,
                        Coordinates coordinates,
                        LocalDateTime creationDate,
                        long annualTurnover,
                        Long employeesCount,
                        OrganizationType type,
                        Address officialAddress) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    /**
     * Конструктор для новых объектов с автоматически сгенерированной датой создания.
     *
     * @param id id (должен быть больше 0 и уникальным)
     * @param name поле name (не null и не пустое)
     * @param coordinates поле coordinates (не null)
     * @param annualTurnover annualTurnover (должен быть больше 0)
     * @param employeesCount employeesCount (не null и больше 0)
     * @param type тип организации (может быть null)
     * @param officialAddress официальный адрес (может быть null)
     */
    public Organization(int id,
                        String name,
                        Coordinates coordinates,
                        long annualTurnover,
                        Long employeesCount,
                        OrganizationType type,
                        Address officialAddress) {
        this(id, name, coordinates, LocalDateTime.now(), annualTurnover, employeesCount, type, officialAddress);
    }

    /**
     * Возвращает идентификатор организации.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя организации.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты организации.
     *
     * @return coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает дату создания организации.
     *
     * @return LocalDateTime
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает годовой оборот организации.
     *
     * @return annualTurnover
     */
    public long getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Возвращает количество сотрудников.
     *
     * @return employeesCount
     */
    public Long getEmployeesCount() {
        return employeesCount;
    }

    /**
     * Возвращает тип организации.
     *
     * @return OrganizationType
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     * Возвращает официальный адрес организации.
     *
     * @return officialAddress
     */
    public Address getOfficialAddress() {
        return officialAddress;
    }

    @Override
    public boolean validate() {
        if (id <= 0) {
            return false;

        }
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (coordinates == null || !coordinates.validate()) {
            return false;
        }
        if (creationDate == null) {
            return false;
        }
        if (annualTurnover <= 0) {
            return false;
        }
        if (employeesCount == null || employeesCount <= 0) {
            return false;
        }
        return officialAddress == null || officialAddress.validate();
    }

    @Override
    public int compareTo(Organization other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", annualTurnover=" + annualTurnover +
                ", employeesCount=" + employeesCount +
                ", type=" + type +
                ", officialAddress=" + officialAddress +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization that = (Organization) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
