package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestDateChange {
    DataGenerator dataGenerator = new DataGenerator();

    String city = DataGenerator.generateCity();
    String name = DataGenerator.generateName();
    String phone = DataGenerator.generateNumberPhone();
    String planingDate = dataGenerator.generateDate(8, "dd.MM.yyyy");
    String changeDate = dataGenerator.generateDate(11, "dd.MM.yyyy");


    @BeforeAll
    public static void addAListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void deleteListener() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void openDeliveryCard() {
        open("http://localhost:9999");
    }


    @Test

    public void appCardDeliveryTest() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue(city);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planingDate);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=success-notification] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Успешно"));
        $("[data-test-id=success-notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно запланирована на " + planingDate));
        $(".calendar-input__custom-control input").doubleClick().sendKeys(changeDate);
        $(".button").click();
        $("[data-test-id=replan-notification] .notification__title")
                .shouldHave(exactText("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification] .notification__title")
                .shouldHave(exactText("Успешно!"), Duration.ofSeconds(40));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + changeDate), Duration.ofSeconds(20));

    }

}
