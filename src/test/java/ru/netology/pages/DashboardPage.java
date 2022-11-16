package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {

    private final SelenideElement main = $("[data-test-id=dashboard]");

    public DashboardPage() {
    }

    public void isVisible() {
        main.shouldBe(Condition.visible);
    }
}