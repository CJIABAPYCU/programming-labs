package common.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Organization implements Serializable, Comparable<Organization>, Validatable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String name;
    private final Coordinates coordinates;
    private final ZonedDateTime creationDate;
    private final int annualTurnover;
    private final long employeesCount;
    private final OrganizationType type;
    private final Address officialAddress;
    private final String ownerLogin;

    public Organization(Integer id,
                        String name,
                        Coordinates coordinates,
                        ZonedDateTime creationDate,
                        int annualTurnover,
                        long employeesCount,
                        OrganizationType type,
                        Address officialAddress) {
        this(id, name, coordinates, creationDate, annualTurnover, employeesCount, type, officialAddress, null);
    }

    public Organization(Integer id,
                        String name,
                        Coordinates coordinates,
                        ZonedDateTime creationDate,
                        int annualTurnover,
                        long employeesCount,
                        OrganizationType type,
                        Address officialAddress,
                        String ownerLogin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
        this.ownerLogin = ownerLogin;
    }

    public Organization withGenerated(Integer newId, ZonedDateTime newCreationDate) {
        return new Organization(newId, name, coordinates, newCreationDate,
                annualTurnover, employeesCount, type, officialAddress, ownerLogin);
    }

    public Organization withGeneratedAndOwner(Integer newId, ZonedDateTime newCreationDate, String owner) {
        return new Organization(newId, name, coordinates, newCreationDate,
                annualTurnover, employeesCount, type, officialAddress, owner);
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public ZonedDateTime getCreationDate() { return creationDate; }
    public int getAnnualTurnover() { return annualTurnover; }
    public long getEmployeesCount() { return employeesCount; }
    public OrganizationType getType() { return type; }
    public Address getOfficialAddress() { return officialAddress; }
    public String getOwnerLogin() { return ownerLogin; }

    public boolean validateWithoutGeneratedFields() {
        return name != null && !name.trim().isEmpty()
                && coordinates != null && coordinates.validate()
                && annualTurnover > 0
                && employeesCount > 0
                && officialAddress != null && officialAddress.validate();
    }

    @Override
    public boolean validate() {
        return id != null && id > 0
                && creationDate != null
                && validateWithoutGeneratedFields();
    }

    @Override
    public int compareTo(Organization other) {
        if (other == null) {
            return 1;
        }
        int cmp = this.name.compareTo(other.name);
        if (cmp != 0) {
            return cmp;
        }
        cmp = Integer.compare(this.annualTurnover, other.annualTurnover);
        if (cmp != 0) {
            return cmp;
        }
        cmp = Long.compare(this.employeesCount, other.employeesCount);
        if (cmp != 0) {
            return cmp;
        }
        int thisId = this.id == null ? 0 : this.id;
        int otherId = other.id == null ? 0 : other.id;
        return Integer.compare(thisId, otherId);
    }

    @Override
    public String toString() {
        return "Organization{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", coordinates=" + coordinates
                + ", creationDate=" + creationDate
                + ", annualTurnover=" + annualTurnover
                + ", employeesCount=" + employeesCount
                + ", type=" + type
                + ", officialAddress=" + officialAddress
                + ", owner='" + ownerLogin + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
