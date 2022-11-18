package ru.netology.data;

import lombok.Value;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {

    private static final String dataSQL = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1";
    private static final String statusSQL = "SELECT status FROM users WHERE id = ? LIMIT 1";
    private static final String getVasyaUserIDSQL = "SELECT id FROM users WHERE login = 'vasya' LIMIT 1";
    private static final String cleanUsersSQL = "DELETE FROM users";
    private static final String cleanCardsSQL = "DELETE FROM cards";
    private static final String cleanAuthCodesSQL = "DELETE FROM auth_codes";
    private static final String cleanCardTransactionsSQL = "DELETE FROM card_transactions";

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String id;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        AuthInfo output = null;
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cards", "petya", "qwerty123"
                );
                var statement = conn.prepareStatement(getVasyaUserIDSQL)
        ) {
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    var id = rs.getString(1);
                    output = new AuthInfo("vasya", id, "qwerty123");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static AuthInfo getInvalidPasswordAuthInfo() {
        return new AuthInfo("vasya", "6200f864-3005-412c-9d3a-57de19cccd83", "0000");
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getVerificationCode(AuthInfo userData) {
        var code = "";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cards", "petya", "qwerty123"
                );
                var statement = conn.prepareStatement(dataSQL)
        ) {
            statement.setString(1, userData.getId());
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new VerificationCode(code);
    }

    public static VerificationCode getInvalidVerificationCode() {
        return new VerificationCode("000000");
    }

    public static String getUserStatus(AuthInfo userData) {
        var status = "";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cards", "petya", "qwerty123"
                );
                var statement = conn.prepareStatement(statusSQL)
        ) {
            statement.setString(1, userData.getId());
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public static void cleanDB() {
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cards", "petya", "qwerty123"
                );
                var statement = conn.createStatement()
        ) {
            statement.executeUpdate(cleanCardTransactionsSQL);
            statement.executeUpdate(cleanCardsSQL);
            statement.executeUpdate(cleanAuthCodesSQL);
            statement.executeUpdate(cleanUsersSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}