package common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Official address of an organization.
 */
public class Address implements Serializable, Validatable {

    private static final long serialVersionUID = 1L;

    private final String street;
    private final String zipCode;
    private final Location town;

    /**
     * @param street optional non-empty street
     * @param zipCode optional zip code with length at least 10
     * @param town non-null location
     */
    public Address(String street, String zipCode, Location town) {
        this.street = street;
        this.zipCode = zipCode;
        this.town = town;
    }

    public String getStreet() { return street; }
    public String getZipCode() { return zipCode; }
    public Location getTown() { return town; }

    @Override
    public boolean validate() {
        return (street == null || !street.trim().isEmpty())
                && (zipCode == null || zipCode.length() >= 10)
                && town != null && town.validate();
    }

    /**
     * @return stable representation used in sorting and output
     */
    public String toSortableString() {
        String streetPart = street == null ? "" : street;
        String zipPart = zipCode == null ? "" : zipCode;
        return streetPart + "|" + zipPart + "|" + town;
    }

    @Override
    public String toString() {
        return "Address{"
                + "street='" + street + '\''
                + ", zipCode='" + zipCode + '\''
                + ", town=" + town
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address address)) {
            return false;
        }
        return Objects.equals(street, address.street)
                && Objects.equals(zipCode, address.zipCode)
                && Objects.equals(town, address.town);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, zipCode, town);
    }
}
