package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Critical path")
    void login() {
        LoginPage login = new LoginPage();
        DataHelper.AuthInfo auth = DataHelper.getAuthInfo();
        login.login(auth);
        VerificationPage verification = new VerificationPage();
        DashboardPage dashboard = verification.validVerify(DataHelper.getVerificationCode(auth));
        dashboard.isVisible();
    }

    @Test
    @DisplayName("Invalid verification code")
    void invalidCode() {
        LoginPage login = new LoginPage();
        DataHelper.AuthInfo auth = DataHelper.getAuthInfo();
        login.login(auth);
        VerificationPage verification = new VerificationPage();
        verification.validVerify(DataHelper.getInvalidVerificationCode());
        verification.checkErrorElement("Неверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("Invalid login credentials")
    void invalidLogin() {
        LoginPage login = new LoginPage();
        DataHelper.AuthInfo auth = DataHelper.getInvalidPasswordAuthInfo();
        login.login(auth);
        login.checkErrorElement("Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Account lock after 3 retries")
    void accountLock() {
        LoginPage login = new LoginPage();
        DataHelper.AuthInfo auth = DataHelper.getInvalidPasswordAuthInfo();
        login.login(auth);
        login.login(auth);
        login.login(auth);
        Assertions.assertEquals("blocked", DataHelper.getUserStatus(auth));
    }

    @AfterAll
    static void cleanDB() {
        DataHelper.cleanDB();
    }
}