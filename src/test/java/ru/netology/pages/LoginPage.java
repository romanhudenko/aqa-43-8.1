package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $(By.name("login"));
    private final SelenideElement passwordField = $(By.name("password"));
    private final SelenideElement loginButton = $("[data-test-id=\"action-login\"]");
    private final SelenideElement error = $("[data-test-id=error-notification]");

    public void login(DataHelper.AuthInfo info) {
        loginField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        loginField.setValue(info.getLogin());
        passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        passwordField.setValue(info.getPassword());
        loginButton.click();
    }

    public void checkErrorElement(String errorText) {
        error.shouldBe(Condition.visible);
        error.shouldHave(Condition.text(errorText));
    }
}
