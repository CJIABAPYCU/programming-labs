package server.storage;

import common.model.Address;
import common.model.Coordinates;
import common.model.Location;
import common.model.Organization;
import common.model.OrganizationType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Работа с БД через JDBC.
 *
 * <p>Все публичные методы объявлены {@code synchronized}, потому что драйвер
 * PostgreSQL не является потокобезопасным: он не гарантирует синхронизацию
 * вызовов на одном {@link Connection}, и синхронизировать их должен вызывающий.
 * Запросы к серверу обрабатываются в пуле потоков, поэтому без этого два потока
 * могли бы одновременно работать с одним соединением.
 */
public class PostgresStorage {

    private static final Logger LOG = Logger.getLogger(PostgresStorage.class.getName());
    private final Connection connection;

    public PostgresStorage(DatabaseConfig config) throws SQLException {
        this.connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        LOG.info("Connected to database: " + config.getUrl());
    }

    public synchronized void initSchema() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE SEQUENCE IF NOT EXISTS organizations_id_seq START 1");
            st.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "login VARCHAR(64) PRIMARY KEY, "
                    + "password_hash VARCHAR(32) NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS organizations ("
                    + "id INTEGER PRIMARY KEY DEFAULT nextval('organizations_id_seq'), "
                    + "map_key INTEGER NOT NULL, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "coord_x REAL NOT NULL, "
                    + "coord_y BIGINT NOT NULL, "
                    + "creation_date TIMESTAMP WITH TIME ZONE NOT NULL, "
                    + "annual_turnover INTEGER NOT NULL, "
                    + "employees_count BIGINT NOT NULL, "
                    + "type VARCHAR(32), "
                    + "street VARCHAR(255), "
                    + "zip_code VARCHAR(255), "
                    + "town_x DOUBLE PRECISION NOT NULL, "
                    + "town_y DOUBLE PRECISION NOT NULL, "
                    + "town_name VARCHAR(255) NOT NULL, "
                    + "owner_login VARCHAR(64) NOT NULL REFERENCES users(login))");
        }
        LOG.info("Database schema initialized");
    }

    public synchronized boolean registerUser(String login, String passwordHash) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users (login, password_hash) VALUES (?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOG.warning("Register user failed: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean checkUser(String login, String passwordHash) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT 1 FROM users WHERE login = ? AND password_hash = ?")) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Check user failed", e);
            return false;
        }
    }

    public synchronized LinkedHashMap<Integer, Organization> loadAll() {
        LinkedHashMap<Integer, Organization> result = new LinkedHashMap<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM organizations ORDER BY id")) {
            while (rs.next()) {
                int mapKey = rs.getInt("map_key");
                Organization org = readOrganization(rs);
                result.put(mapKey, org);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to load organizations", e);
        }
        LOG.info("Loaded organizations from DB: " + result.size());
        return result;
    }

    public synchronized int insert(int mapKey, Organization org, String owner) {
        String sql = "INSERT INTO organizations "
                + "(map_key, name, coord_x, coord_y, creation_date, annual_turnover, "
                + "employees_count, type, street, zip_code, town_x, town_y, town_name, owner_login) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            fillOrganizationParams(ps, mapKey, org, owner);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Insert failed", e);
        }
        return -1;
    }

    public synchronized boolean update(int id, Organization org) {
        String sql = "UPDATE organizations SET name=?, coord_x=?, coord_y=?, creation_date=?, "
                + "annual_turnover=?, employees_count=?, type=?, street=?, zip_code=?, "
                + "town_x=?, town_y=?, town_name=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, org.getName());
            ps.setFloat(i++, org.getCoordinates().getX());
            ps.setLong(i++, org.getCoordinates().getY());
            ps.setTimestamp(i++, Timestamp.from(org.getCreationDate().toInstant()));
            ps.setInt(i++, org.getAnnualTurnover());
            ps.setLong(i++, org.getEmployeesCount());
            if (org.getType() != null) {
                ps.setString(i++, org.getType().name());
            } else {
                ps.setNull(i++, Types.VARCHAR);
            }
            Address addr = org.getOfficialAddress();
            if (addr.getStreet() != null) {
                ps.setString(i++, addr.getStreet());
            } else {
                ps.setNull(i++, Types.VARCHAR);
            }
            if (addr.getZipCode() != null) {
                ps.setString(i++, addr.getZipCode());
            } else {
                ps.setNull(i++, Types.VARCHAR);
            }
            ps.setDouble(i++, addr.getTown().getX());
            ps.setDouble(i++, addr.getTown().getY());
            ps.setString(i++, addr.getTown().getName());
            ps.setInt(i, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Update failed", e);
            return false;
        }
    }

    public synchronized boolean deleteById(int id) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM organizations WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Delete failed", e);
            return false;
        }
    }

    public synchronized int deleteByOwner(String owner) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM organizations WHERE owner_login = ?")) {
            ps.setString(1, owner);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Delete by owner failed", e);
            return 0;
        }
    }

    public synchronized void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOG.warning("Failed to close DB connection: " + e.getMessage());
        }
    }

    private void fillOrganizationParams(PreparedStatement ps, int mapKey, Organization org, String owner)
            throws SQLException {
        int i = 1;
        ps.setInt(i++, mapKey);
        ps.setString(i++, org.getName());
        ps.setFloat(i++, org.getCoordinates().getX());
        ps.setLong(i++, org.getCoordinates().getY());
        ps.setTimestamp(i++, Timestamp.from(ZonedDateTime.now().toInstant()));
        ps.setInt(i++, org.getAnnualTurnover());
        ps.setLong(i++, org.getEmployeesCount());
        if (org.getType() != null) {
            ps.setString(i++, org.getType().name());
        } else {
            ps.setNull(i++, Types.VARCHAR);
        }
        Address addr = org.getOfficialAddress();
        if (addr.getStreet() != null) {
            ps.setString(i++, addr.getStreet());
        } else {
            ps.setNull(i++, Types.VARCHAR);
        }
        if (addr.getZipCode() != null) {
            ps.setString(i++, addr.getZipCode());
        } else {
            ps.setNull(i++, Types.VARCHAR);
        }
        ps.setDouble(i++, addr.getTown().getX());
        ps.setDouble(i++, addr.getTown().getY());
        ps.setString(i++, addr.getTown().getName());
        ps.setString(i, owner);
    }

    private Organization readOrganization(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        float coordX = rs.getFloat("coord_x");
        long coordY = rs.getLong("coord_y");
        Timestamp ts = rs.getTimestamp("creation_date");
        ZonedDateTime creationDate = ts.toInstant().atZone(ZoneId.systemDefault());
        int annualTurnover = rs.getInt("annual_turnover");
        long employeesCount = rs.getLong("employees_count");
        String typeStr = rs.getString("type");
        OrganizationType type = typeStr != null ? OrganizationType.valueOf(typeStr) : null;
        String street = rs.getString("street");
        String zipCode = rs.getString("zip_code");
        double townX = rs.getDouble("town_x");
        double townY = rs.getDouble("town_y");
        String townName = rs.getString("town_name");
        String owner = rs.getString("owner_login");

        return new Organization(id, name, new Coordinates(coordX, coordY),
                creationDate, annualTurnover, employeesCount, type,
                new Address(street, zipCode, new Location(townX, townY, townName)),
                owner);
    }
}
